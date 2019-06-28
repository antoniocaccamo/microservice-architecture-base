package microservices.base.organization.model;

import lombok.Data;

@Data
public class Employee {

    private Long id;
    private String name;
    private int age;
    private String position;
}