package org.arcticquests.dev.oneironautfinal.OneironautFinal.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InstantBreakingBlockEntity extends BlockEntity {
    public InstantBreakingBlockEntity(BlockPos pos, BlockState state) {
        super(OneironautBlockRegistry.INSTANT_BREAKER_ENTITY.get(), pos, state);
    }

    public static <T extends BlockEntity> void tick(Level world, BlockPos pos){
        world.destroyBlock(pos, true);
    }
}
