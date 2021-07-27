package dev.dengchao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class LibComponent {
    private static final Logger log = LoggerFactory.getLogger(LibComponent.class);

    public String hello(@NonNull String name) {
        String res = "Hello " + name;
        log.info("{}", res);
        return res;
    }
}
