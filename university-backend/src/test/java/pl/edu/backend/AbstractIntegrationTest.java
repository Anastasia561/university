package pl.edu.backend;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.edu.backend.config.TestContainersConfig;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Import(TestContainersConfig.class)
public class AbstractIntegrationTest {
}
