package sfu.informationsecurity.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfu.informationsecurity.model.PasswordResetToken;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.repository.PasswordResetTokenRepository;
import sfu.informationsecurity.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {
    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepo;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            PasswordResetTokenRepository tokenRepo,
            UserRepository userRepo,
            JavaMailSender mailSender,
            PasswordEncoder passwordEncoder) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPasswordResetToken(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        tokenRepo.save(new PasswordResetToken(token, user, 60));

        String link = "http://localhost:8080/reset-password?token=" + token;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("bayanovv8091@mail.ru");
        mail.setSubject("Восстановление пароля");
        mail.setText("Перейдите по ссылке, чтобы сбросить пароль:\n" + link);
        try {
            mailSender.send(mail);
        } catch (MailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public boolean resetPassword(String token, String newPassword) {
        return tokenRepo.findByToken(token)
            .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
            .map(t -> {
                User u = t.getUser();
                u.setPassword(passwordEncoder.encode(newPassword));
                userRepo.save(u);
                tokenRepo.delete(t);
                return true;
            }).orElse(false);
    }
}
