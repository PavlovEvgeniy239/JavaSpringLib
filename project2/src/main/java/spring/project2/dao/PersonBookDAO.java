package spring.project2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;

@Component
public class PersonBookDAO {

    private final EntityManager entityManager;

    @Autowired
    public PersonBookDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
