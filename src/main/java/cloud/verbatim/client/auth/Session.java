package cloud.verbatim.client.auth;

import lombok.extern.java.Log;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * The Session class is responsible for managing an authenticated session by
 * providing functionality to generate and refresh tokens based on the provided key.
 * This class leverages a token-based authentication mechanism to handle session security.
 * <p>
 * It ensures that tokens are refreshed before expiration, based on a defined
 * Time-to-Live (TTL) and TTL offset, to avoid session invalidation.
 */
@Log
public class Session {

    static Key key = null;
    static String token = null;
    static Instant expiresAt = null;
    static Duration ttl = null;
    static Duration ttlOffset = null;

    /**
     * Initializes the session with the provided key, setting predefined default values
     * for the session's Time-to-Live (TTL) of 1 hour and TTL offset durations (30 seconds).
     *
     * @param key the key used to authenticate and manage the session.
     */
    static public void init(Key key) {
        init(key, Duration.ofHours(1), Duration.ofSeconds(30));
    }

    /**
     * Initializes the session with the given key, time-to-live (TTL), and TTL offset.
     * This method sets up the session's key and timing values necessary for managing
     * token expiration and refresh operations.
     * Call init() before your first getToken() call
     *
     * @param key       the key used for authentication and session management, must not be null
     * @param ttl       the time-to-live duration for the session, defining how long the session token remains valid, must not be null
     * @param ttlOffset the duration to offset token expiration, allowing preemptive refreshing of the session token, must not be null
     */
    static public void init(Key key, Duration ttl, Duration ttlOffset) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(ttl);
        Objects.requireNonNull(ttlOffset);

        if (ttlOffset.compareTo(ttl) >= 0) {
            throw new RuntimeException("ttl must be greater than ttlOffset");
        }

        Session.key = key;
        Session.ttl = ttl;
        Session.ttlOffset = ttlOffset;
    }

    /**
     * /!\ IMPORTANT NOTICE:  Session.init() MUST be call once before using this function
     * <p>
     * Retrieves the session BEARER token. If the token is not set or has expired, this method
     * generates a new token using the configured key, time-to-live (TTL), and TTL offset values.
     * The expiration time is calculated strategically to ensure seamless session management.
     *
     * @return the current session token as a String
     */
    static public String getToken() {
        if (key == null) {
            throw new IllegalStateException("Session not initialized");
        }

        // Not set or expired
        if (token == null || expiresAt.isBefore(Instant.now().minus(ttlOffset))) {
            log.info("refreshing token");
            token = new TokenBuilder().expiresAt(Instant.now().plus(ttl)).key(key).build();
        }
        return token;
    }
}
