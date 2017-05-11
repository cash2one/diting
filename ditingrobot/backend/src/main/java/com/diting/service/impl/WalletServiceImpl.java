package com.diting.service.impl;

import com.diting.dao.WalletLotMapper;
import com.diting.dao.WalletLotTransactionMapper;
import com.diting.dao.WalletMapper;
import com.diting.dao.WalletTransactionMapper;
import com.diting.error.AppErrors;
import com.diting.model.*;
import com.diting.model.enums.*;
import com.diting.model.options.WalletSearchOptions;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.resources.request.DebitWalletRequest;
import com.diting.service.WalletService;
import com.diting.util.ModelUtils;
import com.diting.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.diting.util.Utils.*;


/**
 * WalletServiceImpl.
 */
@Service("walletService")
@Transactional
public class WalletServiceImpl implements WalletService, InitializingBean {

    private static final String MASTER_LEGER_TYPE_SUFFIX = "_MASTER";
    private static final BigDecimal MINUS_ONE = new BigDecimal(-1);
    private static Logger logger = Logger.getLogger(WalletServiceImpl.class);

    @Autowired
    private WalletMapper walletMapper;

    @Autowired
    private WalletLotMapper walletLotMapper;

    @Autowired
    private WalletTransactionMapper transactionMapper;

    @Autowired
    private WalletLotTransactionMapper lotTransactionMapper;

    private Map<String, Wallet> masterWallets = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        for (WalletType walletType : WalletType.values()) {
            try {
                // for now, assume only one master wallet for each wallet type
                Wallet masterWallet = getWalletByLegerType(walletType + MASTER_LEGER_TYPE_SUFFIX);
                masterWallets.put(masterWallet.getType(), masterWallet);
            } catch (Exception e) {
                logger.error("Failed to load master wallet for type [" + walletType + "].");
            }
        }
    }

    @Override
    public Wallet get(Integer id){
        return walletMapper.get(id);
    }

    @Override
    public Wallet getWalletByLegerType(String legerType) {
        WalletSearchOptions options = new WalletSearchOptions();
        options.setLegerType(legerType);

        List<Wallet> results = walletMapper.getWalletByLegerType(options);

        // assume different leger type maps to different wallet account
        if (CollectionUtils.isEmpty(results)) {
            throw AppErrors.INSTANCE.missingMasterAccount().exception();
        } else {
            return getUnique(results);
        }
    }

    @Override
    public Wallet create(Wallet wallet) {
        // check wallet not exists
        if (walletMapper.checkWalletExists(wallet.getUserId(), wallet.getUserType(), wallet.getType()))
            throw AppErrors.INSTANCE.walletAlreadyExists().exception();

        //ModelUtils.checkOwner(wallet.getUserId(),wallet.getUserType());
        wallet.setCurrency(ModelEnums.DEFAULT_WALLET_CURRENCY);
        wallet.setStatus(str(WalletStatus.ACTIVE));
        wallet.setBalance(new BigDecimal("0.00"));

        walletMapper.create(wallet);

        Map<WalletLotType, Integer> lotPriorityMapping = WalletMapping.LOT_PRIORITY_MAPPING.get(wallet.getType());

        if (lotPriorityMapping == null) {
            throw AppErrors.INSTANCE.invalidWalletType(wallet.getType()).exception();
        }

        for (Map.Entry<WalletLotType, Integer> entry : lotPriorityMapping.entrySet()) {
            WalletLot lot = new WalletLot();

            lot.setWalletId(wallet.getId());
            lot.setType(str(entry.getKey()));
            lot.setBalance(new BigDecimal("0.00"));
            lot.setPriority(entry.getValue());

            walletLotMapper.create(lot);
        }
        return wallet;
    }

    @Override
    public Wallet update(Wallet wallet) {
        walletMapper.update(wallet);
        return wallet;
    }

    @Override
    public WalletTransaction credit(CreditWalletRequest creditRequest) {
        // credit specified wallet
        Result result = _credit(creditRequest);

        // debit corresponding master wallet
        DebitWalletRequest debitRequest = new DebitWalletRequest();
        debitRequest.setWalletId(getMasterWallet(result.getWallet().getType()).getId());

        debitRequest.setTrackingUuid(creditRequest.getTrackingUuid());
        debitRequest.setLotType(String.valueOf(WalletLotType.DIBI_D));
        debitRequest.setReason(creditRequest.getReason());
        debitRequest.setAmount(creditRequest.getAmount());
        debitRequest.setEvent(creditRequest.getEvent());

        _debit(debitRequest);

        return result.getTransaction();
    }

    @Override
    public WalletTransaction creditMaster(CreditWalletRequest creditRequest) {
        Result result = _credit(creditRequest);

        return result.getTransaction();
    }

    @Override
    public WalletTransaction debit(DebitWalletRequest debitRequest) {
        Result result = _debit(debitRequest);

        // credit corresponding master wallet
        for (WalletLotTransaction lotTransaction : result.getLotTransactions()) {
            CreditWalletRequest creditRequest = new CreditWalletRequest();

            creditRequest.setWalletId(getMasterWallet(str(result.getWallet().getType())).getId());

            creditRequest.setTrackingUuid(lotTransaction.getTrackingUuid());
            creditRequest.setLotType(lotTransaction.getLotType());
            creditRequest.setReason(debitRequest.getReason());
            creditRequest.setAmount(MINUS_ONE.multiply(lotTransaction.getAmount()));
            creditRequest.setEvent(debitRequest.getEvent());

            _credit(creditRequest);
        }

        return result.getTransaction();
    }

    @Override
    public BigDecimal refreshBalance(Integer walletId) {
        List<WalletLot> lots = walletLotMapper.getByWalletId(walletId);

        BigDecimal balance = BigDecimal.ZERO;
        for (WalletLot lot : lots) {
            balance = balance.add(lot.getBalance());
        }

        Wallet wallet = walletMapper.get(walletId);
        wallet.setBalance(balance);
        walletMapper.update(wallet);

        return balance;
    }

    @Override
    public BigDecimal getWalletByUserId(Integer user_id) {
        Wallet wallet=walletMapper.searchWalletByUserId(user_id);
        return null!=wallet?wallet.getBalance(): new BigDecimal(0);
    }

    @Override
    public List<Wallet> search(WalletSearchOptions options) {
        return walletMapper.search(options);
    }

    private Wallet getMasterWallet(String walletType) {
        if (masterWallets.containsKey(walletType))
            return masterWallets.get(walletType);

        // try to load master wallet account again
        Wallet masterWallet = getWalletByLegerType(walletType + MASTER_LEGER_TYPE_SUFFIX);
        if (masterWallet == null)
            throw AppErrors.INSTANCE.missingMasterAccount().exception();

        masterWallets.put(masterWallet.getType(), masterWallet);

        return masterWallet;
    }

    private Result _credit(CreditWalletRequest creditRequest) {
        checkNull(creditRequest.getAmount(), "creditAmount");
        checkNull(creditRequest.getWalletId(), "walletId");
        checkNull(creditRequest.getLotType(), "walletLotType");

        Integer walletId = creditRequest.getWalletId();
        String lotType = creditRequest.getLotType();

        // get wallet
        Wallet wallet = walletMapper.get(creditRequest.getWalletId());
        if (wallet == null)
            throw AppErrors.INSTANCE.walletNotExists().exception();

        //secured checked
        if (!Utils.equals(ModelUtils.genSecuredSign(wallet), wallet.getSign())) {
            throw new IllegalStateException("Invalid secured model sign for wallet [" + wallet.getId() + "].");
        }

//        if (!Utils.equals(wallet.getLegerType(), wallet.getType() + MASTER_LEGER_TYPE_SUFFIX))
//            ModelUtils.checkOwner(wallet.getUserId(), UserType.userTypeHandler(wallet.getUserType()));

        // generate tracking UUID if not specified
        if (isEmpty(creditRequest.getTrackingUuid()))
            creditRequest.setTrackingUuid(str(UUID.randomUUID()));

        // update wallet lot balance
        WalletLot lot = walletLotMapper.getByWalletIdAndType(str(walletId), lotType);
        if (lot==null)
            throw AppErrors.INSTANCE.creditWalletFailed().exception();

        //secured checked
        if (!Utils.equals(ModelUtils.genSecuredSign(lot), lot.getSign())) {
            throw new IllegalStateException("Invalid secured model sign for wallet lot [" + lot.getId() + "].");
        }

        lot.setBalance(lot.getBalance().add(creditRequest.getAmount()));
        walletLotMapper.update(lot);

        // create wallet transaction
        WalletTransaction transaction = new WalletTransaction();

        transaction.setTrackingUuid(creditRequest.getTrackingUuid());
        transaction.setWalletId(walletId);
        transaction.setType(str(WalletTransactionType.CREDIT));
        transaction.setReason(creditRequest.getReason());
        transaction.setAmount(creditRequest.getAmount());
        transaction.setOriginalBalance(wallet.getBalance());
        transaction.setCurrentBalance(wallet.getBalance().add(creditRequest.getAmount()));
        transaction.setEvent(creditRequest.getEvent());

        transactionMapper.create(transaction);

        // create wallet lot transaction
        WalletLotTransaction lotTransaction = new WalletLotTransaction();

        lotTransaction.setTrackingUuid(creditRequest.getTrackingUuid());
        lotTransaction.setWalletId(walletId);
        lotTransaction.setWalletLotId(lot.getId());
        lotTransaction.setAmount(creditRequest.getAmount());
        lotTransaction.setTransactionType(str(WalletTransactionType.CREDIT));
        lotTransaction.setLotType(lotType);
        lotTransaction.setTransactionId(transaction.getId());

        lotTransactionMapper.create(lotTransaction);

        // refresh wallet balance
        refreshBalance(walletId);

        // build result
        Result result = new Result();
        result.setTransaction(transaction);
        result.setLotTransactions(Arrays.asList(lotTransaction));
        result.setWallet(wallet);

        return result;
    }

    private Result _debit(DebitWalletRequest debitRequest) {
        checkNull(debitRequest.getAmount(), "debitAmount");
        checkNull(debitRequest.getWalletId(), "walletId");

        Integer walletId = debitRequest.getWalletId();

        // generate tracking UUID if not specified
        if (debitRequest.getTrackingUuid() == null)
            debitRequest.setTrackingUuid(str(UUID.randomUUID()));

        Wallet wallet = walletMapper.get(walletId);
        if (wallet == null)
            throw AppErrors.INSTANCE.walletNotExists().exception();

        //secured checked
        if (!Utils.equals(ModelUtils.genSecuredSign(wallet), wallet.getSign())) {
            throw new IllegalStateException("Invalid secured model sign for wallet [" + wallet.getId() + "].");
        }

        if (!Utils.equals(wallet.getLegerType(), wallet.getType() + MASTER_LEGER_TYPE_SUFFIX))
            ModelUtils.checkOwner(wallet.getUserId(), UserType.userTypeHandler(wallet.getUserType()));

        // check wallet balance
//        if (debitRequest.getAmount().compareTo(wallet.getBalance()) > 0) {
//            throw AppErrors.INSTANCE.insufficientWalletBalance().exception();
//        }

        // create wallet transaction
        WalletTransaction transaction = new WalletTransaction();

        transaction.setTrackingUuid(debitRequest.getTrackingUuid());
        transaction.setWalletId(walletId);
        transaction.setType(str(WalletTransactionType.DEBIT));
        transaction.setReason(debitRequest.getReason());
        transaction.setAmount(MINUS_ONE.multiply(debitRequest.getAmount()));
        transaction.setOriginalBalance(wallet.getBalance());
        transaction.setCurrentBalance(wallet.getBalance().subtract(debitRequest.getAmount()));
        transaction.setEvent(debitRequest.getEvent());

        transactionMapper.create(transaction);

        List<WalletLot> candidateLots = new ArrayList<>();

        if (debitRequest.getLotType() != null) {
            // debit against specified lot
            WalletLot lot = walletLotMapper.getByWalletIdAndType(str(walletId), debitRequest.getLotType());

            if (lot == null)
                throw AppErrors.INSTANCE.debitWalletFailed().exception();

            // check lot balance
//            if (debitRequest.getAmount().compareTo(lot.getBalance()) > 0) {
//                throw AppErrors.INSTANCE.insufficientLotBalance().exception();
//            }

            candidateLots = Arrays.asList(lot);
        }
//        else {
        // debit lot one by one, sorted by lot priority DESC
//            candidateLots = walletLotMapper.getByWalletId(walletId);
//        }

        BigDecimal remaining = debitRequest.getAmount();

        List<WalletLotTransaction> lotTransactions = new ArrayList<>();
        for (WalletLot lot : candidateLots) {

            //secured checked
            if (!Utils.equals(ModelUtils.genSecuredSign(lot), lot.getSign())) {
                throw new IllegalStateException("Invalid secured model sign for wallet lot [" + lot.getId() + "].");
            }

            // check whether continue debit or not
            if (remaining.compareTo(BigDecimal.ZERO) == 0) {
                break;
            }

            // determine debit lot amount
//            BigDecimal debitLotAmount = lot.getBalance();
//            if (remaining.compareTo(lot.getBalance()) < 0) {
//                debitLotAmount = remaining;
//            }
//
//            remaining = remaining.subtract(debitLotAmount);

            // update wallet lot balance
            lot.setBalance(lot.getBalance().subtract(remaining));
            walletLotMapper.update(lot);

            // create wallet lot transaction
            WalletLotTransaction lotTransaction = new WalletLotTransaction();

            lotTransaction.setTrackingUuid(debitRequest.getTrackingUuid());
            lotTransaction.setWalletId(walletId);
            lotTransaction.setWalletLotId(lot.getId());
            lotTransaction.setAmount(MINUS_ONE.multiply(remaining));
            lotTransaction.setTransactionType(str(WalletTransactionType.DEBIT));
            lotTransaction.setLotType(lot.getType());
            lotTransaction.setTransactionId(transaction.getId());

            lotTransactionMapper.create(lotTransaction);
            lotTransactions.add(lotTransaction);
        }

        // refresh wallet balance
        refreshBalance(walletId);

        // build result
        Result result = new Result();
        result.setTransaction(transaction);
        result.setLotTransactions(lotTransactions);
        result.setWallet(wallet);

        return result;
    }

    private static class Result {
        private Wallet wallet;
        private WalletTransaction transaction;
        private List<WalletLotTransaction> lotTransactions;

        public List<WalletLotTransaction> getLotTransactions() {
            return lotTransactions;
        }

        public void setLotTransactions(List<WalletLotTransaction> lotTransactions) {
            this.lotTransactions = lotTransactions;
        }

        public WalletTransaction getTransaction() {
            return transaction;
        }

        public void setTransaction(WalletTransaction transaction) {
            this.transaction = transaction;
        }

        public Wallet getWallet() {
            return wallet;
        }

        public void setWallet(Wallet wallet) {
            this.wallet = wallet;
        }
    }
}
