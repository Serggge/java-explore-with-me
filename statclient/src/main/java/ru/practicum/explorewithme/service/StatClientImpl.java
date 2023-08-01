package ru.practicum.explorewithme.service;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ListStatisticDto;
import ru.practicum.explorewithme.dto.StatisticDto;
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

    public StatisticDto sendHit(HitDto hitDto) {
        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        String uri = "http://" + statServiceUri + "/hit";
        return restTemplate.postForObject(uri, request, StatisticDto.class);
    }

    public List<StatisticDto> getStatisticByParams(Map<String, String> requestParams) {
        String serverUri = "http://" + statServiceUri + "/stats?";
        String encodedURL = requestParams.keySet().stream()
                .map(key -> key + "=" + encodeValue(requestParams.get(key)))
                .collect(joining("&", serverUri, ""));
        return restTemplate.getForObject(encodedURL, ListStatisticDto.class).getStatistic();
    }

    private String encodeValue(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
