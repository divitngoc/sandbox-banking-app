package com.divitbui.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.divitbui.dao.BankingRepository;
import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.exception.TransferFailedException;
import com.divitbui.model.request.TransferRequest;
import com.divitbui.validator.TransferValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final BankingRepository bankingRepository;
    private final TransferValidator transferValidator;

    public void transfer(final UUID currentAccountId, final String userId, final TransferRequest transferRequest) throws AccountNotFoundException,
                                                                                                                  TransferFailedException {
        transferValidator.validate(transferRequest);
        bankingRepository.transfer(currentAccountId, userId, transferRequest);
    }

}
