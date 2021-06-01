package com.divitbui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.divitbui.dao.TransactionDao;
import com.divitbui.generated.tables.pojos.Transaction;
import com.divitbui.model.Amount;
import com.divitbui.model.TransactionDetails;
import com.divitbui.model.response.TransactionResponse;
import com.divitbui.model.response.TransactionsResponse;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionDao transactionDao;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void testGetTransactionsByAccountIdAndUserId() {
        UUID transactionId = UUID.randomUUID();
        when(transactionDao.fetchTransactionsByAccountIdAndUserId(any(UUID.class), eq("userId"))).thenReturn(List.of(createTransaction(transactionId)));
        TransactionsResponse result = transactionService.getTransactionsByAccountIdAndUserId(UUID.randomUUID(), "userId");

        assertThat(result.getTransactions()).hasSize(1)
                                            .extracting(TransactionResponse::getTransactionId,
                                                        TransactionResponse::getTransactionAmount,
                                                        TransactionResponse::getPayee)
                                            .contains(tuple(transactionId,
                                                            Amount.builder().currencyCode("GBP").value(BigDecimal.TEN).build(),
                                                            TransactionDetails.builder().name("payeeName").build()));
    }

    @Test
    void testGetTransactionsByUserId() {
        UUID transactionId = UUID.randomUUID();
        when(transactionDao.fetchAllTransactionsByUserId("userId")).thenReturn(List.of(createTransaction(transactionId)));
        TransactionsResponse result = transactionService.getTransactionsByUserId("userId");

        assertThat(result.getTransactions()).hasSize(1)
                                            .extracting(TransactionResponse::getTransactionId,
                                                        TransactionResponse::getTransactionAmount,
                                                        TransactionResponse::getPayee)
                                            .contains(tuple(transactionId,
                                                            Amount.builder().currencyCode("GBP").value(BigDecimal.TEN).build(),
                                                            TransactionDetails.builder().name("payeeName").build()));
    }

    private Transaction createTransaction(UUID transactionId) {
        Transaction transaction = new Transaction();
        transaction.setUuid(transactionId);
        transaction.setAmount(BigDecimal.TEN);
        transaction.setCurrencyCode("GBP");
        transaction.setDate(LocalDateTime.now());
        transaction.setPayeeName("payeeName");
        return transaction;
    }

}
