package com.example.demo.exceptions;

public class DuplicateMovieException extends RuntimeException{
    public DuplicateMovieException(String message) {
        super(message);
    }
}
