package akademy.itk.wallet.exceptions;

public class WalletNotEnoughFundsException extends RuntimeException {
    public WalletNotEnoughFundsException(String message) {
        super(message);
    }
}
