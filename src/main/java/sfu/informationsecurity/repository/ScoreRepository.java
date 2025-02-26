package sfu.informationsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sfu.informationsecurity.model.Score;
import sfu.informationsecurity.model.User;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByUser(User user);
}
