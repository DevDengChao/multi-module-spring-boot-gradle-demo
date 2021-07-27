package dev.dengchao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LibComponentTest {

    @Autowired
    private LibComponent component;

    @Test
    void test() {
        Assertions.assertEquals("Hello World", component.hello("World"));
    }
}
