package com.prabhu.kafka.producer.exception;

public class BookStorageException extends RuntimeException {
    public BookStorageException(String message) {
        super(message);
    }
}