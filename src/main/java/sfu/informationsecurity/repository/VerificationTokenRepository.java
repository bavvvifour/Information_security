package sfu.informationsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfu.informationsecurity.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

}
