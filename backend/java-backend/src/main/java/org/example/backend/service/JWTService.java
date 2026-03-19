package org.example.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private String secretKey;

    //upon startup of Spring, this will run and generate a secret key using the generateSecretKey() method
    public JWTService() {
        secretKey = generateSecretKey();
    }

    //method to dynamically generate a secret key, as opposed to hardcoding one
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("Secret Key: " + secretKey.toString());
            //then save the byte array as a String
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // expiration set for 5 minutes
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                //the signWith here is what generates the unique signature!
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getKey() {
        // convert the String back to a byte array, then return it as a Key object
        byte [] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public String extractUserName(String token) {
        // extract the username from jwt token.
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // signature from the token is verified and json payload is decoded
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    // returns true if the username matches the DB and if the token isn't expired
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // check if token hasn't expired. This calls extractExpiration() method
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // extract the expiration timestamp
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
