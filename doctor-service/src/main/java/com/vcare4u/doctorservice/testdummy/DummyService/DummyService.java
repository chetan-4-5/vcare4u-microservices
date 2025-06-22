package com.vcare4u.doctorservice.testdummy.DummyService;

import org.springframework.stereotype.Service;

@Service
public class DummyService {
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    public String throwError() {
        throw new RuntimeException("Forced error");
    }
}
