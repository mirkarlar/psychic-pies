package nl.hilgenbos.psyichic_pies.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import nl.hilgenbos.psyichic_pies.service.PieshopUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {


    @Value("${jwt.token-expiration-time}")
    private long secondsFromNow = 3600000;
    private final PieshopUserDetailsService pieshopUserDetailsService;
    private final JwtSigningKeyProvider jwtSigningKeyProvider;

    public JwtTokenProvider(PieshopUserDetailsService pieshopUserDetailsService, JwtSigningKeyProvider jwtSigningKeyProvider) {
        this.pieshopUserDetailsService = pieshopUserDetailsService;
        this.jwtSigningKeyProvider = jwtSigningKeyProvider;
    }

    public String generateToken(String username, List<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(Role::name).toList());

        Instant now = Instant.now();
        Instant expirationTime = now.plus(secondsFromNow, ChronoUnit.SECONDS);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expirationTime))
                .signWith(jwtSigningKeyProvider.getPrivateKey()) // <- this is important, we need a key to sign the jwt
                .compact();

    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                     .setSigningKey(jwtSigningKeyProvider.getPrivateKey())
                     .parseClaimsJws(token)
                     .getBody();
            String username = claims.getSubject();
            UserDetails userDetails = pieshopUserDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, "" , userDetails.getAuthorities());
        } catch (Exception e) {
            throw new JwtException("Bearer Token invalid");
        }

    }

}
