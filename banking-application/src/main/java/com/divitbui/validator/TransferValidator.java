package com.divitbui.validator;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.divitbui.dao.AccountDao;
import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.exception.InvalidRequestException;
import com.divitbui.exception.UnavailableFundException;
import com.divitbui.generated.tables.records.BalanceRecord;
import com.divitbui.model.Amount;
import com.divitbui.model.request.TransferRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferValidator {

    private final AccountDao accountDao;

    public void validate(final TransferRequest transferRequest) throws AccountNotFoundException {
        validateTransferAccountId(transferRequest.getToAccountId());
        validateCurrencyCode(transferRequest.getAmount().getCurrencyCode());
    }

    public void validateCurrencyCode(final String currencyCode) {
        Currency.getInstance(currencyCode);
    }

    public void validateTransferAccountId(final UUID accountId) throws AccountNotFoundException {
        log.debug("Validating if valid accountId...");
        if (accountDao.fetchById(accountId).isEmpty())
            throw new AccountNotFoundException("Account ID: " + accountId + " cannot be found.");
        log.debug("Account Validation passed.");
    }

    public void validateFunds(final BalanceRecord balance, final Amount amount) throws UnavailableFundException {
        log.debug("Validating if enough balance to do request...");
        if (balance.getBalanceValue().compareTo(BigDecimal.ZERO) < 1) {
            throw new InvalidRequestException("amount.value needs to be greater than zero");
        }
        if (balance.getBalanceValue().compareTo(amount.getValue()) < 0) {
            throw new UnavailableFundException();
        }
        log.debug("Balance Validation passed.");
    }

}
