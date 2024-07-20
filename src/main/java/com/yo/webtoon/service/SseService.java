package com.yo.webtoon.service;

import com.yo.webtoon.model.constant.SseCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {

    private static final List<SseEmitter> emitters = new ArrayList<>();
    private static final AtomicInteger eventId = new AtomicInteger(0);

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError((e) -> emitters.remove(emitter));

        return emitter;
    }

    public void sendAlarm(SseCode sseCd, String message) {
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .id(String.valueOf(eventId.incrementAndGet()))
                    .name(sseCd.name())
                    .data(message));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        });
    }
}
