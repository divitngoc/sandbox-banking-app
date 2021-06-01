/*
 * This file is generated by jOOQ.
 */
package com.divitbui.generated.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer    id;
    private String     currencyCode;
    private BigDecimal balanceValue;
    private UUID       accountUuid;

    public Balance() {}

    public Balance(Balance value) {
        this.id = value.id;
        this.currencyCode = value.currencyCode;
        this.balanceValue = value.balanceValue;
        this.accountUuid = value.accountUuid;
    }

    public Balance(
        Integer    id,
        String     currencyCode,
        BigDecimal balanceValue,
        UUID       accountUuid
    ) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.balanceValue = balanceValue;
        this.accountUuid = accountUuid;
    }

    /**
     * Getter for <code>balance.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>balance.id</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>balance.currency_code</code>.
     */
    public String getCurrencyCode() {
        return this.currencyCode;
    }

    /**
     * Setter for <code>balance.currency_code</code>.
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * Getter for <code>balance.balance_value</code>.
     */
    public BigDecimal getBalanceValue() {
        return this.balanceValue;
    }

    /**
     * Setter for <code>balance.balance_value</code>.
     */
    public void setBalanceValue(BigDecimal balanceValue) {
        this.balanceValue = balanceValue;
    }

    /**
     * Getter for <code>balance.account_uuid</code>.
     */
    public UUID getAccountUuid() {
        return this.accountUuid;
    }

    /**
     * Setter for <code>balance.account_uuid</code>.
     */
    public void setAccountUuid(UUID accountUuid) {
        this.accountUuid = accountUuid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Balance (");

        sb.append(id);
        sb.append(", ").append(currencyCode);
        sb.append(", ").append(balanceValue);
        sb.append(", ").append(accountUuid);

        sb.append(")");
        return sb.toString();
    }
}
