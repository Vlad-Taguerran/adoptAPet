package com.adoptapet.adotapet.configure.exceptions;

public class PetNotFound extends RuntimeException {
    public PetNotFound(String message) {
        super(message);
    }
}
