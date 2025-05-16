package cloud.verbatim.client.auth;

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
        Assertions.assertThrows(RuntimeException.class, () -> {
            new TokenBuilder().build();
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            new TokenBuilder().expiresAt(Instant.now().plus(1, ChronoUnit.DAYS)).build();
        });
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json")));
        Assertions.assertThrows(RuntimeException.class, () -> {
            new TokenBuilder().key(new KeyLoader().from(reader).get()).build();
        });
    }

    @Test
    void build() {
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json")));
        String token = new TokenBuilder()
                .key(new KeyLoader().from(reader).get())
                .expiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
                .build();
        Assertions.assertNotNull(token);
        Assertions.assertTrue(token.length() > 100);
    }

    @Test
    void buildWithClaims() {
        InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json")));
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