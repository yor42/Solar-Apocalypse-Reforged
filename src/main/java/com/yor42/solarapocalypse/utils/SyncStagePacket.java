package com.yor42.solarapocalypse.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.HOME;

public class SyncStagePacket {
    private final MathUtils.STAGE stage;

    public SyncStagePacket(MathUtils.STAGE stage){
        this.stage = stage;
    }

    public static SyncStagePacket decode (final PacketBuffer buffer){
        final MathUtils.STAGE stage = buffer.readEnum(MathUtils.STAGE.class);
        return new SyncStagePacket(stage);
    }

    public static void encode(final SyncStagePacket msg, final PacketBuffer buffer){
        buffer.writeEnum(msg.stage);
    }

    public static void handle(final SyncStagePacket msg, final Supplier<NetworkEvent.Context> ctx)
    {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MathUtils.LEVEL_STAGE = msg.stage;
                }));
        ctx.get().setPacketHandled(true);
    }
}
