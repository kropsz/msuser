package com.compassuol.sp.challenge.msuser.common;

import com.compassuol.sp.challenge.msuser.enumerate.EventEnum;
import com.compassuol.sp.challenge.msuser.model.Event;

public class EventConstants {
    
    public static final Event VALID_EVENT_CREATE = new Event("seninha@email.com", EventEnum.CREATE, null);
    public static final Event VALID_EVENT_LOGIN = new Event("seninha@email.com", EventEnum.LOGIN, null);
    public static final Event VALID_EVENT_UPDATE = new Event("seninha@email.com", EventEnum.UPDATE, null);
    public static final Event VALID_EVENT_UPDATE_PASSWORD = new Event("seninha@email.com", EventEnum.UPDATE_PASSWORD, null);
}
