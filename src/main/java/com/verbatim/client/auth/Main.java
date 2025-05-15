package com.verbatim.client.auth;

import lombok.extern.java.Log;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
