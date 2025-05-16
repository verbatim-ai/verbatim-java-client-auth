package cloud.verbatim.client.auth;

import lombok.extern.java.Log;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * The Main class serves as the entry point of the application.
 * It is responsible for generating an authentication token using a provided key file.
 * The key file must be passed as a command-line argument during execution.
 *
 * The workflow of the program is as follows:
 * 1. Validates if the required command-line argument (path to the key file) is provided.
 * 2. Loads the authentication key from the specified file.
 * 3. Generates an authentication token with a 24-hour expiration time.
 * 4. Outputs the token and its expiration time to the console.
 *
 * Behavior upon exceptions:
 * - Logs a warning if the required argument is missing and exits the program.
 * - Catches any exceptions during execution, logs an error message and stack trace, and exits the program.
 *
 * This class assumes the existence of helper classes such as {@link Key}, {@link KeyLoader}, and {@link TokenBuilder}.
 * The authentication process relies on these classes for loading the key and generating the token.
 */
@Log
public class Main {
    public static void main(String[] args) {

        if (args == null || args.length != 1) {
            log.warning("missing parameters. Usage : java -jar verbatim-java-client-auth.jar path/key.json");
            System.exit(1);
        }

        try {
            Key key = new KeyLoader().from(new File(args[0])).get();
            Instant expiredArt = Instant.now().plus(1, ChronoUnit.DAYS);
            String token = new TokenBuilder().key(key).expiresAt(expiredArt).build();
            System.out.println("Your token is ready. Expires at: " + expiredArt);
            System.out.println(token);
        } catch (Exception e) {
            System.err.println("Exception when calling Main");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
