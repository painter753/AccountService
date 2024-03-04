package account.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserIdentityStorage extends CrudRepository<UserIdentityDto, String> {
    Optional<UserIdentityDto> findUserIdentityByEmailIgnoreCase(String email);
    boolean existsUserIdentityByEmailIgnoreCase(String email);
}
