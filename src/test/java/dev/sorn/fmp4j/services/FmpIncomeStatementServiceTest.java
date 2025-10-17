package dev.sorn.fmp4j.services;

import static dev.sorn.fmp4j.HttpClientStub.httpClientStub;
import static dev.sorn.fmp4j.TestUtils.assertAllFieldsNonNull;
import static dev.sorn.fmp4j.TestUtils.jsonTestResource;
import static dev.sorn.fmp4j.json.FmpJsonDeserializerImpl.FMP_JSON_DESERIALIZER;
import static java.util.stream.IntStream.range;
import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.sorn.fmp4j.HttpClientStub;
import dev.sorn.fmp4j.IncomeStatementTestData;
import dev.sorn.fmp4j.cfg.FmpConfigImpl;
import dev.sorn.fmp4j.http.FmpHttpClient;
import dev.sorn.fmp4j.http.FmpHttpClientImpl;
import dev.sorn.fmp4j.models.FmpIncomeStatement;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FmpIncomeStatementServiceTest implements IncomeStatementTestData {
    private final HttpClientStub httpStub = httpClientStub();
    private final FmpHttpClient http = new FmpHttpClientImpl(httpStub, FMP_JSON_DESERIALIZER);
    private final FmpService<FmpIncomeStatement[]> service = new FmpIncomeStatementService(new FmpConfigImpl(), http);

    @Test
    void relative_url() {
        // when
        var relativeUrl = service.relativeUrl();

        // then
        assertEquals("/income-statement", relativeUrl);
    }

    @Test
    void required_params() {
        // when
        var params = service.requiredParams();

        // then
        assertEquals(Set.of("symbol"), params);
    }

    @Test
    void optional_params() {
        // when
        var params = service.optionalParams();

        // then
        assertEquals(Set.of("period", "limit"), params);
    }

    @Test
    void successful_download() {
        // given
        var symbol = "AAPL";
        service.param("symbol", symbol);
        httpStub.configureResponse()
                .body(jsonTestResource("stable/income-statement/?symbol=%s.json", symbol))
                .statusCode(200)
                .apply();

        // when
        var result = service.download();

        // then
        assertEquals(5, result.length);
        assertEquals(anAnnualIncomeStatement(), result[0]);
        range(0, 5).forEach(i -> assertAllFieldsNonNull(result[i]));
    }

    @ParameterizedTest
    @CsvSource({"AAPL,annual,3", "AAPL,quarter,3", "GLIBK,quarter,1"})
    void successful_download_with_optional_period_and_limit(String symbol, String period, int limit) {
        // given
        service.param("symbol", symbol);
        service.param("period", period);
        service.param("limit", limit);
        httpStub.configureResponse()
                .body(jsonTestResource(
                        "stable/income-statement/?symbol=%s&period=%s&limit=%d.json", symbol, period, limit))
                .statusCode(200)
                .apply();

        // when
        var result = service.download();

        // then
        assertEquals(limit, result.length);
        range(0, limit).forEach(i -> assertAllFieldsNonNull(result[i], Set.of("cik")));
    }
}
