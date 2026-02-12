package org.arcticquests.dev.oneironautfinal.OneironautFinal.status.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.Oneironautfinal;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.block.CellBlock;

import java.util.function.Supplier;

public class OneironautBlockRegistry {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Oneironautfinal.MODID);


    public static final RegistryObject<CellBlock> CELL = registerBlockOnly("cell", ()-> new CellBlock(BlockBehaviour.Properties.copy(Blocks.SLIME_BLOCK)
            .speedFactor(0.6f).jumpFactor(0.75f).mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.SLIME_BLOCK).noOcclusion().destroyTime(Blocks.SOUL_SAND.defaultDestroyTime())
    ));


    private static <T extends Block> RegistryObject<T> registerBlockOnly(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return OneironautItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
