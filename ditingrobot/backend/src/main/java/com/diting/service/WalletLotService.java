package com.diting.service;


import com.diting.model.WalletLot;

import java.util.List;

/**
 * WalletLotService
 */
public interface WalletLotService {
    List<WalletLot> getByWalletId(Integer walletId);
}
