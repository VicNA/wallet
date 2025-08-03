package akademy.itk.wallet.services;

import akademy.itk.wallet.dtos.WalletRequestDto;
import akademy.itk.wallet.entities.Wallet;
import akademy.itk.wallet.exceptions.WalletNotEnoughFundsException;
import akademy.itk.wallet.exceptions.WalletNotFoundException;
import akademy.itk.wallet.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    public Wallet getWallet(UUID uuid) {
        return validateWallet(uuid);
    }

    @Transactional
    public Wallet updateWalletBalance(WalletRequestDto walletDto) {
        switch (walletDto.getOperationType()) {
            case DEPOSIT -> {
                return deposit(walletDto);
            }
            case WITHDRAW -> {
                return withdraw(walletDto);
            }
            default -> throw new RuntimeException("Неизвестная операция");
        }
    }

    private Wallet validateWallet(UUID walletId) {
        return walletRepository.findById(walletId).orElseThrow(
                () -> new WalletNotFoundException("Кошелек не найден")
        );
    }

    private Wallet withdraw(WalletRequestDto walletDto) {
        Wallet wallet = validateWallet(walletDto.getWalletId());
        BigDecimal balance = new BigDecimal(wallet.getBalance()).subtract(walletDto.getAmount());

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletNotEnoughFundsException("Недостаточно средств");
        }

        wallet.setBalance(balance.toString());
        walletRepository.save(wallet);

        return wallet;
    }

    private Wallet deposit(WalletRequestDto walletDto) {
        Wallet wallet = validateWallet(walletDto.getWalletId());
        BigDecimal balance = new BigDecimal(wallet.getBalance()).add(walletDto.getAmount());
        wallet.setBalance(balance.toString());
        walletRepository.save(wallet);

        return wallet;
    }
}
