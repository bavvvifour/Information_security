package sfu.informationsecurity.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sfu.informationsecurity.dto.LoginRequest;
import sfu.informationsecurity.dto.RegisterRequest;
import sfu.informationsecurity.model.Role;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.repository.UserRepository;
import sfu.informationsecurity.service.JwtService;
import sfu.informationsecurity.service.VerificationService;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationService verificationService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute RegisterRequest request, Model model) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            model.addAttribute("error", "Имя пользователя уже занято");
            return "registration";
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        userRepository.save(user);

        verificationService.sendVerificationEmail(user);

        model.addAttribute("email", request.getEmail());
        return "verification-info";
    }


    @GetMapping("/registration")
    public String registrationPage() {
        return "registration";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, Model model, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(request.getUsername());

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token, Model model) {
        if (verificationService.confirmToken(token)) {
            model.addAttribute("message", "Аккаунт успешно подтверждён!");
        } else {
            model.addAttribute("error", "Ссылка недействительна или устарела");
        }
        return "login";
    }

}
