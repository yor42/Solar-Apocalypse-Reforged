package com.yor42.solarapocalypse.gameobjects;

import com.yor42.solarapocalypse.Constants;
import com.yor42.solarapocalypse.SolApocalypseConfig;
import com.yor42.solarapocalypse.SolarApocalypseMapData;
import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.World;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.network.NetworkHooks;

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
                        player.sendMessage(new TextComponent(key), player.getUUID());
                    } else if ((!world.canSeeSky(pos.above()) || world.isNight()) && !player.isCrouching()) {
                        player.sendMessage(new TranslationTextComponent("message.nosunlight"), player.getUUID());
                    }

                    if (!state.getValue(ACTIVE) && (!world.canSeeSky(pos.above()) || player.isCrouching())) {
                        TileEntity TE = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
                        Block.dropResources(state, world, pos, TE);
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                } else if (SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                    player.sendMessage(new TranslationTextComponent(key), player.getUUID());
                } else {
                    if (!SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                        LightningBoltEntity lightingBolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
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
                                world.getServer().getPlayerList().broadcastMessage(new TranslationTextComponent("message.apocalypsestarted", player.getDisplayName(), Integer.toString((int) min)), ChatType.CHAT, player.getUUID());
                            } else {
                                world.getServer().getPlayerList().broadcastMessage(new TranslationTextComponent("message.apocalypsestarted.immidiate", player.getDisplayName()), ChatType.CHAT, player.getUUID());
                            }
                        }
                        return ActionResultType.CONSUME;
                    }
                }
            }else {
                boolean flag = !player.getPersistentData().getBoolean(Constants.MODID+"_sawConfigWarning");

                if (player.isCrouching()) {
                    TileEntity TE = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
                    Block.dropResources(state, world, pos, TE);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
                else if(flag) {
                    player.sendMessage(new TranslationTextComponent("message.apocalypse_disabled"), player.getUUID());
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

        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType use(BlockState state, Level world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
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
                        player.sendMessage(new TranslationTextComponent(key), player.getUUID());
                    } else if ((!world.canSeeSky(pos.above()) || world.isNight()) && !player.isCrouching()) {
                        player.sendMessage(new TranslationTextComponent("message.nosunlight"), player.getUUID());
                    }

                    if (!state.getValue(ACTIVE) && (!world.canSeeSky(pos.above()) || player.isCrouching())) {
                        TileEntity TE = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
                        Block.dropResources(state, world, pos, TE);
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }
                } else if (SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                    player.sendMessage(new TranslationTextComponent(key), player.getUUID());
                } else {
                    if (!SolarApocalypseMapData.isApocalypseStarted((ServerLevel) world)) {
                        LightningBoltEntity lightingBolt = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
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
                                world.getServer().getPlayerList().broadcastMessage(new TranslationTextComponent("message.apocalypsestarted", player.getDisplayName(), Integer.toString((int) min)), ChatType.CHAT, player.getUUID());
                            } else {
                                world.getServer().getPlayerList().broadcastMessage(new TranslationTextComponent("message.apocalypsestarted.immidiate", player.getDisplayName()), ChatType.CHAT, player.getUUID());
                            }
                        }
                        return ActionResultType.CONSUME;
                    }
                }
            }else {
                boolean flag = !player.getPersistentData().getBoolean(Constants.MODID+"_sawConfigWarning");

                if (player.isCrouching()) {
                    TileEntity TE = state.hasTileEntity() ? world.getBlockEntity(pos) : null;
                    Block.dropResources(state, world, pos, TE);
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                }
                else if(flag) {
                    player.sendMessage(new TranslationTextComponent("message.apocalypse_disabled"), player.getUUID());
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

        return ActionResultType.SUCCESS;
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
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        return this.defaultBlockState().setValue(ACTIVE, false);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }
}
