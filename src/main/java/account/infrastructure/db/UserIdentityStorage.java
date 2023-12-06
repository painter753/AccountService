package account.infrastructure.db;

import account.domain.UserIdentity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserIdentityStorage extends CrudRepository<UserIdentity, String> {
    Optional<UserIdentity> findUserIdentityByEmail(String email);

    boolean existsUserIdentityByEmail(String email);
}
