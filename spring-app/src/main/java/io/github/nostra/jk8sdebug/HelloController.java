package io.github.nostra.jk8sdebug;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HelloController {
    private static final AtomicInteger counter = new AtomicInteger();
    private final MeterRegistry registry;
    private Logger log = org.apache.logging.log4j.LogManager.getLogger(HelloController.class);

    public HelloController(MeterRegistry registry) {
        this.registry = registry;
    }

    @RequestMapping("/")
    public String home() {
        if ( counter.incrementAndGet() % 100 == 0 ) {
            log.info("Counter is [{}]", counter.get());
        }
        Counter requestCounter = Counter.builder("app_requests_total")
                .description("A metric which is intentionally wrongly configured")
                .tags("version", "1.0")
                .tags("id", (counter.get() % 1000 )+ "")
                .register(registry);

        requestCounter.increment();
        return "Hello World";
    }
}
