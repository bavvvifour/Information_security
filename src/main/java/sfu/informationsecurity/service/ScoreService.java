package sfu.informationsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sfu.informationsecurity.model.Score;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.repository.ScoreRepository;

@Service
public class ScoreService {
    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public int getScore(User user) {
        if (user == null) {
            return 0;
        }
        return scoreRepository.findByUser(user).map(Score::getScore).orElse(0);
    }

    public void updateScore(User user, int newScore) {
        if (user == null) {
            return;
        }
        Score score = scoreRepository.findByUser(user).orElseGet(() -> {
            Score newScoreEntry = new Score();
            newScoreEntry.setUser(user);
            return newScoreEntry;
        });

        score.setScore(newScore);
        scoreRepository.save(score);
    }

    @Scheduled(fixedRate = 10000)
    public void autoUpdateScore() {
        scoreRepository.findAll().forEach(score -> {
            int newScore = score.getScore() + 10;
            score.setScore(newScore);
            scoreRepository.save(score);
        });
    }
}
