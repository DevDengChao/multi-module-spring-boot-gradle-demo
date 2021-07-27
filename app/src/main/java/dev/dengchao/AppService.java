package dev.dengchao;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    private static final Logger log = LoggerFactory.getLogger(AppService.class);

    @NonNull
    private final LibComponent component;
    @NonNull
    private final OkHttpClient okHttpClient;

    public AppService(@NonNull LibComponent component, @NonNull OkHttpClient okHttpClient) {
        this.component = component;
        this.okHttpClient = okHttpClient;
        log.info("Component injected {}", component);
        log.info("3rd party library injected {}", okHttpClient);
    }

    @NonNull
    public String hello(@NonNull String name) {
        String res = component.hello(name);
        log.info("{}", res);
        return res;
    }

    @NonNull
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
