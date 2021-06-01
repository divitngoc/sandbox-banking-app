package com.divitbui.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.divitbui.dao.AccountDao;
import com.divitbui.exception.AccountNotFoundException;
import com.divitbui.generated.tables.pojos.Account;

@ExtendWith(MockitoExtension.class)
class TransferValidatorTest {

    @Mock
    private AccountDao accountDao;
    @InjectMocks
    private TransferValidator validator;

    @Test
    void testValidateAccountId() throws AccountNotFoundException {
        UUID accountId = UUID.randomUUID();
        when(accountDao.fetchById(accountId)).thenReturn(Optional.of(new Account()));

        assertDoesNotThrow(() -> validator.validateTransferAccountId(accountId));
    }

    @Test
    void testValidateAccountId_AccountNotFound() throws AccountNotFoundException {
        UUID accountId = UUID.randomUUID();
        when(accountDao.fetchById(accountId)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> {
            validator.validateTransferAccountId(accountId);
        });
    }
}
