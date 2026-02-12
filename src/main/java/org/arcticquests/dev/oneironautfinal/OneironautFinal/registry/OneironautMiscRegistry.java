package org.arcticquests.dev.oneironautfinal.OneironautFinal.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.material.Fluid;

public class OneironautMiscRegistry {
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(Oneironaut.MOD_ID, Registries.FLUID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Oneironaut.MOD_ID, Registries.MOB_EFFECT);
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(Oneironaut.MOD_ID, Registries.ENCHANTMENT);


    //I will not scream at my computer over this

    public static void init() {
        FLUIDS.register();
        EFFECTS.register();
        ENCHANTMENTS.register();
    }

    public static final RegistrySupplier<DetectionResistEffect> DETECTION_RESISTANCE = EFFECTS.register("detection_resistance", DetectionResistEffect::new);
    public static final RegistrySupplier<GlowingAmbitEffect> NOT_MISSING = EFFECTS.register("not_missing", GlowingAmbitEffect::new);
    public static final RegistrySupplier<MonkfruitDelayEffect> RUMINATION = EFFECTS.register("rumination", MonkfruitDelayEffect::new);
    public static final RegistrySupplier<MediaDisintegrationEffect> DISINTEGRATION = EFFECTS.register("disintegration", MediaDisintegrationEffect::new);
    public static final RegistrySupplier<DisintegrationProtectionEffect> DISINTEGRATION_PROTECTION = EFFECTS.register("disintegration_protection", DisintegrationProtectionEffect::new);

    public static final RegistrySupplier<ThoughtSlurry> THOUGHT_SLURRY = FLUIDS.register("thought_slurry", () -> ThoughtSlurry.STILL_FLUID /*new ThoughtSlurry.Still(OneironautThingRegistry.THOUGHT_SLURRY_ATTRIBUTES)*/);
    public static final RegistrySupplier<ThoughtSlurry> THOUGHT_SLURRY_FLOWING = FLUIDS.register("thought_slurry_flowing", () -> ThoughtSlurry.FLOWING_FLUID /*new ThoughtSlurry.Flowing(OneironautThingRegistry.THOUGHT_SLURRY_ATTRIBUTES)*/);

    public static final RegistrySupplier<OvercastDamageEnchant> OVERCAST_DAMAGE_ENCHANT = ENCHANTMENTS.register("overcast_damage", OvercastDamageEnchant::new);
}
