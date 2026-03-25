package com.verbatim.client.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

/**
 * The TokenBuilder class is responsible for creating and generating JWT tokens
 * with specific claims and configurations. It simplifies the process of crafting
 * tokens with predefined issuer information, expiration times, and custom claims.
 * It requires a valid {@code Key} instance that provides the necessary credentials
 * for signing the token.
 * <p>
 * This class enforces validation of critical parameters, such as the signing key,
 * expiration time, user ID, and user email, ensuring that a well-formed token is
 * generated.
 */
public class TokenBuilder {

    private final static String ISSUER = "verbatim_client";
    private final static String CLAIMS_USER_ID = "uid";
    private final static String CLAIMS_USER_EMAIL = "email";

    Key key;
    String userId = null;
    String userEmail = null;
    Instant expiresAt = null;

    /**
     * Sets the key used for building the token. This method ensures that the provided
     * key is not null and associates the given key with the token being built.
     *
     * @param key the key used for signing the token. Must not be null.
     * @return the current instance of {@code TokenBuilder} to allow method chaining.
     * @throws NullPointerException if {@code key} is null.
     */
    public TokenBuilder key(Key key) {
        Objects.requireNonNull(key, "key cannot be null");
        this.key = key;
        return this;
    }

    /**
     * Sets the expiration time for the token being built. The provided expiration
     * time must not be null and cannot be earlier than the current time.
     */
    public TokenBuilder expiresAt(Instant expiresAt) {
        Objects.requireNonNull(expiresAt, "expiresAt cannot be null");
        Instant now = Instant.now();
        if (expiresAt.isBefore(now)) {
            throw new RuntimeException("expiresAt cannot be before now");
        }

        if (expiresAt.isAfter(now.plusSeconds(60 * 60 * 24))) {
            throw new RuntimeException("expiresAt cannot be more than 1 day in the future");
        }
        this.expiresAt = expiresAt;
        return this;
    }

    /**
     * Sets the unique identifier for the user to be included as a claim in the token being built.
     * This method ensures that the provided user ID is neither null nor blank.
     *
     * @param userId the unique identifier for the user. Must not be null or blank.
     * @return the current instance of {@code TokenBuilder} to allow method chaining.
     * @throws NullPointerException if {@code userId} is null.
     * @throws RuntimeException     if {@code userId} is blank.
     */
    public TokenBuilder userId(String userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        if (userId.isBlank()) {
            throw new RuntimeException("userId cannot be empty");
        }
        this.userId = userId;
        return this;
    }

    /**
     * Sets the email of the user to be included as a claim in the token being built.
     * This method validates that the provided email is neither null nor empty and throws
     * an exception if these constraints are violated.
     *
     * @param userEmail the email address of the user to be added as a claim. Must not be null or empty.
     * @return the current instance of {@code TokenBuilder} to allow method chaining.
     * @throws NullPointerException if {@code userEmail} is null.
     * @throws RuntimeException     if {@code userEmail} is empty or blank.
     */
    public TokenBuilder userEmail(String userEmail) {
        Objects.requireNonNull(userEmail, "userEmail cannot be null");
        if (userEmail.isBlank()) {
            throw new RuntimeException("userEmail cannot be empty");
        }
        this.userEmail = userEmail;
        return this;
    }

    /**
     * Builds and returns a signed JSON Web Token (JWT) based on the provided key, expiration time,
     * and optional claims. The method validates that the necessary parameters, such as the key
     * and expiration time, are set prior to building the token. If any of these critical fields
     * are missing, an exception is thrown. Additional claims for user ID and user email are added
     * to the token if they are provided.
     *
     * @return a signed JWT as a String using the HMAC512 algorithm with the provided key's secret.
     * @throws RuntimeException if the key or expiration time is not set.
     */
    public String build() throws TokenException {

        if (key == null) {
            throw new TokenException("Key not set");
        }

        if (expiresAt == null) {
            throw new TokenException("ExpireAt not set");
        }

        try {
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

            Algorithm algorithm = Algorithm.RSA512(readX509PublicKey(key.publicKey), readPKCS8PrivateKey(key.privateKey));
            return builder.sign(algorithm);
        } catch (Exception e) {
            throw new TokenException(e);
        }
    }

    public static RSAPublicKey readX509PublicKey(String key) throws TokenException {
        try {
            String publicKeyPEM = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("(\\n|\\r|\\s)", "");

            byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new TokenException(e);
        }
    }

    public RSAPrivateKey readPKCS8PrivateKey(String key) throws TokenException {
        try {

            String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("(\\n|\\r|\\s)", "");

            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new TokenException(e);
        }
    }


}
