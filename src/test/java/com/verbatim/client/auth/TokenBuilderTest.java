package com.verbatim.client.auth;

import lombok.extern.java.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
@Log
class TokenBuilderTest {

    @Test
    void keyFail() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TokenBuilder().key(null);
        });
    }

    @Test
    void expiresAtFails() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TokenBuilder().expiresAt(null);
        });
    }

    @Test
    void userId() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TokenBuilder().userId(null);
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            new TokenBuilder().userId("  ");
        });
    }

    @Test
    void userEmail() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new TokenBuilder().userEmail(null);
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            new TokenBuilder().userEmail("  ");
        });
    }

    @Test
    void buildFail() {
        Assertions.assertThrows(TokenException.class, () -> {
            new TokenBuilder().build();
        });
        Assertions.assertThrows(TokenException.class, () -> {
            new TokenBuilder().expiresAt(Instant.now().plus(1, ChronoUnit.DAYS)).build();
        });
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(TestKeyLoader.class.getResourceAsStream("/sampleKey.json")));
        Assertions.assertThrows(TokenException.class, () -> {
            new TokenBuilder().key(new KeyLoader().from(reader).get()).build();
        });
    }

    @Test
    void buildInvalidKey() throws TokenException {
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(TestKeyLoader.class.getResourceAsStream("/invalidKey.json")));
        Assertions.assertThrows(TokenException.class,() -> new TokenBuilder()
                .key(new KeyLoader().from(reader).get())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build());
    }
    @Test
    void build() throws TokenException {
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(TestKeyLoader.class.getResourceAsStream("/demoKey.json")));
        String token = new TokenBuilder()
                .key(new KeyLoader().from(reader).get())
                .expiresAt(Instant.now().plus(1, ChronoUnit.HOURS))
                .build();
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 100);
    }

    @Test
    void buildWithClaims() throws TokenException {
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(TestKeyLoader.class.getResourceAsStream("/demoKey.json")));
        String token = new TokenBuilder()
                .key(new KeyLoader().from(reader).get())
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .userEmail("myemail")
                .userId("myid")
                .build();
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 100);
        log.info("test token: " + token);
    }
}