package microservices.base.organization.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by antonio on 06/04/2019.
 */


@RestController
public class OrganitationRestController {



        @GetMapping
        public List<String> getAll(){
            return Arrays.asList("shhhkkkk", "dnaslkfalnf");
        }

    }



