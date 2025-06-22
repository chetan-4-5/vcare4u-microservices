package com.vcare4u.authservice;

import com.vcare4u.authservice.logging.LoggingAspect;
import com.vcare4u.authservice.service.DummyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.junit.jupiter.api.Assertions.*;

class LoggingAspectTest {

    private DummyService proxy;

    @BeforeEach
    void setUp() {
        DummyService target = new DummyService();           // Actual class
        LoggingAspect aspect = new LoggingAspect();         // Your Aspect

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        factory.addAspect(aspect);

        proxy = factory.getProxy();                         // AOP Proxy created
    }

    @Test
    void testBeforeAndAfterReturning() {
        // This triggers @Before and @AfterReturning
        String result = proxy.greet("Greeshma");
        assertEquals("Hello, Greeshma", result);
    }

    @Test
    void testAfterThrowing() {
        // This triggers @Before and @AfterThrowing
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            proxy.errorMethod();
        });

        assertEquals("Forced error", ex.getMessage());
    }
}
