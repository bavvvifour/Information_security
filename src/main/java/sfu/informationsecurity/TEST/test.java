//package sfu.informationsecurity.TEST;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import sfu.informationsecurity.service.JwtService;
//
//@Component
//public class test implements CommandLineRunner {
//
//    @Override
//    public void run(String... args) throws Exception {
//        JwtService jwtService = new JwtService();
//
//        // Генерация токена
//        String token = jwtService.generateToken("user123");
//        System.out.println("🔑 JWT Token: " + token);
//
//        // Валидация токена
//        boolean isValid = jwtService.validateToken(token, "user123");
//        System.out.println("✅ Токен валиден: " + isValid);
//
//        // Извлечение имени пользователя
//        String username = jwtService.extractUsername(token);
//        System.out.println("👤 Пользователь: " + username);
//    }
//}
