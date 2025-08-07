package akademy.itk.wallet.services;

import akademy.itk.wallet.dtos.WalletRequestDto;
import akademy.itk.wallet.dtos.enums.OperationType;
import akademy.itk.wallet.entities.Wallet;
import akademy.itk.wallet.exceptions.WalletNotEnoughFundsException;
import akademy.itk.wallet.exceptions.WalletNotFoundException;
import akademy.itk.wallet.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;

    @Transactional(readOnly = true)
    public Wallet getWallet(UUID uuid) {
        return validateWallet(
                () -> walletRepository.findById(uuid));
    }

    @Transactional
    public Wallet updateWalletBalance(WalletRequestDto walletDto) {
        Wallet wallet = validateWallet(
                () -> walletRepository.findByIdWithLock(walletDto.getWalletId()));
        BigDecimal balance = new BigDecimal(wallet.getBalance());

        balance = calculateBalance(balance, walletDto.getAmount(), walletDto.getOperationType());

        wallet.setBalance(balance.toString());
        walletRepository.save(wallet);

        return wallet;
    }

    private BigDecimal calculateBalance(BigDecimal balance, BigDecimal amount, OperationType operationType) {
        switch (operationType) {
            case DEPOSIT -> {
                return deposit(balance, amount);
            }
            case WITHDRAW ->  {
                return withdraw(balance, amount);
            }
            default -> throw new RuntimeException("Неизвестная операция");
        }
    }

    private BigDecimal withdraw(BigDecimal balance, BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new WalletNotEnoughFundsException("Недостаточно средств");
        }

        return balance.subtract(amount);
    }

    private BigDecimal deposit(BigDecimal balance, BigDecimal amount) {
        return balance.add(amount);
    }

    private Wallet validateWallet(Supplier<Optional<Wallet>> supplier) {
        return supplier.get().orElseThrow(
                () -> new WalletNotFoundException("Кошелек не найден")
        );
    }
}
