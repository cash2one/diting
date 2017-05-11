package com.diting.service.impl;


import com.diting.dao.WalletLotMapper;
import com.diting.model.WalletLot;
import com.diting.service.WalletLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * WalletLotServiceImpl.
 */
@Service("walletLotService")
@Transactional
public class WalletLotServiceImpl implements WalletLotService {

    @Autowired
    private WalletLotMapper walletLotMapper;

    @Override
    public List<WalletLot> getByWalletId(Integer walletId) {
        return walletLotMapper.getByWalletId(walletId);
    }
}
