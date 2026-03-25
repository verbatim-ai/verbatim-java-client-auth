package com.verbatim.client.auth;

public class TokenException extends Exception{
    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String message) {
        super(message);
    }
}
