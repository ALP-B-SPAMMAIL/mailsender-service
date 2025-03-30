package com.example.mailsender.event;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.example.mailsender.eventDto.AbstractDto;

import lombok.Data;
@Data
public abstract class AbstractEvent {
    protected String topic;
    private String eventType;
    private AbstractDto payload;
    private Long timestamp;

    public AbstractEvent(AbstractDto payload) {
        this.payload = payload;
        this.eventType = this.getClass().getSimpleName();
        this.timestamp = Timestamp.valueOf(LocalDateTime.now()).getTime();
    }
}
