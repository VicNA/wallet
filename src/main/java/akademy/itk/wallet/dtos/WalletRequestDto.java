package akademy.itk.wallet.dtos;

import akademy.itk.wallet.dtos.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequestDto {
    private UUID walletId;
    private OperationType operationType;
    private BigDecimal amount;
}
