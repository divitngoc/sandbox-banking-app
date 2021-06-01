package com.divitbui.model.request;

import java.util.Optional;

import com.divitbui.generated.enums.AccountType;
import com.divitbui.model.Amount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequest {

    private AccountType type;
    @Builder.Default
    private Optional<Amount> amount = Optional.empty();

}
