package com.verbatim.client.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.Objects;

public class TokenBuilder {

    private final static String ISSUER = "https://api.verbatim.cloud";
    private final static String CLAIMS_USER_ID = "uid";
    private final static String CLAIMS_USER_EMAIL = "email";

    Key key;
    String userId = null;
    String userEmail = null;
    Instant expiresAt = null;

    public TokenBuilder key(Key key) {
        Objects.requireNonNull(key, "key cannot be null");
        this.key = key;
        return this;
    }

    public TokenBuilder expiresAt(Instant expiresAt) {
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Instant now = Instant.now();
        if (expiresAt.isBefore(now)) {
            throw new RuntimeException("expiresAt cannot be before now");
        }
        this.expiresAt = expiresAt;
        return this;
    }

    public TokenBuilder userId(String userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        if (userId.isBlank()) {
            throw new RuntimeException("userId cannot be empty");
        }
        this.userId = userId;
        return this;
    }

    public TokenBuilder userEmail(String userEmail) {
        Objects.requireNonNull(userEmail, "userEmail cannot be null");
        if (userEmail.isBlank()) {
            throw new RuntimeException("userEmail cannot be empty");
        }
        this.userEmail = userEmail;
        return this;
    }

    public String build() {

        if (key == null) {
            throw new RuntimeException("Key not set");
        }

        if (expiresAt == null) {
            throw new RuntimeException("ExpireAt not set");
        }

        JWTCreator.Builder builder = JWT.create();
        builder
                .withIssuer(ISSUER)
                .withIssuedAt(Instant.now())
                .withSubject(key.getOrganizationId())
                .withKeyId(key.getKeyId())
                .withExpiresAt(expiresAt);

        if (userId != null) {
            builder.withClaim(CLAIMS_USER_ID, userId);
        }
        if (userEmail != null) {
            builder.withClaim(CLAIMS_USER_EMAIL, userEmail);
        }
        return builder.sign(Algorithm.HMAC512(key.getSecret()));
    }

}
