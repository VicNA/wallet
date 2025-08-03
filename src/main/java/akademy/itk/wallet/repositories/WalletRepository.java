package akademy.itk.wallet.repositories;

import akademy.itk.wallet.entities.Wallet;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, UUID> {
}
