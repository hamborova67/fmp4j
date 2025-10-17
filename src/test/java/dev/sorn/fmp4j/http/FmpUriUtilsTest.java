package dev.sorn.fmp4j.http;

import static dev.sorn.fmp4j.http.FmpUriUtils.uri;
import static dev.sorn.fmp4j.http.FmpUriUtils.uriWithParams;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FmpUriUtilsTest {
    @Test
    void uriWithParams_single_param() {
        // given
        var uri = uri("https://example.com");
        var params = Map.<String, Object>of("param", 42);

        // when
        var uriWithParams = uriWithParams(uri, params);

        // then
        assertEquals("https://example.com?param=42", uriWithParams.toString());
    }

    @Test
    void uriWithParams_multiple_params() {
        // given
        var uri = uri("https://example.com");
        var params = Map.<String, Object>of("param", 42);

        // when
        var uriWithParams = uriWithParams(uri, params);

        // then
        assertEquals("https://example.com?param=42", uriWithParams.toString());
    }

    @Test
    void uriWithParams_null_params() {
        // given
        var uri = uri("https://example.com");
        var params = (Map<String, Object>) null;

        // when
        var uriWithParams = uriWithParams(uri, params);

        // then
        assertEquals("https://example.com", uriWithParams.toString());
    }

    @Test
    void uriWithParams_empty_params() {
        // given
        var uri = uri("https://example.com");
        var params = Map.<String, Object>of();

        // when
        var uriWithParams = uriWithParams(uri, params);

        // then
        assertEquals("https://example.com", uriWithParams.toString());
    }

    @Test
    void uriWithParams_illegalQueryKey_throwsFmpHttpException() {
        var uri = URI.create("https:/.com");
        var params = Map.<String, Object>of("param", 42);

        FmpHttpException ex = assertThrows(FmpHttpException.class, () -> {
            FmpUriUtils.uriWithParams(uri, params);
        });

        assertTrue(ex.getMessage().contains("Failed to build URI"));
    }

    @Test
    void privateConstructor_throwsAssertionError() throws Exception {
        Constructor<FmpUriUtils> constructor = FmpUriUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, constructor::newInstance);

        Throwable cause = thrown.getCause();
        assertInstanceOf(AssertionError.class, cause);
        assertTrue(cause.getMessage().contains("FmpUriUtils cannot be instantiated"));
    }
}
