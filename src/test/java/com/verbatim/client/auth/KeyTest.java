package com.verbatim.client.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KeyTest {

    @Test
    void assertContent() {
        Key key=new Key();
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId=null;
        key.organizationId="oid";
        key.secret="secret";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="keyId";
        key.organizationId=null;
        key.secret="secret";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.secret=null;
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId=" ";
        key.organizationId="oid";
        key.secret="secret";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="keyId";
        key.organizationId=" ";
        key.secret="secret";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.secret=" ";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.secret="toosmall";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);
    }

    @Test
    void getKeyId() {
        Key key=new Key();
        key.keyId="ID";
        Assertions.assertEquals("ID", key.keyId);
    }

    @Test
    void getOrganizationId() {
        Key key=new Key();
        key.organizationId="OID";
        Assertions.assertEquals("OID", key.organizationId);
    }

    @Test
    void getSecret() {
        Key key=new Key();
        key.secret="SECRET";
        Assertions.assertEquals("SECRET", key.secret);
    }

    @Test
    void testToString() {
        Key key=new Key();
        key.keyId="ID";
        key.organizationId="OID";
        key.secret="SECRET";
        Assertions.assertEquals("Key(keyId=ID, organizationId=OID, secret=SECRET)", key.toString());
    }
}