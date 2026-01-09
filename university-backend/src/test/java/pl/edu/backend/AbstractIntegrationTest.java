package pl.edu.backend;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.backend.config.TestContainersConfig;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Import(TestContainersConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AbstractIntegrationTest {
}
