package com.revature.music.utils;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message)
    {
        super(message);
    }
}
