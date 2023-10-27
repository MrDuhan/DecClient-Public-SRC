package me.decclient.api.events.impl;

import me.decclient.api.events.Event;

public class UpdateWalkingPlayerEvent extends Event {

    public UpdateWalkingPlayerEvent(int stage) {
        super(stage);
    }
}

