package me.decclient.api.managers.impl;

import me.decclient.api.util.impl.Nameable;

public interface IConnectionEntry extends Nameable
{
    default int getId()
    {
        return 0x00;
    }
}