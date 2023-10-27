package me.decclient.api.managers.impl;

public interface IConnectionListener
{
    void onJoin(IConnectionManager manager, IConnection connection);

    void onLeave(IConnectionManager manager, IConnection connection);

}