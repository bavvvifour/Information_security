package sfu.informationsecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.repository.UserRepository;
import sfu.informationsecurity.service.ScoreService;

import java.security.Principal;
import java.util.Map;

@Controller
public class MainController {

    private final ScoreService scoreService;
    private final UserRepository userRepository;

    public MainController(ScoreService scoreService, UserRepository userRepository) {
        this.scoreService = scoreService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String showGamePage(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        int score = scoreService.getScore(user);
        model.addAttribute("score", score);
        return "index";
    }

    @PostMapping("/save")
    @ResponseBody
    public String saveScore(@RequestBody Map<String, Object> body, Principal principal) {
        Integer score = (Integer) body.get("score");
        Integer clickGain = (Integer) body.get("clickGain");
        Integer autoGain = (Integer) body.get("autoGain");
        Integer clickMultiplier = (Integer) body.get("clickMultiplier");
        Integer autoGainBonus = (Integer) body.get("autoGainBonus");
        Integer autoMultiplier = (Integer) body.get("autoMultiplier");
        Integer b1Level = (Integer) body.get("b1Level");
        Integer b2Level = (Integer) body.get("b2Level");
        Integer b3Level = (Integer) body.get("b3Level");

        if (score == null || clickGain == null || autoGain == null ||
                clickMultiplier == null || autoGainBonus == null || autoMultiplier == null ||
                b1Level == null || b2Level == null || b3Level == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            scoreService.updateScore(user, score);
            user.setClickGain(clickGain);
            user.setAutoGain(autoGain);
            user.setB1Level(b1Level);
            user.setB2Level(b2Level);
            user.setB3Level(b3Level);
            userRepository.save(user);
        }

        return "OK";
    }


    @GetMapping("/loadScore")
    @ResponseBody
    public Map<String, Object> loadScore(Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        int score = user != null ? scoreService.getScore(user) : 0;
        int clickGain = user != null ? user.getClickGain() : 1;
        int autoGain = user != null ? user.getAutoGain() : 1;
        int b1Level = user != null ? user.getB1Level() : 1;
        int b2Level = user != null ? user.getB2Level() : 1;
        int b3Level = user != null ? user.getB3Level() : 1;

        return Map.of(
                "score", score,
                "clickGain", clickGain,
                "autoGain", autoGain,
                "b1Level", b1Level,
                "b2Level", b2Level,
                "b3Level", b3Level
        );
    }
}
