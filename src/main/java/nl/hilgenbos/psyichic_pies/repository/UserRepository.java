package nl.hilgenbos.psyichic_pies.repository;

import nl.hilgenbos.psyichic_pies.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);

    Optional<UserEntity> findOptionalByUsername(String username);

    boolean existsByUsername(String username);
}
