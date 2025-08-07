package akademy.itk.wallet.services;

import akademy.itk.wallet.dtos.WalletRequestDto;
import akademy.itk.wallet.dtos.enums.OperationType;
import akademy.itk.wallet.entities.Wallet;
import akademy.itk.wallet.exceptions.WalletNotEnoughFundsException;
import akademy.itk.wallet.exceptions.WalletNotFoundException;
import akademy.itk.wallet.repositories.WalletRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository repository;

    @InjectMocks
    private WalletService service;

    @Test
    void getWalletTest() {
        UUID walletId = UUID.randomUUID();
        String balance = "1000";
        Wallet wallet = new Wallet(walletId, balance);

        when(repository.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = service.getWallet(walletId);

        assertEquals(wallet, result);
        verify(repository).findById(walletId);
    }

    @Test
    void updateWalletBalanceWithdraw() {
        UUID walletId = UUID.randomUUID();
        String balance = "5000";
        Wallet wallet = new Wallet(walletId, balance);

        Wallet updateWallet = new Wallet(walletId, "2500");
        WalletRequestDto requestDto = new WalletRequestDto(
                walletId, OperationType.WITHDRAW, BigDecimal.valueOf(2500));

        when(repository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = service.updateWalletBalance(requestDto);

        assertEquals(updateWallet, result);
        verify(repository).findByIdWithLock(walletId);
    }

    @Test
    void updateWalletBalanceDeposit() {
        UUID walletId = UUID.randomUUID();
        String balance = "5000";
        Wallet wallet = new Wallet(walletId, balance);

        Wallet updateWallet = new Wallet(walletId, "7500");
        WalletRequestDto requestDto = new WalletRequestDto(
                walletId, OperationType.DEPOSIT, BigDecimal.valueOf(2500));

        when(repository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = service.updateWalletBalance(requestDto);

        assertEquals(updateWallet, result);
        verify(repository).findByIdWithLock(walletId);
    }

    @Test
    void getWallet_NotFoundTest() {
        UUID walletId = UUID.randomUUID();

        WalletNotFoundException exception = assertThrows(
                WalletNotFoundException.class, () -> service.getWallet(walletId));

        assertEquals("Кошелек не найден", exception.getMessage());
        verify(repository).findById(walletId);
    }

    @Test
    void updateWallet_WalletNotEnoughFundsTest() {
        UUID walletId = UUID.randomUUID();
        String balance = "1000";
        Wallet wallet = new Wallet(walletId, balance);

        when(repository.findByIdWithLock(walletId)).thenReturn(Optional.of(wallet));

        WalletRequestDto requestDto = new WalletRequestDto(
                walletId, OperationType.WITHDRAW, BigDecimal.valueOf(5000));

        WalletNotEnoughFundsException exception = assertThrows(
                WalletNotEnoughFundsException.class, () -> service.updateWalletBalance(requestDto));

        assertEquals("Недостаточно средств", exception.getMessage());
        verify(repository).findByIdWithLock(walletId);
    }
}
