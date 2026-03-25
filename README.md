# Core Authentication lib

You'll find here java classes to build your RSA secured tokens to authenticate your next API calls.

You'll find here advanced specs of our APIs. You can play with these APIs on our
**[Swagger Playground](https://www.verbatim-ai.com/api-docs/swagger)**. Feel
free to check our **[Cookbook](https://www.verbatim-ai.com/cookbook)** to get samples
and how start easily.

_____

For more information, please
visit [https://www.verbatim-ai.com](https://www.verbatim-ai.com)

## Requirements

Building the API client auth library requires:

1. Java 1.8+
2. Maven/Gradle

## Installation

To install the API client library to your local Maven repository, simply
execute:

```shell
mvn clean install
```

## Getting Started

### Build your RSA key pair

#### Step #1: Build your private RAS key

```shell
openssl genrsa -out key.pem 4096
```

#### Step #2: Build your public part of your RSA key

```shell
openssl rsa -in key.pem -pubout -outform PEM -out public.pem
```

#### Step #3: Publish the PUBLIC part of your key in your Verbatim AI app 
Go to https://app.verbatim-ai.com > Section `Keys` and init a new key with the public part of
your key.

#### Step #4: Build your json key file

This file will be your credentials to use Verbatim AI Apis.

This file must look like.
```json
{
  "keyId": "MY_KEY_ID",
  "organizationId": "MY_ORG_ID",
  "privateKey": "-----BEGIN PRIVATE KEY-----\nMIIJQAI........MSU0d6yiGX4w=\n-----END PRIVATE KEY-----\n",
  "publicKey": "-----BEGIN PUBLIC KEY-----\nMIICIjANBg.......+2RzeuaUCAwEAAQ==\n-----END PUBLIC KEY-----\n"
}
```

where 
- `keyId` filled with the UID of your key in your app. This info in the detail info page of your key
- `organizationId` filled with the ID of your organization. This info in the detail info page of your key
- `privateKey` filled the private part of your RSA key. This value MUST stay private and never shared
- `publicKey` filled the public part of your RSA key, value you just published in your app. 

You can find samples in the `test` directory of this project (sample here [demoKey.json](/src/test/resources/demoKey.json))

Store this file in a SECURED space of your project (where you store your private key or credentials of your project).

⚠️ **Never share this file. This file is a credential. Do not commit it on your code repository**

### Build your first JWT token

Please follow the [installation](#installation) instruction, and when your get your credentials (`key.json`),  execute the
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
            ///  https://app.verbatim-ai.com > Keys
            Key key = new KeyLoader().from(new File("/PATH_OF_MY_SECRET/key.json")).get();
            ///  Define the expiration date of your token. After this, your token is no more valid
            /// Here the token is valid for one day
            Instant expiredAt = Instant.now().plus(1, ChronoUnit.HOURS);
            ///  Build to token
            String token = new TokenBuilder().key(key).expiresAt(expiredAt).build();
            System.out.println("Your token is ready. Expires at: " + expiredAt);
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

## Ready to authenticate your API calls
When this code succeeds, your can use this token produced with `new TokenBuilder().key(key).expiresAt(expiredAt).build()` in the authorization header to authenticate your API call. Check our [API docs](https://www.verbatim-ai.com/api-docs/index.html) to see how the token can be used.

## Author
contact[@]verbatim-ai.com
