package com.divitbui.model.response;

import java.util.UUID;

import com.divitbui.model.Amount;
import com.divitbui.model.TransactionDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private UUID transactionId;
    private Amount transactionAmount;
    private TransactionDetails payee;
    private TransactionDetails payer;

}
