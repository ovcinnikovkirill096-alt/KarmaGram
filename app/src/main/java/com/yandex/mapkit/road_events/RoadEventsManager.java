package com.yandex.mapkit.road_events;

public interface RoadEventsManager {
    EventInfoSession requestEventInfo(String str, EventInfoSession.EventInfoListener eventInfoListener);
}
