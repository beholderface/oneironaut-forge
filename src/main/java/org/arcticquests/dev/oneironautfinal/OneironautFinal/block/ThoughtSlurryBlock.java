package org.arcticquests.dev.oneironautfinal.OneironautFinal.block;


import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
//import net.oneironaut.block.ThoughtSlurry;
//import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
//import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Abs;

public class ThoughtSlurryBlock extends LiquidBlock {
    public static final ResourceLocation ID =
            ResourceLocation.tryBuild(Oneironaut.MOD_ID, "thought_slurry");
    public static final Properties SETTINGS =
            Properties.copy(Blocks.WATER).noOcclusion().mapColor(MapColor.COLOR_PURPLE);
    public static final ThoughtSlurryBlock INSTANCE =
            new ThoughtSlurryBlock(ThoughtSlurry.STILL_FLUID, SETTINGS);

    public ThoughtSlurryBlock(ThoughtSlurry thoughtSlurry, Properties settings) {
        super(thoughtSlurry, settings);
    }

    //@Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }
    public boolean isPathfindable(BlockState state, BlockGetter world, BlockPos pos, PathComputationType type) {
        return true;
    }
}