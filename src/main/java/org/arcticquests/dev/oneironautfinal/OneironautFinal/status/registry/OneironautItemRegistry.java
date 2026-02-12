package org.arcticquests.dev.oneironautfinal.OneironautFinal.status.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.Oneironautfinal;

public class OneironautItemRegistry {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Oneironautfinal.MODID);



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
