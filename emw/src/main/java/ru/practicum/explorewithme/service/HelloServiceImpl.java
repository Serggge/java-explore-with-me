package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.HitDto;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor(onConstructor__ = @Autowired)
public class HelloServiceImpl implements HelloService {

    private final StatProxyService statService;

    @Override
    public String sendHello(HttpServletRequest request) {
        HitDto hitDto = new HitDto();
        hitDto.setUri(request.getRequestURI());
        hitDto.setApp("ewm-main-service");
        hitDto.setIp(request.getRemoteAddr());
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        hitDto.setTimestamp(timestamp.format(formatter));
        statService.addHit(hitDto);
        return "Hello!";
    }

}
