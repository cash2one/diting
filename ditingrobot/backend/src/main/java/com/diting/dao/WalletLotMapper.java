package com.diting.dao;

import com.diting.model.WalletLot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WalletLotMapper.
 */
public interface WalletLotMapper{

    void create(WalletLot walletLot);

    void update(WalletLot walletLot);

    WalletLot getByWalletIdAndType(@Param("walletId") String walletId, @Param("type") String type);

    List<WalletLot> getByWalletId(@Param("walletId") Integer walletId);
}
