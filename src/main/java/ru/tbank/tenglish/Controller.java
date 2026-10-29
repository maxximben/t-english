package ru.tbank.tenglish;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final UserRepository userRepository;

    @GetMapping("/user")
    User getUser(@RequestParam long id) {
        return userRepository.findUserById(id);
    }

    @GetMapping("/hello")
    String hello() {
        return "hello";
    }

}
