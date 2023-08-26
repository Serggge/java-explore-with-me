package ru.practicum.explorewithme.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.dto.EndpointHit;
import ru.practicum.explorewithme.dto.ViewStats;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.joining;

public class StatClientImpl implements StatClient {

    private final String statServiceUri;
    private final RestTemplate restTemplate;

    protected StatClientImpl(String uri) {
        this.statServiceUri = uri;
        this.restTemplate = new RestTemplate();
    }

    public ViewStats sendHit(EndpointHit hitDto) {
        HttpEntity<EndpointHit> request = new HttpEntity<>(hitDto);
        String uri = "http://" + statServiceUri + "/hit";
        return restTemplate.postForObject(uri, request, ViewStats.class);
    }

    public List<ViewStats> getStatisticByParams(Map<String, String> requestParams) {
        String serverUri = "http://" + statServiceUri + "/stats?";
        String encodedURL = requestParams.keySet().stream()
                .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                .collect(joining("&", serverUri, ""));
        return restTemplate.exchange(encodedURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ViewStats>>(){}).getBody();
    }

    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
