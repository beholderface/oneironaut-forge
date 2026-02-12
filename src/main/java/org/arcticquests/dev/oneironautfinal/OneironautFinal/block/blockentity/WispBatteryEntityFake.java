package org.arcticquests.dev.oneironautfinal.OneironautFinal.block.blockentity;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.items.pigment.ItemDyePigment;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.particles.ConjureParticleOptions;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import kotlin.collections.CollectionsKt;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import ram.talia.hexal.api.FunUtilsKt;
import ram.talia.hexal.common.entities.WanderingWisp;

import java.util.ArrayList;

public class WispBatteryEntityFake extends BlockEntity {
    public WispBatteryEntityFake(BlockPos pos, BlockState state){
        super(OneironautBlockRegistry.WISP_BATTERY_ENTITY_DECORATIVE.get(), pos, state);
    }

    public static int[] getColors(RandomSource random){
        ArrayList<Integer> colors = new ArrayList<>();
        for (int i = 0; i < 32; i++){
            for (ItemDyePigment color : HexItems.DYE_PIGMENTS.values()){
                colors.add(FunUtilsKt.nextColour((new FrozenPigment(new ItemStack(color), Util.NIL_UUID)), random));
            }
        }
        return CollectionsKt.toIntArray(colors);
    }

    public void tick(Level world, BlockPos pos, BlockState state){
        //only do anything when powered
        if (state.getValue(WispBatteryFake.REDSTONE_POWERED)){
            if (world.isClientSide){
                Vec3 doublePos = new Vec3(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
                int[] colors = getColors(world.random);
                world.addParticle(
                        new ConjureParticleOptions(colors[world.random.nextInt(colors.length)]),
                        doublePos.x, doublePos.y, doublePos.z,
                        0.0125 * (world.random.nextDouble() - 0.5),
                        0.0125 * (world.random.nextDouble() - 0.5),
                        0.0125 * (world.random.nextDouble() - 0.5)
                );
            } else {
                if (world.getGameTime() % 80 == 0 && world.getEntitiesOfClass(WanderingWisp.class, AABB.ofSize(Vec3.atCenterOf(pos), 64.0, 64.0, 64.0), (idfk)-> true).size() < 20){
                    WanderingWisp wisp = new WanderingWisp(world, Vec3.upFromBottomCenterOf(pos, 1));
                    wisp.setPigment(new FrozenPigment(
                            new ItemStack(CollectionsKt.elementAt(HexItems.DYE_PIGMENTS.values(),
                                    world.random.nextInt(HexItems.DYE_PIGMENTS.size()))),
                            Util.NIL_UUID
                    ));
                    ComponentKey<BoolComponent> decorative = OneironautComponents.WISP_DECORATIVE;
                    decorative.get(wisp).setValue(true);
                    world.addFreshEntity(wisp);
                }
            }
        }
    }
}
