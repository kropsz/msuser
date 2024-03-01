package com.compassuol.sp.challenge.msuser.mqueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.compassuol.sp.challenge.msuser.enumerate.EventEnum;
import com.compassuol.sp.challenge.msuser.model.Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class EventNotificationPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private Queue queueEventNotification;

    @InjectMocks
    private EventNotificationPublisher eventNotificationPublisher;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEventNotification() throws JsonProcessingException {
        Event event = new Event(); 
        String queueName = "testQueue";
        when(queueEventNotification.getName()).thenReturn(queueName);

        eventNotificationPublisher.eventNotification(event);

        verify(rabbitTemplate).convertAndSend(queueName, eventNotificationPublisher.converIntoJson(event));
    }
    @Test
    public void testConverIntoJson() throws JsonProcessingException {
        Event event = new Event("email@email.com", EventEnum.CREATE, "2021-08-01"); 
    
        String json = eventNotificationPublisher.converIntoJson(event);
        String expectedJson = "{\"email\":\"email@email.com\", \"event\":\"CREATE\", \"date\":\"2021-08-01\"}"; 
    
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode1 = mapper.readTree(json);
        JsonNode jsonNode2 = mapper.readTree(expectedJson);
    
        assertEquals(jsonNode2, jsonNode1);
    }
}