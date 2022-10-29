package com.yor42.solarapocalypse.utils;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncStagePacket {
    private final MathUtils.STAGE stage;

    public SyncStagePacket(MathUtils.STAGE stage){
        this.stage = stage;
    }

    public static SyncStagePacket decode (final FriendlyByteBuf buffer){
        final MathUtils.STAGE stage = buffer.readEnum(MathUtils.STAGE.class);
        return new SyncStagePacket(stage);
    }

    public static void encode(final SyncStagePacket msg, final FriendlyByteBuf buffer){
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
