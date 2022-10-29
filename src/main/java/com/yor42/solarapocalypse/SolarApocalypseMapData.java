package com.yor42.solarapocalypse;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.nbt.CompoundTag;

public class SolarApocalypseMapData extends SavedData {

    private static final String ID = Constants.MODID+"_worlddata";

    private long apocalypseStartedTime;
    private boolean isApocalypseStarted;
    private MathUtils.STAGE stage;

    public static SolarApocalypseMapData create(){
        return new SolarApocalypseMapData();
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putLong("apocalypse_started_time", this.apocalypseStartedTime);
        compound.putBoolean("is_apocalypse_started", this.isApocalypseStarted);
        return compound;
    }



    public SolarApocalypseMapData load() {
        this.apocalypseStartedTime = nbt.getLong("apocalypse_started_time");
        this.isApocalypseStarted = nbt.getBoolean("is_apocalypse_started");

    }

    public void setApocalypseStarted(boolean apocalypseStarted) {
        this.isApocalypseStarted = apocalypseStarted;
        this.setDirty();
    }

    public void setApocalypseStartedTime(long time){
        this.apocalypseStartedTime = time;
        this.setDirty();
    }

    public boolean isApocalypseStarted(){
        return this.isApocalypseStarted;
    }

    public float getApocalypseStartedDay(){
        return (float) this.apocalypseStartedTime/24000;
    }

    public static SolarApocalypseMapData getMapdata(ServerLevel world){
        SolarApocalypseMapData storage = world.getDataStorage().get(SolarApocalypseMapData::new, ID);
        if(storage == null){
            storage = new SolarApocalypseMapData();
            world.getDataStorage().set(storage);
        }
        return storage;
    }

    public static void StartApocalypse(ServerLevel world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        mapdata.setApocalypseStarted(true);
        mapdata.setApocalypseStartedTime(world.getDayTime());
    }

    public static void StartApocalypseFronTheBeginning(ServerLevel world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        mapdata.setApocalypseStarted(true);
        mapdata.setApocalypseStartedTime(0);
    }

    public static boolean isApocalypseStarted(ServerLevel world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        return mapdata.isApocalypseStarted();
    }

}
