package me.antoniocaccamo.oas.repository;

import me.antoniocaccamo.oas.domain.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credentials,Long> {

    Credentials findByName(String name);

}
