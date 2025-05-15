package com.verbatim.client.auth.example;

import com.verbatim.client.auth.Key;
import com.verbatim.client.auth.KeyLoader;
import com.verbatim.client.auth.TokenBuilder;

import java.io.File;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TokenBuilderExample {
    public static void main(String[] args) {

        try {
            ///  Replace the path <code>/PATH_OF_MY_SECRET/key.json</code> by the valid path of your JSON key file on your server
            ///  This key file can be downloaded from your console
            ///  https://console.verbatim.cloud > Keys
            Key key = new KeyLoader().from(new File("/PATH_OF_MY_SECRET/key.json")).get();
            ///  Define the expiration date of your token. After this, your token is no more valid
            /// Here the token is valid for one day
            Instant expiredArt = Instant.now().plus(1, ChronoUnit.DAYS);
            ///  Build to token
            String token = new TokenBuilder().key(key).expiresAt(expiredArt).build();
            System.out.println("Your token is ready. Expires at: " + expiredArt);
            /// Use this token as a BEARER TOKEN to authenticate your API calls
            System.out.println(token);
        } catch (Exception e) {
            System.err.println("Exception when running TokenBuilderExample");
            System.err.println("Reason: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}