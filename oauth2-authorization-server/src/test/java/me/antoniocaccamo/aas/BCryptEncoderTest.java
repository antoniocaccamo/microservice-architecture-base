package me.antoniocaccamo.aas;

import org.junit.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by antonio on 05/06/2019.
 */
@Slf4j
public class BCryptEncoderTest {

    @Test
    public void encrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        log.info("{}", encoder.encode("password"));
    }
}
