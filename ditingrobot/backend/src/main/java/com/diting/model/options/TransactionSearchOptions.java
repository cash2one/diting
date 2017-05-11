package com.diting.model.options;

public class TransactionSearchOptions extends PageableOptions {
    private Integer walletId;
    private String transactionType; // CREDIT, DEBIT, REFUND, TRANSFER_IN, TRANSFER_OUT
}