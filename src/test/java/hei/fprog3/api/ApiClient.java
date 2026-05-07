package hei.fprog3.api;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ApiClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:8080";
    private final String apiKey = Dotenv.load().get("API_KEY");

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public <T> T get(String path, Class<T> responseType) {
        try {
            var entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, responseType).getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        }
    }

    public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
        try {
            var entity = new HttpEntity<>(getHeaders());
            return restTemplate.exchange(baseUrl + path, HttpMethod.GET, entity, responseType).getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        }
    }

    public <T> T post(String path, Object body, ParameterizedTypeReference<T> responseType) {
        try {
            var entity = new HttpEntity<>(body, getHeaders());
            return restTemplate.exchange(baseUrl + path, HttpMethod.POST, entity, responseType).getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("HTTP Error: " + e.getStatusCode().value() + " " + e.getResponseBodyAsString(), e);
        }
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        try {
            var entity = new HttpEntity<>(body, getHeaders());
            return restTemplate.exchange(baseUrl + path, HttpMethod.PUT, entity, responseType).getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        }
    }
}