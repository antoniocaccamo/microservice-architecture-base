package microservices.base.organization.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class Department {

	private Long id;
	private Long organizationId;
	private String name;
    private List<Employee> employees = new ArrayList<>();

	public Department() {

	}

	public Department(Long organizationId, String name) {
		super();
		this.organizationId = organizationId;
		this.name = name;
	}
}