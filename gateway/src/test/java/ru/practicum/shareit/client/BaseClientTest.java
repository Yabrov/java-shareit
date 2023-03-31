package ru.practicum.shareit.client;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class BaseClientTest {

    private final RestTemplate rest = Mockito.mock(RestTemplate.class);

    private final BaseClient baseClient = new BaseClient(rest);

    public BaseClientTest() {
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any(),
                        anyMap()))
                .thenReturn(ResponseEntity.ok(null));
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(ResponseEntity.ok().body(null));
    }

    @Test
    void getRequestTest() {
        assertThat(baseClient.get("")).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.get("", 1L)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.get("", 1L, Collections.EMPTY_MAP)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(2)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void getRequestWithErrorTest() {
        Map<String, String> body = Map.of("error", "mes");
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(ResponseEntity.badRequest().body(body));
        assertThat(baseClient.get("")).isEqualTo(ResponseEntity.badRequest().body(body));
        assertThat(baseClient.get("", 1L)).isEqualTo(ResponseEntity.badRequest().body(body));
        verify(rest, times(2)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void postRequestTest() {
        assertThat(baseClient.post("", null)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.post("", 1L)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.post("", 1L, Collections.EMPTY_MAP, null)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(2)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void putRequestTest() {
        assertThat(baseClient.put("", 1L, null)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.put("", 1L, Collections.EMPTY_MAP, null)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void deleteRequestTest() {
        assertThat(baseClient.delete("")).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.delete("", 1L)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.delete("", 1L, Collections.EMPTY_MAP)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(2)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }

    @Test
    void patchRequestTest() {
        assertThat(baseClient.patch("", null)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.patch("", 1L)).isEqualTo(ResponseEntity.ok(null));
        assertThat(baseClient.patch("", 1L, Collections.EMPTY_MAP, null)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(2)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }
}
