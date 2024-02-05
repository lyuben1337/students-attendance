package com.example.studentattendance.database.config;

public class TenantNotFoundException extends Exception {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
