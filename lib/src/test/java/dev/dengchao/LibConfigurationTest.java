package dev.dengchao;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibConfigurationTest {

    @Autowired
    private OkHttpClient client;

    @Test
    void test() {
        Assertions.assertNotNull(client);
    }
}
