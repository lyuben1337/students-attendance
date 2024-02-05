package com.example.studentattendance.database.config;

public class TenantResolvingException extends Exception {
    public TenantResolvingException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
