package com.chicos_ingenieros.zenkai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Import(TestContainersConfig.class)
@TestPropertySource(properties = {
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
