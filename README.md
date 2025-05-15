# Core Authentication lib

Verbatim AI API

- API version: v1

- Generator version: 7.13.0

**Welcome on Verbatim AI Platform!**

You'll find here advanced specs of our APIs. You can play with these APIs on our
**[Swagger Playground](https://www.verbatim.cloud/api-docs/swagger)**. Feel free
to check our **[Cookbook](https://www.verbatim.cloud/cookbook)** to get samples
and how start easily.

_____

For more information, please
visit [https://www.verbatim.cloud](https://www.verbatim.cloud)

## Requirements

Building the API client library requires:

1. Java 1.8+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply
execute:

```shell
mvn clean install
```

To deploy it to a remote Maven repository instead, configure the settings of the
repository and execute:

```shell
mvn clean deploy
```

Refer to the [OSSRH Guide](http://central.sonatype.org/pages/ossrh-guide.html)
for more information.

### Maven users

Add this dependency to your project's POM:

```xml

<dependency>
    <groupId>com.verbatim.client</groupId>
    <artifactId>auth</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
  repositories {
    mavenCentral()
    // Needed if the 'google-api-client' jar has been published to maven central.
    mavenLocal()
    // Needed if the 'google-api-client' jar has been published to the local maven repo.
}

dependencies {
    implementation "com.verbatim.client:auth:1.0.0-SNAPSHOT"
}
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

- `target/google-api-client-1.0.0-SNAPSHOT.jar`
- `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the
following Java code:

```java

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
```

## Author

api@verbatim.cloud

