package com.yor42.solarapocalypse.gameobjects;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import com.yor42.solarapocalypse.SolarApocalypseMapData;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPandorasTotem extends Block {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlockPandorasTotem(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if(!world.isClientSide()){
            if(SolApocalypseConfig.CONFIG.EnableApocalypse.get()) {
                boolean flag = player.getPersistentData().getBoolean(Constants.MODID+"_sawConfigWarning");
                if(flag) {
                    player.getPersistentData().putBoolean(Constants.MODID + "_sawConfigWarning", false);
                }
                boolean isApocalypseStarted = SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world) && (MathUtils.shouldExcuteStage((ServerLevel) world, MathUtils.STAGE.STAGE_1) || MathUtils.shouldExcuteStage((ServerLevel) world, MathUtils.STAGE.STAGE_2) || MathUtils.shouldExcuteStage((ServerLevel) world, MathUtils.STAGE.STAGE_3) || MathUtils.shouldExcuteStage((ServerLevel) world, MathUtils.STAGE.STAGE_3_5));
                String key = isApocalypseStarted ? "message.apocalypse_already_triggered" : "message.gather_resource";
                if (!state.getValue(ACTIVE) && !world.canSeeSky(pos.above()) || world.isNight() || player.isCrouching()) {

                    if (SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world) && !player.isCrouching()) {
                        player.sendMessage(new TranslatableComponent(key), player.getUUID());
                    } else if ((!world.canSeeSky(pos.above()) || world.isNight()) && !player.isCrouching()) {
                        player.sendMessage(new TranslatableComponent("message.nosunlight"), player.getUUID());
                    }

                    if (!state.getValue(ACTIVE) && (!world.canSeeSky(pos.above()) || player.isCrouching())) {
                        BlockEntity TE = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                        Block.dropResources(state, world, pos, TE);
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                } else if (SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                    player.sendMessage(new TranslatableComponent(key), player.getUUID());
                } else {
                    if (!SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                        LightningBolt lightingBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, world);
                        lightingBolt.moveTo(pos.getX(), pos.above().getY(), pos.getZ());
                        world.addFreshEntity(lightingBolt);
                        SolarApocalypseMapData.StartApocalypse((ServerLevel) world);
                        world.setBlock(pos, world.getBlockState(pos).setValue(BlockPandorasTotem.ACTIVE, SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)), 2);
                        if (world.getServer() != null) {
                            double[] daylist = {SolApocalypseConfig.CONFIG.STAGE_1.get(), SolApocalypseConfig.CONFIG.STAGE_2.get(), SolApocalypseConfig.CONFIG.STAGE_3.get(), SolApocalypseConfig.CONFIG.STAGE_3_5.get()};
                            double min = daylist[0];
                            for (double y : daylist) {
                                if (y < min && y >= 0) {
                                    min = y;
                                }
                            }
                            if ((int) min > 0) {
                                world.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("message.apocalypsestarted", player.getDisplayName(), Integer.toString((int) min)), ChatType.CHAT, player.getUUID());
                            } else {
                                world.getServer().getPlayerList().broadcastMessage(new TranslatableComponent("message.apocalypsestarted.immidiate", player.getDisplayName()), ChatType.CHAT, player.getUUID());
                            }
                        }
                        return InteractionResult.CONSUME;
                    }
                }
            }else {
                boolean flag = !player.getPersistentData().getBoolean(Constants.MODID+"_sawConfigWarning");

                if (player.isCrouching()) {
                    BlockEntity TE = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
                    Block.dropResources(state, world, pos, TE);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
                else if(flag) {
                    player.sendMessage(new TranslatableComponent("message.apocalypse_disabled"), player.getUUID());
                    player.getPersistentData().putBoolean(Constants.MODID+"_sawConfigWarning", true);
                }
                else{
                    world.setBlock(pos, world.getBlockState(pos).setValue(BlockPandorasTotem.ACTIVE, !world.getBlockState(pos).getValue(ACTIVE)), 2);
                }
            }
        }

        if(!world.isNight() && state.hasProperty(ACTIVE) && !world.isClientSide() && SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world) != state.getValue(ACTIVE)){
            world.setBlock(pos, world.getBlockState(pos).setValue(BlockPandorasTotem.ACTIVE, SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)), 2);
        }

        return InteractionResult.SUCCESS;
    }
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.tick(state, world, pos, random);
        if(!world.isClientSide() && SolarApocalypseMapData.isApocalypseStarted(world) != state.getValue(ACTIVE)){
            world.setBlock(pos, world.getBlockState(pos).setValue(BlockPandorasTotem.ACTIVE, SolarApocalypseMapData.isApocalypseStarted(world)), 2);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }
}
