package pl.edu.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersConfig {
    private static final String REUSE_PROPERTY = "${testcontainers.reuse.enable}";

    private static final String POSTGRES_IMAGE = "postgres:15-alpine";

    private static final String DB_USERNAME = "university_user";

    private static final String DB_PASSWORD = "university_password";

    private static final String DB_NAME = "university_db";

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer(@Value(REUSE_PROPERTY) boolean reusable) {
        return new PostgreSQLContainer<>(POSTGRES_IMAGE).withUsername(DB_USERNAME)
                .withPassword(DB_PASSWORD)
                .withDatabaseName(DB_NAME)
                .withReuse(reusable);
    }
}
