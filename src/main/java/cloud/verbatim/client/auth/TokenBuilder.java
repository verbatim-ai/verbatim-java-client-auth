package cloud.verbatim.client.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.util.Objects;

/**
 * The TokenBuilder class is responsible for creating and generating JWT tokens
 * with specific claims and configurations. It simplifies the process of crafting
 * tokens with predefined issuer information, expiration times, and custom claims.
 * It requires a valid {@code Key} instance that provides the necessary credentials
 * for signing the token.
 *
 * This class enforces validation of critical parameters, such as the signing key,
 * expiration time, user ID, and user email, ensuring that a well-formed token is
 * generated.
 */
public class TokenBuilder {

    private final static String ISSUER = "https://api.verbatim.cloud";
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
     * @throws RuntimeException if {@code userId} is blank.
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
     * @throws RuntimeException if {@code userEmail} is empty or blank.
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
