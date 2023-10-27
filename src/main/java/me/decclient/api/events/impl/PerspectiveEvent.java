package me.decclient.api.events.impl;

import me.decclient.api.events.Event;

public class PerspectiveEvent extends Event {

    private float angle;

    public PerspectiveEvent(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}

