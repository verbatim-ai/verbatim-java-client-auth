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
        key.privateKey="privateKey";
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="keyId";
        key.organizationId=null;
        key.privateKey="privateKey";
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.privateKey=null;
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId=" ";
        key.organizationId="oid";
        key.privateKey="privateKey";
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="keyId";
        key.organizationId=" ";
        key.privateKey="privateKey";
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.privateKey=" ";
        key.publicKey="publicKey";
        Assertions.assertThrows(RuntimeException.class, key::assertContent);

        key.keyId="kid";
        key.organizationId="oid";
        key.privateKey="toosmall";
        key.publicKey="publicKey";
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
    void getprivateKey() {
        Key key=new Key();
        key.privateKey="privateKey";
        Assertions.assertEquals("privateKey", key.privateKey);
    }

    @Test
    void testToString() {
        Key key=new Key();
        key.keyId="ID";
        key.organizationId="OID";
        key.privateKey="privateKey";
        Assertions.assertEquals("Key(keyId=ID, organizationId=OID, privateKey=privateKey, publicKey=null)", key.toString());
    }
}