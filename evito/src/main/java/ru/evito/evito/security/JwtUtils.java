package ru.evito.evito.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.evito.evito.dto.user.UserDto;
import ru.evito.evito.models.User;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${jwt.token.secret}")
    private String secret;

    public String generate(UserDto user) {
        var experationDate = Date.from(ZonedDateTime.now()
                .plusDays(2)
                .toInstant());
        return JWT.create()
                .withSubject("User details")
                .withClaim("username", user.getUsername())
                .withClaim("role", user.getRole())
                .withIssuedAt(new Date())
                .withExpiresAt(experationDate)
                .withIssuer("spring-app-evito")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateAndVerifyToken(String token) {
        var verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User details")
                .withIssuer("spring-app-evito")
                .build();
        var jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
