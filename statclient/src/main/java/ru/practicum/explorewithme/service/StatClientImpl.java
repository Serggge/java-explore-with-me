package ru.practicum.explorewithme.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.dto.HitDto;
import ru.practicum.explorewithme.dto.ListStatisticDto;
import ru.practicum.explorewithme.dto.StatisticDto;
import java.util.List;
import java.util.Map;

public class StatClientImpl implements StatClient {

    private final String statServiceUri;
    private final RestTemplate restTemplate;

    protected StatClientImpl(String uri) {
        this.statServiceUri = uri;
        this.restTemplate = new RestTemplate();
    }

    public HitDto sendHit(HitDto hitDto) {
        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        return restTemplate.postForObject(statServiceUri + "/hit", request, HitDto.class);
    }

    public List<StatisticDto> getStatisticByParams(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        params.forEach(map::add);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ListStatisticDto wrapperList = restTemplate.getForObject(statServiceUri + "/stats",
                ListStatisticDto.class, request);
        return wrapperList.getStatistic();
    }

}
