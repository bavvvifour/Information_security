package sfu.informationsecurity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.repository.UserRepository;
import sfu.informationsecurity.service.ScoreService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

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

        if (score == null || clickGain == null || autoGain == null ||
                clickMultiplier == null || autoGainBonus == null || autoMultiplier == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing parameters");
        }

        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            scoreService.updateScore(user, score);
            user.setClickGain(clickGain);
            user.setAutoGain(autoGain);
            user.setClickMultiplier(clickMultiplier);
            user.setAutoGainBonus(autoGainBonus);
            user.setAutoMultiplier(autoMultiplier);
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
        int clickMultiplier = user != null ? user.getClickMultiplier() : 1;
        int autoGainBonus = user != null ? user.getAutoGainBonus() : 0;
        int autoMultiplier = user != null ? user.getAutoMultiplier() : 1;

        return Map.of(
                "score", score,
                "clickGain", clickGain,
                "autoGain", autoGain,
                "clickMultiplier", clickMultiplier,
                "autoGainBonus", autoGainBonus,
                "autoMultiplier", autoMultiplier
        );
    }




}
