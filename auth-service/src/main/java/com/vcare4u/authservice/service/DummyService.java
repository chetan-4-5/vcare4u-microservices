package com.vcare4u.authservice.service;

public class DummyService {
    
    public String greet(String name) {
        return "Hello, " + name;
    }

    public String errorMethod() {
        throw new RuntimeException("Forced error");
    }
}
