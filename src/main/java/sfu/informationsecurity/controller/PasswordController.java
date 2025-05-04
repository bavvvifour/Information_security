package sfu.informationsecurity.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sfu.informationsecurity.service.PasswordResetService;

@Controller
public class PasswordController {
    private final PasswordResetService resetService;

    public PasswordController(PasswordResetService resetService) {
        this.resetService = resetService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @RequestParam("email") String email,
            Model model) {
        try {
            resetService.createPasswordResetToken(email);
            model.addAttribute("email", email);
            return "forgot-password-confirmation";
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Пользователь с таким email не найден");
            return "forgot-password";
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось отправить письмо. Попробуйте позже.");
            return "forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam("token") String token,
            Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam("token") String token,
            @RequestParam("password") String password,
            Model model) {
        if (resetService.resetPassword(token, password)) {
            model.addAttribute("message", "Пароль успешно изменён");
            return "login";
        } else {
            model.addAttribute("error", "Ссылка недействительна или истёк срок");
            return "reset-password";
        }
    }
}
