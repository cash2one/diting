package com.diting.model.options;

public class LotTransactionSearchOptions extends PageableOptions {
    private Integer walletId;
    private Integer walletLotId;
    private String lotType; // M, N, E
    private String transactionType; // CREDIT, DEBIT, REFUND, TRANSFER_IN, TRANSFER_OUT
}