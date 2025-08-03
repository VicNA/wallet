package akademy.itk.wallet.convertes;

import akademy.itk.wallet.dtos.WalletResponseDto;
import akademy.itk.wallet.entities.Wallet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class WalletConverter {

    public WalletResponseDto entityToDto(Wallet wallet) {
        return new WalletResponseDto(wallet.getWalletId(), new BigDecimal(wallet.getBalance()));
    }
}
