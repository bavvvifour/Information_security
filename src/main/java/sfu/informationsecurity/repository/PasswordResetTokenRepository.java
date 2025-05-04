package sfu.informationsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfu.informationsecurity.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
