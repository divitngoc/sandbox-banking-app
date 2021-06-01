package com.divitbui.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.divitbui.dao.AccountDao;
import com.divitbui.dao.BankingRepository;
import com.divitbui.generated.tables.pojos.Account;
import com.divitbui.generated.tables.pojos.Balance;
import com.divitbui.model.Amount;
import com.divitbui.model.request.AccountRequest;
import com.divitbui.model.response.AccountResponse;
import com.divitbui.model.response.AccountsResponse;
import com.divitbui.model.response.BalanceResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountDao accountDao;
    private final BankingRepository bankingRepository;

    public AccountResponse createAccount(final String userId, final String firstName, final String email, final AccountRequest request) {
        final Account account = mapToAccount(userId, firstName, email, request);
        final AccountResponse response = AccountResponse.builder()
                                                        .accountId(account.getUuid())
                                                        .build();
        request.getAmount()
               .ifPresentOrElse(amount -> {
                   final Balance balance = new Balance();
                   balance.setAccountUuid(account.getUuid());
                   balance.setBalanceValue(amount.getValue());
                   balance.setCurrencyCode(amount.getCurrencyCode());
                   response.setBalances(List.of(BalanceResponse.builder()
                                                               .amount(amount)
                                                               .build()));
                   bankingRepository.insertAccountAndBalance(account, balance);
               }, () -> accountDao.insert(account));
        return response;
    }

    private Account mapToAccount(final String userId, final String firstName, final String email, final AccountRequest request) {
        final Account account = new Account();
        account.setUuid(UUID.randomUUID());
        account.setUserId(userId);
        account.setName(firstName);
        account.setEmail(email);
        account.setType(request.getType());
        return account;
    }

    public AccountsResponse getAccountsByUserId(final String userId) {
        return AccountsResponse.builder()
                               .accounts(bankingRepository.fetchAccountsAndBalanceByUserId(userId)
                                                          .entrySet()
                                                          .stream()
                                                          .map(entry -> AccountResponse.builder()
                                                                                       .accountId(entry.getKey().getUuid())
                                                                                       .type(entry.getKey().getType())
                                                                                       .balances(entry.getValue()
                                                                                                      .stream()
                                                                                                      .map(this::mapToBalanceResponse)
                                                                                                      .collect(Collectors.toList()))
                                                                                       .build())
                                                          .collect(Collectors.toList()))
                               .build();
    }

    private BalanceResponse mapToBalanceResponse(final Balance b) {
        return BalanceResponse.builder()
                              .amount(Amount.builder()
                                            .currencyCode(b.getCurrencyCode())
                                            .value(b.getBalanceValue())
                                            .build())
                              .build();
    }

}
