/*
 * This file is generated by jOOQ.
 */
package com.divitbui.generated.tables.daos;


import com.divitbui.generated.tables.Balance;
import com.divitbui.generated.tables.records.BalanceRecord;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BalanceDao extends DAOImpl<BalanceRecord, com.divitbui.generated.tables.pojos.Balance, Integer> {

    /**
     * Create a new BalanceDao without any configuration
     */
    public BalanceDao() {
        super(Balance.BALANCE, com.divitbui.generated.tables.pojos.Balance.class);
    }

    /**
     * Create a new BalanceDao with an attached configuration
     */
    public BalanceDao(Configuration configuration) {
        super(Balance.BALANCE, com.divitbui.generated.tables.pojos.Balance.class, configuration);
    }

    @Override
    public Integer getId(com.divitbui.generated.tables.pojos.Balance object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchRangeOfId(Integer lowerInclusive, Integer upperInclusive) {
        return fetchRange(Balance.BALANCE.ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchById(Integer... values) {
        return fetch(Balance.BALANCE.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public com.divitbui.generated.tables.pojos.Balance fetchOneById(Integer value) {
        return fetchOne(Balance.BALANCE.ID, value);
    }

    /**
     * Fetch records that have <code>currency_code BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchRangeOfCurrencyCode(String lowerInclusive, String upperInclusive) {
        return fetchRange(Balance.BALANCE.CURRENCY_CODE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>currency_code IN (values)</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchByCurrencyCode(String... values) {
        return fetch(Balance.BALANCE.CURRENCY_CODE, values);
    }

    /**
     * Fetch records that have <code>balance_value BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchRangeOfBalanceValue(BigDecimal lowerInclusive, BigDecimal upperInclusive) {
        return fetchRange(Balance.BALANCE.BALANCE_VALUE, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>balance_value IN (values)</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchByBalanceValue(BigDecimal... values) {
        return fetch(Balance.BALANCE.BALANCE_VALUE, values);
    }

    /**
     * Fetch records that have <code>account_uuid BETWEEN lowerInclusive AND upperInclusive</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchRangeOfAccountUuid(UUID lowerInclusive, UUID upperInclusive) {
        return fetchRange(Balance.BALANCE.ACCOUNT_UUID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>account_uuid IN (values)</code>
     */
    public List<com.divitbui.generated.tables.pojos.Balance> fetchByAccountUuid(UUID... values) {
        return fetch(Balance.BALANCE.ACCOUNT_UUID, values);
    }
}
