package com.yor42.solarapocalypse;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;

public class SolarApocalypseMapData extends WorldSavedData {

    private static final String ID = Constants.MODID+"_worlddata";

    private long apocalypseStartedTime;
    private boolean isApocalypseStarted;

    public SolarApocalypseMapData() {
        super(ID);
    }

    @Override
    public void load(CompoundNBT nbt) {
        this.apocalypseStartedTime = nbt.getLong("apocalypse_started_time");
        this.isApocalypseStarted = nbt.getBoolean("is_apocalypse_started");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLong("apocalypse_started_time", this.apocalypseStartedTime);
        compound.putBoolean("is_apocalypse_started", this.isApocalypseStarted);
        return compound;
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

    public static SolarApocalypseMapData getMapdata(ServerWorld world){
        SolarApocalypseMapData storage = world.getDataStorage().get(SolarApocalypseMapData::new, ID);
        if(storage == null){
            storage = new SolarApocalypseMapData();
            world.getDataStorage().set(storage);
        }
        return storage;
    }

    public static void StartApocalypse(ServerWorld world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        mapdata.setApocalypseStarted(true);
        mapdata.setApocalypseStartedTime(world.getDayTime());
    }

    public static void StartApocalypseFronTheBeginning(ServerWorld world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        mapdata.setApocalypseStarted(true);
        mapdata.setApocalypseStartedTime(0);
    }

    public static boolean isApocalypseStarted(ServerWorld world){
        SolarApocalypseMapData mapdata = getMapdata(world);
        return mapdata.isApocalypseStarted();
    }

}
