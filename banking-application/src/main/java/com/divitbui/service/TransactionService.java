package com.divitbui.service;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.divitbui.dao.TransactionDao;
import com.divitbui.generated.tables.pojos.Transaction;
import com.divitbui.model.Amount;
import com.divitbui.model.TransactionDetails;
import com.divitbui.model.response.TransactionResponse;
import com.divitbui.model.response.TransactionsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionDao transactionDao;

    public TransactionsResponse getTransactionsByAccountIdAndUserId(final UUID accountId, final String userId) {
        return TransactionsResponse.builder()
                                   .transactions(transactionDao.fetchTransactionsByAccountIdAndUserId(accountId, userId)
                                                               .stream()
                                                               .map(this::convertToTransactionResponse)
                                                               .collect(Collectors.toList()))
                                   .build();
    }

    public TransactionsResponse getTransactionsByUserId(final String userId) {
        return TransactionsResponse.builder()
                                   .transactions(transactionDao.fetchAllTransactionsByUserId(userId)
                                                               .stream()
                                                               .map(this::convertToTransactionResponse)
                                                               .collect(Collectors.toList()))
                                   .build();
    }

    private TransactionResponse convertToTransactionResponse(final Transaction t) {
        final TransactionResponse response = TransactionResponse.builder()
                                                                .transactionId(t.getUuid())
                                                                .transactionAmount(Amount.builder()
                                                                                         .currencyCode(t.getCurrencyCode())
                                                                                         .value(t.getAmount())
                                                                                         .build())
                                                                .build();

        if (StringUtils.hasText(t.getPayeeName())) {
            response.setPayee(TransactionDetails.builder()
                                                .name(t.getPayeeName())
                                                .build());
        }
        if (StringUtils.hasText(t.getPayerName())) {
            response.setPayer(TransactionDetails.builder()
                                                .name(t.getPayerName())
                                                .build());
        }
        return response;
    }
}
