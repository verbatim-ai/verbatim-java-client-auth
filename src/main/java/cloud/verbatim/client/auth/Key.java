package cloud.verbatim.client.auth;

import lombok.Getter;
import lombok.ToString;

/**
 * The Key class represents a set of authentication parameters used to
 * securely authenticate requests to a remote system or API.
 * It encapsulates the necessary credentials required for secure communication.
 *
 * This class includes validation logic to ensure the integrity of the key's properties.
 *
 * Note:
 * - It is critical to handle and store instances of this class securely.
 * - Avoid exposing sensitive information (e.g., the secret key) in front-end applications or publicly accessible areas.
 */
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

    /**
     * Ensures that the key's fields are valid and initialized properly.
     *
     * This method checks the following conditions:
     * - `keyId`, `organizationId`, and `secret` must not be null.
     * - `keyId` and `organizationId` must not be blank.
     * - `secret` must not be blank and must have a minimum length of 64 characters.
     *
     * Throws:
     * - RuntimeException if any of the validation conditions are violated.
     *
     * Use this method to validate the integrity of key properties before using them
     * in operations that require secure authentication.
     */
    void assertContent() {

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
