package org.arcticquests.dev.oneironautfinal.OneironautFinal.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class SortOfImmutableItemSettings extends Item.Properties {


    public SortOfImmutableItemSettings() {
        super();
    }

    public SortOfImmutableItemSettings tab(CreativeModeTab group) {
        this.tab(group);
        return this;
    }
}