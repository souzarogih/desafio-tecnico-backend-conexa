package com.desafio.conexa.saude.conexa_backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoffServiceTest {

    @Mock
    private RevokedTokensService revokedTokensService;

    @InjectMocks
    private LogoffService logoffService;

    @Test
    @DisplayName("Should revoke token successfully and return true on logoff")
    void logoff_shouldRevokeTokenSuccessfully_andReturnTrue() {

        String token = "fake-jwt-token";
        boolean result = logoffService.logoff(token);
        assertTrue(result);
        verify(revokedTokensService, times(1)).revokeToken(token);
    }

    @Test
    @DisplayName("Should throw exception when RevokedTokensService fails on logoff")
    void logoff_shouldThrowException_whenRevokedTokensServiceFails() {

        String token = "invalid-token";
        doThrow(new RuntimeException("Falha ao revogar token")).when(revokedTokensService).revokeToken(token);
        assertThrows(RuntimeException.class, () -> logoffService.logoff(token));
        verify(revokedTokensService, times(1)).revokeToken(token);
    }
}