package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.service.HelloService;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor(onConstructor__ = @Autowired)
@RequestMapping("/hello")
@Slf4j
public class HelloController {

    private final HelloService helloService;

    @GetMapping
    public String sayHello(HttpServletRequest request) {
        return helloService.sendHello(request);
    }
}
