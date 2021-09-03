package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GreetingHandler {
    public Mono<ServerResponse> hello(ServerRequest request) {
        log.info("Handling Greeting Request avinash@example.com");
        try {
            throw new Exception("Test");
        } catch (Exception ex) {
            log.error("Testing ",ex);
        }
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new Greeting("Hello Spring Reactive!")));
    }
}
