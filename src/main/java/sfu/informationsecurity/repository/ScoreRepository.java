package sfu.informationsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sfu.informationsecurity.model.Score;
import sfu.informationsecurity.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUser(User user);

    @Query("SELECT s FROM Score s " +
            "WHERE s.createdAt IN (" +
            "  SELECT MAX(s2.createdAt) FROM Score s2 GROUP BY s2.user" +
            ") " +
            "ORDER BY s.score DESC")
    List<Score> findTopScores();
}
