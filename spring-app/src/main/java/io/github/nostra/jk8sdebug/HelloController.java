package io.github.nostra.jk8sdebug;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
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
        if (counter.incrementAndGet() % 100 == 0) {
            log.info("Counter is [{}]", counter.get());
        }

        for (int i = 0; i++ < 100; ) {
            String uniqueId = counter.get() + "-" + UUID.randomUUID().toString().substring(0, 6);
            Counter requestCounter = Counter.builder("app_requests_total")
                    .description("A metric which intentionally creates memory pressure")
                    .tags("version", "1.0")
                    .tags("request_id", uniqueId)
                    .tags("timestamp", String.valueOf(System.currentTimeMillis()))
                    .tags("counter_value", String.valueOf(counter.get()))
                    .register(registry);

            requestCounter.increment();
        }
        return "Hello World";
    }
}
