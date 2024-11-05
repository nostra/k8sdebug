package io.github.nostra.jk8sdebug;

import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HelloController {
    private static final AtomicInteger counter = new AtomicInteger();
    private Logger log = org.apache.logging.log4j.LogManager.getLogger(HelloController.class);

    @RequestMapping("/")
    public String home() {
        if ( counter.incrementAndGet() % 100 == 0 ) {
            log.info("Counter is [{}]", counter.get());
        }
        return "Hello World";
    }
}
