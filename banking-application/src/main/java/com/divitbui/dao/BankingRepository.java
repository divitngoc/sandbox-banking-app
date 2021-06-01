package com.divitbui.dao;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.divitbui.exception.BalanceNotFoundException;
import com.divitbui.exception.TransferFailedException;
import com.divitbui.generated.Tables;
import com.divitbui.generated.tables.pojos.Account;
import com.divitbui.generated.tables.pojos.Balance;
import com.divitbui.generated.tables.records.AccountRecord;
import com.divitbui.generated.tables.records.BalanceRecord;
import com.divitbui.generated.tables.records.TransactionRecord;
import com.divitbui.model.request.TransferRequest;
import com.divitbui.validator.TransferValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BankingRepository {

    private final DSLContext dsl;
    private final TransferValidator transferValidator;
    private final AccountDao accountDao;

    public void insertAccountAndBalance(final Account account, final Balance balance) {
        final AccountRecord accRecord = dsl.newRecord(Tables.ACCOUNT);
        accRecord.from(account);
        final BalanceRecord balRecord = dsl.newRecord(Tables.BALANCE);
        balRecord.from(balance);
        dsl.batchInsert(accRecord, balRecord)
           .execute();
    }

    public Map<Account, List<Balance>> fetchAccountsAndBalanceByUserId(final String userId) {
        return dsl.select()
                  .from(Tables.ACCOUNT)
                  .join(Tables.BALANCE)
                  .on(Tables.BALANCE.ACCOUNT_UUID.eq(Tables.ACCOUNT.UUID))
                  .where(Tables.ACCOUNT.USER_ID.eq(userId))
                  .fetchGroups(Account.class, Balance.class);
    }

    public Optional<BalanceRecord> fetchBalanceByAccountIdCurrencyCode(final DSLContext ctx, final UUID accountId, final String currencyCode) {
        return ctx.select()
                  .from(Tables.BALANCE)
                  .join(Tables.ACCOUNT)
                  .on(Tables.ACCOUNT.UUID.eq(Tables.BALANCE.ACCOUNT_UUID))
                  .and(Tables.BALANCE.ACCOUNT_UUID.eq(accountId))
                  .where(Tables.BALANCE.CURRENCY_CODE.eq(currencyCode))
                  .forUpdate()
                  .fetchOptionalInto(BalanceRecord.class);
    }

    public Optional<BalanceRecord> fetchBalanceByAccountIdCurrencyCodeAndUserId(final DSLContext ctx, final UUID accountId,
                                                                                final String currencyCode, final String userId) {
        return ctx.select()
                  .from(Tables.BALANCE)
                  .join(Tables.ACCOUNT)
                  .on(Tables.ACCOUNT.UUID.eq(Tables.BALANCE.ACCOUNT_UUID))
                  .and(Tables.BALANCE.ACCOUNT_UUID.eq(accountId))
                  .and(Tables.ACCOUNT.USER_ID.eq(userId))
                  .where(Tables.BALANCE.CURRENCY_CODE.eq(currencyCode))
                  .forUpdate()
                  .fetchOptionalInto(BalanceRecord.class);
    }

    public void transfer(final UUID fromAccountId, final String userId, final TransferRequest transferRequest) throws TransferFailedException {
        try {
            log.debug("Begin transaction for transfers...");
            dsl.transaction(c -> {
                final DSLContext ctx = c.dsl();
                final BalanceRecord balance = fetchBalanceByAccountIdCurrencyCodeAndUserId(ctx, fromAccountId,
                    transferRequest.getAmount().getCurrencyCode(), userId).orElseThrow(() -> new BalanceNotFoundException("Unable to find balance that corresponds with the transfer currency code."));
                transferValidator.validateFunds(balance, transferRequest.getAmount());
                upsertBalances(ctx, transferRequest, balance);
                insertTransactions(ctx, fromAccountId, transferRequest);
                log.debug("Transfer successfully completed.");
                log.debug("Transaction finished for transfers.");
                // Implicit commit executed here
            });
        } catch (RuntimeException e) {
            log.debug("Transfer error occurred, rolling back transaction...", e.getMessage(), e);
            throw new TransferFailedException(e);
        }
    }

    private void upsertBalances(final DSLContext ctx, final TransferRequest transferRequest, final BalanceRecord fromBalance) {
        log.debug("Upserting balances...");
        final BalanceRecord balanceToTransfer = fetchBalanceByAccountIdCurrencyCode(ctx, transferRequest.getToAccountId(),
                                                                                    transferRequest.getAmount().getCurrencyCode()).orElseGet(() -> {
                                                                                        log.debug("Unable to find fromBalance with currencyCode: [{}]. Creating Balance for accountId [{}] with currencyCode [{}]",
                                                                                                  transferRequest.getAmount().getCurrencyCode(),
                                                                                                  transferRequest.getToAccountId(),
                                                                                                  transferRequest.getAmount().getCurrencyCode());
                                                                                        return createEmptyBalanceRecord(transferRequest);
                                                                                    });
        final BigDecimal amountToTransfer = transferRequest.getAmount().getValue();
        fromBalance.setBalanceValue(fromBalance.getBalanceValue().subtract(amountToTransfer));
        balanceToTransfer.setBalanceValue(balanceToTransfer.getBalanceValue().add(amountToTransfer));
        fromBalance.store();
        balanceToTransfer.store();
        log.debug("Successfully Upserted balances: [fromBalance: {}, toBalance: {}]", fromBalance.getId(), balanceToTransfer.getId());
    }

    private void insertTransactions(final DSLContext ctx, final UUID fromAccountId, final TransferRequest transferRequest) {
        log.debug("Inserting transactions record...");
        final TransactionRecord fromTransaction = ctx.newRecord(Tables.TRANSACTION);
        setTransactionValues(transferRequest, fromTransaction);
        fromTransaction.setAmount(fromTransaction.getAmount().negate());
        fromTransaction.setPayeeName(transferRequest.getName());
        fromTransaction.setAccountUuid(fromAccountId);

        final TransactionRecord toTransaction = ctx.newRecord(Tables.TRANSACTION);
        setTransactionValues(transferRequest, toTransaction);
        toTransaction.setPayerName(accountDao.fetchById(fromAccountId).get().getName());
        toTransaction.setAccountUuid(transferRequest.getToAccountId());
        ctx.batchInsert(fromTransaction,
                        toTransaction)
           .execute();
        log.debug("Successfully inserted transactions record. fromTransaction: {}, toTransaction: {}", fromTransaction.getUuid(), toTransaction.getUuid());
    }

    private void setTransactionValues(final TransferRequest transferRequest, final TransactionRecord transaction) {
        transaction.setUuid(UUID.randomUUID());
        transaction.setAmount(transferRequest.getAmount().getValue());
        transaction.setCurrencyCode(transferRequest.getAmount().getCurrencyCode());
        transaction.setDate(LocalDateTime.now(Clock.systemUTC()));
        transaction.setDescription(transferRequest.getDescription());
    }

    private BalanceRecord createEmptyBalanceRecord(final TransferRequest transferRequest) {
        final BalanceRecord record = new BalanceRecord();
        record.setAccountUuid(transferRequest.getToAccountId());
        record.setBalanceValue(BigDecimal.ZERO);
        record.setCurrencyCode(transferRequest.getAmount().getCurrencyCode());
        return record;
    }
}