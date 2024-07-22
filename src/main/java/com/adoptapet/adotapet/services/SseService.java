package com.adoptapet.adotapet.services;

import com.adoptapet.adotapet.dto.PetDto;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class SseService {

    private final ConcurrentLinkedQueue<FluxSink<ServerSentEvent<List<PetDto>>>> sinks = new ConcurrentLinkedQueue<>();

    public Flux<ServerSentEvent<List<PetDto>>>getEvents() {
        return Flux.create(emitter -> {
            sinks.add(emitter.onDispose(() -> sinks.remove(emitter)));
        }, FluxSink.OverflowStrategy.LATEST);
    }

    public void sendUpdate(List<PetDto> message) {
        sinks.forEach(sink -> sink.next(ServerSentEvent.builder(message).build()));
    }
}