package dev.dengchao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppServiceTest {

    @Autowired
    private AppService service;

    @Test
    void test() {
        assertNotNull(service);

        assertEquals("Hello World", service.hello("World"));

        assertNotNull(service.getOkHttpClient());
    }
}
