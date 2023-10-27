package me.decclient.api.util.impl;


import me.decclient.mod.modules.Module;

public class SimpleData
{
    private final int color;
    private final String description;

    public SimpleData(Module module, String description)
    {
        this(module, description, 0xffffffff);
    }

    public SimpleData(Module module, String description, int color)
    {
        super();
        this.color = color;
        this.description = description;
    }


    public int getColor()
    {
        return color;
    }


    public String getDescription()
    {
        return description;
    }

}