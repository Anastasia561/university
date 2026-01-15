package pl.edu.backend;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.backend.config.TestContainersConfig;

@ActiveProfiles("test")
@Testcontainers
@Import(TestContainersConfig.class)
public abstract class AbstractIntegrationTest {
}
