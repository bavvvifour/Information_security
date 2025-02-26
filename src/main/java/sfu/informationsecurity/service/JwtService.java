package sfu.informationsecurity.service;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;
import sfu.informationsecurity.util.RSAKeyUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Service
public class JwtService {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final JwtParser jwtParser;

    public JwtService() throws Exception {
        this.privateKey = RSAKeyUtil.getPrivateKey();
        this.publicKey = RSAKeyUtil.getPublicKey();
        this.jwtParser = Jwts.parser()
                .verifyWith(publicKey)
                .build();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(privateKey)
                .compact();
    }

    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return jwtParser.parseClaimsJws(token).getPayload().getSubject();
    }

    public Date extractExpiration(String token) {
        return jwtParser.parseClaimsJws(token).getPayload().getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
