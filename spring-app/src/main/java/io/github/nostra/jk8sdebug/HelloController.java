package io.github.nostra.jk8sdebug;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HelloController {
    private static final AtomicInteger counter = new AtomicInteger();
    private final MeterRegistry registry;
    private Logger log = org.apache.logging.log4j.LogManager.getLogger(HelloController.class);

    private int accelerator;

    public HelloController(MeterRegistry registry) {
        this.registry = registry;
    }

    @Value("${app.accelerator:1}")
    public void setAccelerator(int accelerator) {
        this.accelerator = accelerator;
        log.info("Accelerator is {}", accelerator);
    }

    @RequestMapping("/")
    public String home() {
        if (counter.incrementAndGet() % 100 == 0) {
            log.info("Counter is [{}]", counter.get());
        }

        for (int i = 0; i++ < accelerator ; ) {
            String id = counter.get() + "-" + UUID.randomUUID().toString().substring(0, 6);
            Counter requestCounter = Counter.builder("app_metric_with_error")
                    .description("A metric which intentionally creates memory pressure")
                    .tags("request_id", id)
                    .register(registry);

            requestCounter.increment();
        }
        return "Hello World";
    }
}