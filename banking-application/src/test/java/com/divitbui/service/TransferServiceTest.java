package com.divitbui.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.divitbui.dao.BankingRepository;
import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.exception.TransferFailedException;
import com.divitbui.model.request.TransferRequest;
import com.divitbui.validator.TransferValidator;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private BankingRepository bankingRepository;
    @Mock
    private TransferValidator transferValidator;
    @InjectMocks
    private TransferService transferService;

    @Test
    void testTransfer() throws AccountNotFoundException, TransferFailedException {
        transferService.transfer(UUID.randomUUID(), "userId", new TransferRequest());
        verify(transferValidator, times(1)).validate(any(TransferRequest.class));
        verify(bankingRepository, times(1)).transfer(any(), eq("userId"), any(TransferRequest.class));
    }

}
