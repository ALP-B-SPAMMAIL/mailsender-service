package com.example.mailsender.event;

import com.example.mailsender.eventDto.ReportCreatedEventDto;


import lombok.Getter;
import lombok.Setter;   

@Getter
@Setter
public class ReportCreatedEvent extends AbstractEvent {
    private ReportCreatedEventDto payload;

    public ReportCreatedEvent() {
        super();
    }

    public ReportCreatedEvent(ReportCreatedEventDto payload) {
        super(payload);
        this.payload = payload;
    }

    
}