package com.divitbui.model.response;

import java.util.List;
import java.util.UUID;

import com.divitbui.generated.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {

    private UUID accountId;
    private AccountType type;
    private List<BalanceResponse> balances;

}
