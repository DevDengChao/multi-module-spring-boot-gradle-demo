package dev.dengchao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    private static final Logger log = LoggerFactory.getLogger(AppService.class);

    @NonNull
    private final LibComponent component;

    public AppService(@NonNull LibComponent component) {
        this.component = component;
        log.info("Component injected {}", component);
    }

    @NonNull
    public String hello(@NonNull String name) {
        String res = component.hello(name);
        log.info("{}", res);
        return res;
    }
}
