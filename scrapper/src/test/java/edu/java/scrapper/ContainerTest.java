package edu.java.scrapper;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContainerTest extends IntegrationTest {
    @Test
    public void runMigrationTest() {
        assertTrue(POSTGRES.isRunning(), "Container running successfully");
    }
}
