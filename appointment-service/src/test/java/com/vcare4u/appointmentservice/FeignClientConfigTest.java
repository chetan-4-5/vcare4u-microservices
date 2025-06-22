package com.vcare4u.appointmentservice;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.vcare4u.appointmentservice.config.FeignClientConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FeignClientConfigTest {

    private HttpServletRequest httpServletRequest;
    private ServletRequestAttributes servletRequestAttributes;

    @BeforeEach
    void setUp() {
        httpServletRequest = mock(HttpServletRequest.class);
        servletRequestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void shouldAddAuthorizationHeaderToRequestTemplate() {
        // Arrange
        String token = "Bearer test-token";
        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        FeignClientConfig config = new FeignClientConfig();
        RequestInterceptor interceptor = config.requestInterceptor();
        RequestTemplate template = new RequestTemplate();

        // Act
        interceptor.apply(template);

        // Assert
        assertThat(template.headers()).containsKey("Authorization");
        assertThat(template.headers().get("Authorization")).contains(token);
    }

    @Test
    void shouldNotAddHeaderIfRequestIsNull() {
        // Arrange
        RequestContextHolder.resetRequestAttributes(); // simulate null request context
        FeignClientConfig config = new FeignClientConfig();
        RequestInterceptor interceptor = config.requestInterceptor();
        RequestTemplate template = new RequestTemplate();

        // Act
        interceptor.apply(template);

        // Assert
        assertThat(template.headers()).doesNotContainKey("Authorization");
    }

    @Test
    void shouldNotAddHeaderIfAuthHeaderIsEmpty() {
        // Arrange
        when(httpServletRequest.getHeader("Authorization")).thenReturn("");
        FeignClientConfig config = new FeignClientConfig();
        RequestInterceptor interceptor = config.requestInterceptor();
        RequestTemplate template = new RequestTemplate();

        // Act
        interceptor.apply(template);

        // Assert
        assertThat(template.headers()).doesNotContainKey("Authorization");
    }
}
