package com.compassuol.sp.challenge.msuser.model;

import com.compassuol.sp.challenge.msuser.enumerate.EventEnum;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String email;
    @Enumerated(EnumType.STRING)
    private EventEnum event;
    private String date;
}
