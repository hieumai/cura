package com.kms.cura.event;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by linhtnvo on 6/10/2016.
 */
public class EventBroker {
    public Map<String, Set<EventHandler>> eventHandlers;

    private static EventBroker mInstance;

    private EventBroker() {
        //
        eventHandlers = new HashMap<String, Set<EventHandler>>();
    }

    public static EventBroker getInstance() {
        if (mInstance == null) {
            mInstance = new EventBroker();
        }
        return mInstance;
    }

    public synchronized void register(EventHandler eventHandler, String event) {
        Set<EventHandler> eventHandlerList = getEventHandlerSet(event);
        eventHandlerList.add(eventHandler);
    }

    public synchronized void unRegister(EventHandler eventHandler, String event) {
        Set<EventHandler> eventHandlerList = getEventHandlerSet(event);
        eventHandlerList.remove(eventHandler);
    }

    private Set<EventHandler> getEventHandlerSet(String event) {
        Set<EventHandler> eventHandlerList = eventHandlers.get(event);
        if (eventHandlerList == null) {
            eventHandlerList = new LinkedHashSet<EventHandler>();
            eventHandlers.put(event, eventHandlerList);
        }
        return eventHandlerList;
    }

    public synchronized void pusblish(String event, Object data) {
        Set<EventHandler> eventHandlerList = getEventHandlerSet(event);
        for (EventHandler eventHandler : eventHandlerList) {
            eventHandler.handleEvent(event, data);
        }
    }
}
