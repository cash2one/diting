package com.diting.service;

import com.diting.model.Wallet;
import com.diting.model.WalletTransaction;
import com.diting.model.options.WalletSearchOptions;
import com.diting.resources.request.CreditWalletRequest;
import com.diting.resources.request.DebitWalletRequest;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {
    Wallet get(Integer id);

    Wallet getWalletByLegerType(String legerType);

    Wallet create(Wallet wallet);

    Wallet update(Wallet wallet);

    List<Wallet> search(WalletSearchOptions options);

    WalletTransaction credit(CreditWalletRequest creditRequest);

    WalletTransaction creditMaster(CreditWalletRequest creditRequest);

    WalletTransaction debit(DebitWalletRequest debitRequest);

    BigDecimal refreshBalance(Integer walletId);

    BigDecimal getWalletByUserId(Integer user_id);

}