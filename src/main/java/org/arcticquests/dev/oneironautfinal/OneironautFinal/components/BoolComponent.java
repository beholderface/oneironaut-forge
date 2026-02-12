package org.arcticquests.dev.oneironautfinal.OneironautFinal.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class BoolComponent implements AutoSyncedComponent {
    private boolean value = false;
    private Entity entity;

    public BoolComponent(Entity e){
        this.entity = e;
    }
    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean newValue) {
        this.value = newValue;
        //DoubleComponent.VOLUME.sync(this.entity);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.value = tag.getBoolean("value");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("value", this.value);
    }

}
