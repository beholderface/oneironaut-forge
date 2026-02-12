package org.arcticquests.dev.oneironautfinal.OneironautFinal.registry;

import at.petrak.hexcasting.api.casting.iota.BooleanIota;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.common.items.ItemStaff;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.arcticquests.dev.oneironautfinal.OneironautFinal.Oneironautfinal;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.item.*;

import java.util.HashMap;
import java.util.Map;

public class OneironautItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Oneironautfinal.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ForgeRegistries.CREAT, Oneironaut.MOD_ID);

    // Creative tab registration
    public static final RegistryObject<CreativeModeTab> ONEIRONAUT_GROUP = TABS.register("oneironaut", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.oneironaut.oneironaut"))
                    .icon(() -> new ItemStack(PSUEDOAMETHYST_SHARD.get()))
                    .build());

    private static Item.Properties prop64() { return new Item.Properties().stacksTo(64).tab(ONEIRONAUT_GROUP); }
    private static Item.Properties prop64_noTab() { return new Item.Properties().stacksTo(64); }
    private static Item.Properties prop16() { return new Item.Properties().stacksTo(16).tab(ONEIRONAUT_GROUP); }
    private static Item.Properties prop1() { return new Item.Properties().stacksTo(1).tab(ONEIRONAUT_GROUP); }
    private static Item.Properties prop1_1024() { return new Item.Properties().stacksTo(1).durability(1024).tab(ONEIRONAUT_GROUP); }

    public static final RegistryObject<ItemStolenMediaProvider> PSUEDOAMETHYST_SHARD = ITEMS.register("pseudoamethyst_shard",
            () -> new ItemStolenMediaProvider(prop64(), (int) (MediaConstants.SHARD_UNIT * 1.5), 1500));
    public static final RegistryObject<ShiftingPseudoamethystItem> SHIFTING_PSEUDOAMETHYST = ITEMS.register("shifting_pseudoamethyst",
            () -> new ShiftingPseudoamethystItem(prop64().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<ArchitecturyBucketItem> THOUGHT_SLURRY_BUCKET = ITEMS.register("thought_slurry_bucket",
            () -> new ArchitecturyBucketItem(OneironautMiscRegistry.THOUGHT_SLURRY, prop1()));
    public static final RegistryObject<ReverberationRod> REVERBERATION_ROD = ITEMS.register("reverberation_rod",
            () -> new ReverberationRod(prop1()));
    public static final RegistryObject<BottomlessMediaItem> BOTTOMLESS_MEDIA_ITEM = ITEMS.register("endless_phial",
            () -> new BottomlessMediaItem(prop1()));
    public static final RegistryObject<BottomlessCastingItem> BOTTOMLESS_CASTING_ITEM = ITEMS.register("bottomless_trinket",
            () -> new BottomlessCastingItem(prop1()));
    public static final RegistryObject<ItemStaff> ECHO_STAFF = ITEMS.register("echo_staff",
            () -> new GeneralNoisyStaff(prop1(), SoundEvents.SCULK_CLICKING, SoundEvents.SCULK_SHRIEKER_SHRIEK, null));
    public static final RegistryObject<ItemStaff> BEACON_STAFF = ITEMS.register("beacon_staff",
            () -> new GeneralNoisyStaff(prop1(), SoundEvents.BEACON_ACTIVATE, SoundEvents.BEACON_DEACTIVATE, null));
    public static final RegistryObject<ShovelItem> SPOON_STAFF = ITEMS.register("spoon_staff",
            () -> new ShovelItem(Tiers.IRON, 1.5F, -3.0F, prop1_1024()));
    public static final RegistryObject<GeneralPigmentItem> PIGMENT_NOOSPHERE = ITEMS.register("pigment_noosphere",
            () -> new GeneralPigmentItem(prop64(), GeneralPigmentItem.colors_noosphere));
    public static final RegistryObject<GeneralPigmentItem> PIGMENT_FLAME = ITEMS.register("pigment_flame",
            () -> new GeneralPigmentItem(prop64(), GeneralPigmentItem.colors_flame));
    public static final RegistryObject<GeneralPigmentItem> PIGMENT_ECHO = ITEMS.register("pigment_echo",
            () -> new GeneralPigmentItem(prop64(), GeneralPigmentItem.colors_echo));
    public static final RegistryObject<GeneralPigmentItem> PIGMENT_FRENZY = ITEMS.register("pigment_frenzyflame",
            () -> new GeneralPigmentItem(prop64(), GeneralPigmentItem.colors_frenzy));
    // The rest follows the same pattern. Continue on your own for the rest of your statics.
    // For BlockItems:
    public static final RegistryObject<BlockItem> PSUEDOAMETHYST_BLOCK_ITEM = ITEMS.register("pseudoamethyst_block",
            () -> new BlockItem(OneironautBlockRegistry.PSUEDOAMETHYST_BLOCK.get(), prop64()));
    public static final RegistryObject<BlockItem> SUPER_BUDDING_ITEM = ITEMS.register("super_budding",
            () -> new BlockItem(OneironautBlockRegistry.SUPER_BUDDING.get(), prop64()));
    // ...Repeat all block item registrations...

    // If you use dyed block items, this is the Forge idiom:
    public static final Map<DyeColor, RegistryObject<BlockItem>> COLORFUL_CONCEPT_MODIFIERS = new HashMap<>();
    static {
        for (DyeColor color : DyeColor.values()){
            RegistryObject<BlockItem> supplier = ITEMS.register("concept_decorator_color_" + color.getName(),
                    () -> new BlockItem(OneironautBlockRegistry.COLORFUL_CONCEPT_MODIFIERS.get(color).get(), prop64()));
            COLORFUL_CONCEPT_MODIFIERS.put(color, supplier);
        }
    }

    // FoodProperties (can stay the same)
    public static final FoodProperties MONKFRUIT_FOOD = (new FoodProperties.Builder()).nutrition(4).saturationMod(0.6F).fast().alwaysEat().build();
    public static final FoodProperties MONKFRUIT_FOOD_COOKED = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.8F).alwaysEat().build();
    public static final FoodProperties MONKFRUIT_FOOD_JAM = (new FoodProperties.Builder()).nutrition(6).saturationMod(1.0F).alwaysEat().build();

    public static final RegistryObject<MonkfruitItem> MONKFRUIT = ITEMS.register("monkfruit",
            () -> new MonkfruitItem(
                    OneironautBlockRegistry.RENDER_BUSH.get(),
                    prop64().food(MONKFRUIT_FOOD)));
    public static final RegistryObject<MonkfruitItemCooked> MONKFRUIT_COOKED = ITEMS.register("monkfruit_cooked",
            () -> new MonkfruitItemCooked(
                    prop64().food(MONKFRUIT_FOOD_COOKED)));
    public static final RegistryObject<MonkfruitItemJam> MONKFRUIT_JAM = ITEMS.register("hexjam",
            () -> new MonkfruitItemJam(
                    prop64().food(MONKFRUIT_FOOD_JAM)));

    // Don't forget to add your DeferredRegister to the mod event bus (typically in your mod main class):
    // OneironautItemRegistry.ITEMS.register(modEventBus);
    // OneironautItemRegistry.TABS.register(modEventBus);
}