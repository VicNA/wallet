package akademy.itk.wallet.controllers;

import akademy.itk.wallet.convertes.WalletConverter;
import akademy.itk.wallet.dtos.WalletRequestDto;
import akademy.itk.wallet.dtos.WalletResponseDto;
import akademy.itk.wallet.dtos.enums.OperationType;
import akademy.itk.wallet.entities.Wallet;
import akademy.itk.wallet.services.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static akademy.itk.wallet.dtos.enums.OperationType.DEPOSIT;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {
    @Mock
    private WalletService walletService;
    @Mock
    private WalletConverter walletConverter;
    @InjectMocks
    private WalletController walletController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    void getWalletBalanceTest() {
        UUID walletId = UUID.randomUUID();
        String balance = "1000";
        Wallet wallet = new Wallet(walletId, balance);
        WalletResponseDto responseWalletDTO = new WalletResponseDto(walletId, new BigDecimal(balance));

        when(walletService.getWallet(walletId)).thenReturn(wallet);
        when(walletConverter.entityToDto(wallet)).thenReturn(responseWalletDTO);
        ResponseEntity<WalletResponseDto> response = walletController.getWallet(walletId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseWalletDTO, response.getBody());
    }

    @Test
    void changeWalletBalanceTest() {
        UUID walletId = UUID.randomUUID();
        String balance = "7500";
        Wallet wallet = new Wallet(walletId, balance);

        WalletRequestDto requestDto = new WalletRequestDto(
                walletId, OperationType.DEPOSIT, BigDecimal.valueOf(2500));
        WalletResponseDto responseDto = new WalletResponseDto(walletId, new BigDecimal(balance));

        when(walletService.updateWalletBalance(requestDto)).thenReturn(wallet);
        when(walletConverter.entityToDto(wallet)).thenReturn(responseDto);
        ResponseEntity<WalletResponseDto> response = walletController.changeWalletBalance(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
    }

    @Test
    void getBadRequestTest() throws Exception {
        // given
        String requestJson = "it's JSON";
        // when
        mockMvc.perform(post("/api/v1/wallet").contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                // then
                .andExpect(result -> assertInstanceOf(HttpMessageNotReadableException.class, result.getResolvedException()))
                .andExpect(status().isBadRequest());
    }
}
