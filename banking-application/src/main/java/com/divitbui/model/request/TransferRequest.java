package com.divitbui.model.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.divitbui.model.Amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    @NotNull
    private UUID toAccountId;
    @NotNull
    private Amount amount;
    @NotNull
    private String name;
    private String description;

}
