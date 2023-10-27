package me.decclient.api.managers.impl;

import java.io.IOException;

public interface IPacketHandler
{
    void handle(IConnection connection, byte[] bytes) throws IOException;

}