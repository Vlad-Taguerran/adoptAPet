package com.adoptapet.adotapet.controller.sseController;

import com.adoptapet.adotapet.dto.PetDto;
import com.adoptapet.adotapet.services.SseService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@RestController
public class ServerSideEventControllerr {

    private final SseService sseService;

    public ServerSideEventControllerr(SseService sseService) {
        this.sseService = sseService;
    }

    @GetMapping("api/pet/sse")
    public Flux<ServerSentEvent<List<PetDto>>> streamEvents() {
        return sseService.getEvents();
    }
}
