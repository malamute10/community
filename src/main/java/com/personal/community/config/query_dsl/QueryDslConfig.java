package com.personal.community.config.query_dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @Bean
    public JPAQueryFactory jpqlQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}

