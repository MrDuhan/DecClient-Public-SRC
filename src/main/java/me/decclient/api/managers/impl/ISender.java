package me.decclient.api.managers.impl;

import java.io.IOException;

public interface ISender
{
    void send(byte[] packet) throws IOException;

}