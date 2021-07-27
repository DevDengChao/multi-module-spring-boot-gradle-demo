# 如何基于 Gradle 构建多模块 Spring Boot 应用

## What:

这个工程展示了如何基于 `Gradle` 构建多模块的 `Spring Boot` 工程, 并编写了足够的单元测试以确保样板工程的稳定.

它实现了 `app` 模块使用 `lib` 模块提供的 `Component` 和 `lib` 模块依赖的其他第三方类库的能力.

## Why:

多模块构建一方面解决了代码复用问题, 另一方面可以将不相关的功能单独拿出来进行开发和测试, 降低了代码之间的耦合性.

## How:

### 1. 初始化空白工程

新建 `build.gradle` 文件, 并向其中添加:

```groovy
group 'dev.dengchao'
version '0.0.0-SNAPSHOT'
```

### 2. 添加 `lib` 模块

新建 `settings.gradle` 文件, 并向其中添加:

```groovy
include "lib"
```

向 `build.gradle` 文件中添加:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath "org.springframework.boot:spring-boot-gradle-plugin:2.5.3"
    }
}
// ...
```

新建 `lib/build.gradle` 文件, 并向其中添加:

```groovy
apply {
    plugin 'java'
    plugin 'org.springframework.boot'
    plugin 'io.spring.dependency-management'
}

repositories {
    mavenCentral()
}

test {
    useJUnitPlatform()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### 3. 添加并测试 `LibComponent`

新建 `lib/src/main/java/dev/dengchao/LibComponent.java` 文件, 并向其中添加:

```java

@Component
public class LibComponent {
    private static final Logger log = LoggerFactory.getLogger(LibComponent.class);

    public String hello(@NonNull String name) {
        String res = "Hello " + name;
        log.info("{}", res);
        return res;
    }
}
```

新建 `lib/src/test/java/dev/dengchao/LibTestApplication.java` 文件, 并向其中添加:

```java

@SpringBootApplication
class LibTestApplication {
}
```

新建 `lib/src/test/java/dev/dengchao/LibComponentTest.java` 文件, 并向其中添加:

```java

@SpringBootTest
class LibComponentTest {

    @Autowired
    private LibComponent component;

    @Test
    void test() {
        Assertions.assertEquals("Hello World", component.hello("World"));
    }
}
```

通过命令 `gradle :lib:test` 确认单元测试通过.

### 4. 添加 `app` 模块

复制 `lib/build.gradle` 到 `app/build.gradle`, 并向 `settings.gradle` 文件中添加:

```groovy
// ...
include "app"
```

### 5. 添加 `Application` 和 `AppService` 并进行测试

新建 `app/src/main/java/dev/dengchao/Application.java` 文件, 并向其中添加:

```java

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

新建 `app/src/main/java/dev/dengchao/AppService.java` 文件, 并向其中添加:

```java

@Service
public class AppService {
}
```

新建 `app/src/test/java/dev/dengchao/AppServiceTest.java` 文件, 并向其中添加:

```java

@SpringBootTest
class AppServiceTest {

    @Autowired
    private AppService service;

    @Test
    void test() {
        Assertions.assertNotNull(service);
    }
}
```

通过命令 `gradle :app:test` 确认单元测试通过.

### 6. 向 `app` 模块添加 `lib` 模块作为依赖

向 `app/build.gradle` 文件的 `dependencies` 中添加:

```groovy
//...
implementation project(":lib")
```

### 7. 测试 `lib` 模块中的类在 `app` 模块运行时可以被使用

修改 `app/src/main/java/dev/dengchao/AppService.java`:

```java

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
```

修改 `app/src/test/java/dev/dengchao/AppServiceTest.java`:

```diff
    @Test
    void test() {
        Assertions.assertNotNull(service);
+       Assertions.assertEquals("Hello World", service.hello("World"));
    }
```

通过命令 `gradle :app:test` 确认单元测试通过.

现在 `app` 模块能够复用 `lib` 模块提供的 `Component` 了.

### 8. 向 `lib` 模块添加第三方依赖, 并进行测试

向 `lib/build.gradle` 文件的 `dependencies` 中添加:

```groovy
//...
implementation 'com.squareup.okhttp3:okhttp:4.9.1'
```

新建 `lib/src/main/dev/dengchao/LibConfiguration.java` 文件, 并向其中添加:

```java

@Configuration
public class LibConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
```

新建 `lib/src/test/dev/dengchao/LibConfigurationTest.java` 文件, 并向其中添加:

```java

@SpringBootTest
class LibConfigurationTest {

    @Autowired
    private OkHttpClient client;

    @Test
    void test() {
        Assertions.assertNotNull(client);
    }
}
```

通过命令 `gradle :lib:test` 确认单元测试通过.

### 9. 向 `lib` 模块添加 `java-library` 插件并修改第三方库的依赖方式

向 `lib/build.gradle` 文件的 `apply` 中添加:

```groovy
//...
plugin 'java-library'
```

修改 `lib/build.gradle` 文件的 `dependencies` :

```diff
//...
- implementation 'com.squareup.okhttp3:okhttp:4.9.1'
+ api 'com.squareup.okhttp3:okhttp:4.9.1'
```

### 10. 测试 `app` 模块可以使用 `lib` 模块的第三方依赖

修改 `app/src/main/java/dev/dengchao/AppService.java`:

```java

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
```

修改 `app/src/test/java/dev/dengchao/AppServiceTest.java`:

```diff
    @Test
    void test(){
        Assertions.assertNotNull(service);
        Assertions.assertEquals("Hello World",service.hello("World"));
+       Assertions.assertNotNull(service.getOkHttpClient());
    }
```

通过命令 `gradle :app:test` 确认单元测试通过.

现在 `app` 模块能够使用 `lib` 模块提供的其他第三方依赖了.

## Reference:

+ [Spring Guide: Creating a Multi Module Project](https://spring.io/guides/gs/multi-module/)
+ [Gradle Guide: The Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)
+ [Gradle Sample: Building Java Libraries](https://docs.gradle.org/current/samples/sample_building_java_libraries.html)
