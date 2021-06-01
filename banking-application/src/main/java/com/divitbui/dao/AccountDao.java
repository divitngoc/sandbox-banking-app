package com.divitbui.dao;

import java.util.Optional;
import java.util.UUID;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import com.divitbui.generated.tables.pojos.Account;

@Repository
public class AccountDao extends com.divitbui.generated.tables.daos.AccountDao {

    public AccountDao(final DSLContext dsl) {
        super(dsl.configuration());
    }

    public Optional<Account> fetchById(UUID id) {
        return Optional.ofNullable(super.fetchOneByUuid(id));
    }

}
