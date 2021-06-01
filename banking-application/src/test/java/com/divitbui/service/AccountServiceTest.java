package com.divitbui.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.divitbui.dao.AccountDao;
import com.divitbui.dao.BankingRepository;
import com.divitbui.generated.enums.AccountType;
import com.divitbui.generated.tables.pojos.Account;
import com.divitbui.generated.tables.pojos.Balance;
import com.divitbui.model.Amount;
import com.divitbui.model.request.AccountRequest;
import com.divitbui.model.response.AccountResponse;
import com.divitbui.model.response.AccountsResponse;
import com.divitbui.model.response.BalanceResponse;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountDao accountDao;
    @Mock
    private BankingRepository bankingRepository;
    @InjectMocks
    private AccountService accountService;

    @Test
    void testCreateAccount_justAccount() {
        AccountResponse result = accountService.createAccount("123456", "user", "user1@gmail.com", accountRequestBuilder()
                                                                                                                          .amount(Optional.empty())
                                                                                                                          .build());
        assertNotNull(result.getAccountId());
        verify(accountDao, times(1)).insert(any(Account.class));
        verifyNoInteractions(bankingRepository);
    }

    @Test
    void testCreateAccount_AccountAndBalance() {
        AccountResponse result = accountService.createAccount("123456", "user", "user1@gmail.com", accountRequestBuilder().build());
        assertNotNull(result.getAccountId());
        verify(bankingRepository, times(1)).insertAccountAndBalance(any(Account.class), any(Balance.class));
        verifyNoInteractions(accountDao);
    }

    @Test
    void testGetAccountsByUserId() {
        UUID accountId = UUID.randomUUID();
        when(bankingRepository.fetchAccountsAndBalanceByUserId("userId")).thenReturn(Map.ofEntries(entry(createAccount(accountId, AccountType.PERSONAL),
                                                                                                         List.of(createBalance(accountId)))));

        AccountsResponse result = accountService.getAccountsByUserId("userId");
        assertThat(result.getAccounts()).hasSize(1);
        AccountResponse accResponse = result.getAccounts().get(0);
        assertEquals(accountId, accResponse.getAccountId());
        BalanceResponse balResponse = accResponse.getBalances().get(0);
        assertEquals(BigDecimal.TEN, balResponse.getAmount().getValue());
        assertEquals("GBP", balResponse.getAmount().getCurrencyCode());
    }

    private Account createAccount(UUID accountId, AccountType type) {
        final Account acc = new Account();
        acc.setUuid(accountId);
        acc.setCreated(LocalDateTime.now());
        acc.setEmail("user1@gmail.com");
        acc.setName("user1");
        acc.setType(type);
        return acc;
    }

    private Balance createBalance(UUID accountId) {
        final Balance bal = new Balance();
        bal.setId(1);
        bal.setAccountUuid(accountId);
        bal.setBalanceValue(BigDecimal.TEN);
        bal.setCurrencyCode("GBP");
        return bal;
    }

    private AccountRequest.AccountRequestBuilder accountRequestBuilder() {
        return AccountRequest.builder()
                             .amount(Optional.of(Amount.builder().currencyCode("GBP").value(BigDecimal.TEN).build()))
                             .type(AccountType.PERSONAL);
    }

}
