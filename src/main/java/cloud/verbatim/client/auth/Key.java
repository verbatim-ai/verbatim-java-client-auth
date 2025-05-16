package cloud.verbatim.client.auth;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Key {

    protected Key() {

    }

    /**
     * ID of your key
     * This ID is available from your console / Keys
     */
    String keyId;

    /**
     * ID of your Organization
     * This ID is available into your Verbatim console / Profile
     */
    String organizationId;

    /**
     * Your secret key.
     * NEVER SHARE THIS SECRET AND KEEP IT IN A SECURE PLACE. ONLY USE ON YOUR BACKEND SERVER.
     * DO NOT USE THIS KEY ON A FRONT END APP OR ON A DEVICE
     */
    String secret;

    public void assertContent() {

        if (keyId == null || organizationId == null || secret == null) {
            throw new RuntimeException("Invalid key. Missing keyId, organizationId or secret");
        }

        if (keyId.isBlank()) {
            throw new RuntimeException("Invalid keyID. keyId can't be blank");
        }

        if (organizationId.isBlank()) {
            throw new RuntimeException("Invalid organizationID. organizationId can't be blank");
        }

        if (secret.isBlank()) {
            throw new RuntimeException("Invalid secret. Secret can't be blank");
        }

        if (secret.length()<64) {
            throw new RuntimeException("Invalid secret. Secret value seems wrong. You probably use a sample key, not a valid key, get from your console.");
        }

    }

}
