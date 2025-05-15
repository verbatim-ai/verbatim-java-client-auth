package com.verbatim.client.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

class KeyLoaderTest {

    @Test
    void fromInputStreamFails()  {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new KeyLoader().from((InputStream) null);
        });
    }

    @Test
    void fromInputStream()  {
        Key key=new KeyLoader().from(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json")).get();
        Assertions.assertNotNull(key);
    }

    @Test
    void testFromFile() throws FileNotFoundException {
       File file= new File(KeyLoaderTest.class.getResource("/sampleKey.json").getFile());
       Assertions.assertNotNull(file);
        Key key=new KeyLoader().from(file).get();
        Assertions.assertNotNull(key);
    }

    @Test
    void testFromPath() throws FileNotFoundException {
        File file= new File(KeyLoaderTest.class.getResource("/sampleKey.json").getFile());
        Assertions.assertNotNull(file);
        Key key=new KeyLoader().from(file.toPath()).get();
        Assertions.assertNotNull(key);
    }

    @Test
    void testFromReader() {
        InputStreamReader reader= new InputStreamReader(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json"));
        Assertions.assertNotNull(reader);
        Key key=new KeyLoader().from(reader).get();
        Assertions.assertNotNull(key);
    }

    @Test
    void get() {
        Key key=new KeyLoader().from(KeyLoaderTest.class.getResourceAsStream("/sampleKey.json")).get();
        Assertions.assertNotNull(key);
        Assertions.assertEquals("MY_KEY_ID",key.getKeyId());
        Assertions.assertEquals("MY_ORG_ID",key.getOrganizationId());
        Assertions.assertEquals("MY_SECRET_TO_TESTMY_SECRET_TO_TESTMY_SECRET_TO_TESTMY_SECRET_TO_TESTMY_SECRET_TO_TESTMY_SECRET_TO_TEST",key.getSecret());
    }
}