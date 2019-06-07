package me.antoniocaccamo.ors.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController @Slf4j
@RequestMapping("me")
public class UserRestController {


    @GetMapping
    public Principal user(Principal user) {
        return user;
    }

}
