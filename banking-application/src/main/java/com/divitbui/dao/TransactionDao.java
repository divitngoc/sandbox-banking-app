package com.divitbui.dao;

import java.util.List;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.divitbui.generated.Tables;
import com.divitbui.generated.tables.pojos.Transaction;

@Repository
public class TransactionDao extends com.divitbui.generated.tables.daos.TransactionDao {

    private final DSLContext dsl;

    public TransactionDao(final DSLContext dsl) {
        super(dsl.configuration());
        this.dsl = dsl;
    }

    public List<Transaction> fetchAllTransactionsByUserId(final String userId) {
        return dsl.select()
                  .from(Tables.TRANSACTION)
                  .join(Tables.ACCOUNT)
                  .on(Tables.ACCOUNT.UUID.eq(Tables.TRANSACTION.ACCOUNT_UUID))
                  .and(Tables.ACCOUNT.USER_ID.eq(userId))
                  .fetchInto(Transaction.class);
    }

    public List<Transaction> fetchTransactionsByAccountIdAndUserId(final UUID accountId, final String userId) {
        return dsl.select()
                  .from(Tables.TRANSACTION)
                  .join(Tables.ACCOUNT)
                  .on(Tables.ACCOUNT.UUID.eq(Tables.TRANSACTION.ACCOUNT_UUID))
                  .and(Tables.ACCOUNT.UUID.eq(accountId))
                  .and(Tables.ACCOUNT.USER_ID.eq(userId))
                  .fetchInto(Transaction.class);
    }

}
