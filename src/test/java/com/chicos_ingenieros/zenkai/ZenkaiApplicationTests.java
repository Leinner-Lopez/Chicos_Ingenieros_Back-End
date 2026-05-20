package com.chicos_ingenieros.zenkai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "CLOUDINARY_CLOUD_NAME=test",
        "CLOUDINARY_API_KEY=test",
        "CLOUDINARY_API_SECRET=test",
        "JWT_SECRET=test-secret-key-for-testing-purposes-only-32chars"
})

class ZenkaiApplicationTests {

    @Test
    void contextLoads() {
    }

}
