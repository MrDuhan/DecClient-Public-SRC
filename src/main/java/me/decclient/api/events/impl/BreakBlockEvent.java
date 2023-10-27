package me.decclient.api.events.impl;

import me.decclient.api.events.Event;
import net.minecraft.util.math.BlockPos;

public class BreakBlockEvent extends Event {

    BlockPos pos;

    public BreakBlockEvent(BlockPos blockPos){
        super();
        pos = blockPos;
    }

    public BlockPos getPos(){
        return pos;
    }

    public void setPos(BlockPos pos){
        this.pos = pos;
    }
}
