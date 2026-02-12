package com.marcossial.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifierService {
    private final Map<Event, List<EventListener>> listeners = new HashMap<>() {{
        put(Event.CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscribe(final Event eventType, EventListener listener) {
        var selectedListeners = listeners.get(eventType);
        selectedListeners.add(listener);
    }

    public void notify(final Event eventType) {
        listeners.get(eventType).forEach(l -> l.update(eventType));
    }
}
