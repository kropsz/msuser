package com.compassuol.sp.challenge.msuser.mqueue;

import java.time.LocalDateTime;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.compassuol.sp.challenge.msuser.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventNotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEventNotification;

    public void eventNotification(Event event) throws JsonProcessingException {
        event.setDate(LocalDateTime.now().toString());
        var json = converIntoJson(event);
        rabbitTemplate.convertAndSend(queueEventNotification.getName(), json);
    }

    public String converIntoJson(Event event) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(event);
        return json;
    }
}
