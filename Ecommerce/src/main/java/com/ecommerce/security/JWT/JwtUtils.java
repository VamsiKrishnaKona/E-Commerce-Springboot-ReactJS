package com.ecommerce.security.JWT;

import com.ecommerce.security.service.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${spring.app.dateExpirationMilleSeconds}")
    private int dateExpirationMilleSeconds;

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.cookieName}")
    private String jwtCookieName;

    //Getting JWT from Header
    public String extractTokenFromHeader(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Bearer Token: {}", bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            return bearerToken.substring(7);
        }
        return null;
    }

    //Get JWT from the Cookie
    public String getTokenFromCookie(HttpServletRequest request)
    {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);

        if(cookie != null)
        {
            return cookie.getValue();
        }
        return null;
    }

    //Generating Token for username
    public String generateTokenForUsername(String username)
    {
        //String username = userDetails.getUsername();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date().getTime() + dateExpirationMilleSeconds)))
                .signWith(key())
                .compact();
    }

    public ResponseCookie generateJwtCookie(UserDetails userPrincipal)
    {
        String JwtToken = generateTokenForUsername(userPrincipal.getUsername());

        ResponseCookie responseCookie = ResponseCookie
                .from(jwtCookieName, JwtToken)
                .path("/api")
                .httpOnly(false)
                .maxAge(24 * 60 * 60)
                .build();

        return responseCookie;
    }

    public ResponseCookie cleanJwtCookie()
    {
        return ResponseCookie
                .from(jwtCookieName, null)
                .path("/api")
                .build();
    }

    //Getting Username from JWT token
    public String getUsernameFromToken(String token)
    {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    //Generating signing key
    public Key key()
    {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    //Validating JWT Token
    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build().parseSignedClaims(token);

            return true;
        }
        catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        catch (ExpiredJwtException e) {
            logger.error("Expired JWT token: {}", e.getMessage());
        }
        catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        catch(SignatureException e) {
            logger.error("JWT claims string is invalid: {}", e.getMessage());
        }
        return false;
    }
}
