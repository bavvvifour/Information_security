package sfu.informationsecurity.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import sfu.informationsecurity.model.User;
import sfu.informationsecurity.model.VerificationToken;
import sfu.informationsecurity.repository.UserRepository;
import sfu.informationsecurity.repository.VerificationTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    public VerificationService(VerificationTokenRepository tokenRepository, JavaMailSender mailSender, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.userRepository = userRepository;
    }

    public void sendVerificationEmail(User user) {
        String token = UUID.randomUUID().toString();
        tokenRepository.save(new VerificationToken(token, user));

        String link = "http://localhost:8080/confirm?token=" + token;
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("bayanovv8091@mail.ru");
        mail.setSubject("Подтверждение регистрации");
        mail.setText("Перейдите по ссылке для подтверждения регистрации: " + link);
        mailSender.send(mail);
    }

    public boolean confirmToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return true;
    }
}
