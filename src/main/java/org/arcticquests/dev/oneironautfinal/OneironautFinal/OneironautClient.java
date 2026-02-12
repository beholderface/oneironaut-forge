// Java
package org.arcticquests.dev.oneironautfinal.OneironautFinal;

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry;
import at.petrak.hexcasting.common.items.ItemStaff;
import at.petrak.hexcasting.common.items.magic.ItemPackagedHex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.common.MinecraftForge;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.block.InactiveSlipwayBlock;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.item.ReverberationRod;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.registry.OneironautItemRegistry;

import java.util.*;


@Mod.EventBusSubscriber(modid = Oneironautfinal.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OneironautClient {

    private static int applyBlockRenderLayers(Collection<Block> blocks, RenderType layer){
        int applied = 0;
        for (Block block : blocks){
            RenderTypeLookup.setRenderLayer(block, layer);
            applied++;
        }
        return applied;
    }

    public static long lastShiftingHoverTick = 0L;
    public static ItemStack lastHoveredShifting = null;
    private static float processObservationPredicate(ItemStack stack, ClientLevel world, LivingEntity holder, int holderID){
        LocalPlayer cachedPlayer = cachedClient.player;
        final float OFF = 0.99f;
        final float ON = -0.01f;
        float output = ON;
        int fov = cachedClient.options.fov().get();
        double threshold = fov / (fov <= 85 ? 90.0 : 100.0);
        if (cachedPlayer != null){
            if (stack.isFramed()){
                assert stack.getFrame() != null;
                if (MiscAPIKt.vecProximity(stack.getFrame().position().subtract(cachedPlayer.getEyePosition()), cachedPlayer.getLookAngle()) <= threshold) {
                    output = OFF;
                }
            }
            if (stack.getEntityRepresentation() != null && stack.getEntityRepresentation() != cachedPlayer){
                Vec3 holderCenterApprox = stack.getEntityRepresentation().position().add(stack.getEntityRepresentation().getEyePosition()).scale(0.5);
                if (MiscAPIKt.vecProximity(holderCenterApprox.subtract(cachedPlayer.getEyePosition()), cachedPlayer.getLookAngle()) <= threshold) {
                    output = OFF;
                }
            }
            if (holder == cachedPlayer && (holder.getItemInHand(InteractionHand.MAIN_HAND) == stack || holder.getItemInHand(InteractionHand.OFF_HAND) == stack)){
                output = OFF;
            }
            if (cachedPlayer.containerMenu.getCarried() == stack ||
                    (lastShiftingHoverTick + 1 >= cachedPlayer.level().getGameTime() && lastHoveredShifting == stack)){
                output = OFF;
            }
        }
        if (!cachedClient.isWindowActive()){
            output = ON;
        }
        return output;
    }

    //private static ClientPlayerEntity cachedPlayer = null;
    private static Minecraft cachedClient = null;
    public static Minecraft getCachedClient(){
        return cachedClient;
    }

    // This method is called during FMLClientSetupEvent via the @SubscribeEvent below.
    private static void doClientRegistration() {

        // Note: Forge fluid client registration differs from Fabric.
        // TODO: Register ThoughtSlurry fluid client properties via IClientFluidTypeExtensions for Forge.
        // For now, make sure fluid blocks use translucent render type if applicable:
        // (If ThoughtSlurry has a Block, set its render layer. If only Fluid types, use IClientFluidTypeExtensions.)
        try {
            // Example placeholder if you have fluid _blocks_:
            // RenderTypeLookup.setRenderLayer(OneironautBlockRegistry.THOUGHT_SLURRY_BLOCK.get(), RenderType.translucent());
            // If not, implement IClientFluidTypeExtensions registration for ThoughtSlurry.STILL_FLUID / FLOWING_FLUID.
        } catch (Exception ignored){}

        ScryingLensOverlayRegistry.addDisplayer(OneironautBlockRegistry.WISP_BATTERY.get(),
                WispBatteryEntity::applyScryingLensOverlay
        );

        List<RegistrySupplierShim<ConceptModifierBlock>> conceptModifiers = List.of(
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_GRIDSIZE),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_ANTIEROSION),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_MAXHEALTH),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_GTP_DROP),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_REFERENCE_COMPARISON),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_REFERENCE_FALSY),
                new RegistrySupplierShim<>(OneironautBlockRegistry.CONCEPT_MODIFIER_STACK_SIZE)
        );

        for (RegistrySupplierShim<ConceptModifierBlock> supplier : conceptModifiers){
            ScryingLensOverlayRegistry.addDisplayer(supplier.get(), ConceptModifierBlockEntity::applyScryingLensOverlay);
        }
        ScryingLensOverlayRegistry.addDisplayer(OneironautBlockRegistry.CONCEPT_CORE.get(), ConceptCoreBlockEntity::applyScryingLensOverlay);

        List<Block> cutoutBlocks = new ArrayList<>(List.of(
                OneironautBlockRegistry.WISP_LANTERN.get(), OneironautBlockRegistry.WISP_LANTERN_TINTED.get(),
                OneironautBlockRegistry.WISP_BATTERY.get(), OneironautBlockRegistry.WISP_BATTERY_DECORATIVE.get(),
                OneironautBlockRegistry.CIRCLE.get(), OneironautBlockRegistry.PSEUDOAMETHYST_CLUSTER.get(), OneironautBlockRegistry.PSEUDOAMETHYST_BUD_LARGE.get(),
                OneironautBlockRegistry.PSEUDOAMETHYST_BUD_MEDIUM.get(), OneironautBlockRegistry.PSEUDOAMETHYST_BUD_SMALL.get(),
                OneironautBlockRegistry.RENDER_BUSH.get(), OneironautBlockRegistry.DEEP_NOOSPHERE_FLOOR.get(),
                OneironautBlockRegistry.CONCEPT_MODIFIER_REFERENCE_FALSY.get(), OneironautBlockRegistry.CONCEPT_MODIFIER_GRIDSIZE.get(), OneironautBlockRegistry.CONCEPT_MODIFIER_EMPTY.get(),
                OneironautBlockRegistry.CONCEPT_MODIFIER_SUS.get(), OneironautBlockRegistry.CONCEPT_MODIFIER_ANTIEROSION.get(), OneironautBlockRegistry.CONCEPT_MODIFIER_MAXHEALTH.get(),
                OneironautBlockRegistry.CONCEPT_MODIFIER_GTP_DROP.get(), OneironautBlockRegistry.CONCEPT_MODIFIER_REFERENCE_COMPARISON.get(),
                OneironautBlockRegistry.CONCEPT_MODIFIER_STACK_SIZE.get()
        ));
        for (RegistrySupplierShim<ConceptDecoratorBlock> supplier : OneironautBlockRegistryShim.COLORFUL_CONCEPT_MODIFIERS().values()){
            cutoutBlocks.add(supplier.get());
        }
        Block[] translucentBlocks = {OneironautBlockRegistry.RAYCAST_BLOCKER_GLASS.get(), OneironautBlockRegistry.MEDIA_GEL.get(),
                OneironautBlockRegistry.CELL.get()};

        // If ThoughtSlurry is represented by blocks or block states, ensure translucency:
        // BlockRender for fluids handled in TODO above.

        Oneironautfinal.LOGGER.info("Applied cutout layer to " + applyBlockRenderLayers(cutoutBlocks, RenderType.cutout()) + " blocks");
        Oneironautfinal.LOGGER.info("Applied translucent layer to " + applyBlockRenderLayers(List.of(translucentBlocks), RenderType.translucent()) + " blocks");

        Oneironautfinal.LOGGER.info("Registering client-side hoverlift processor.");

        // Client tick registration via Forge event bus
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ClientTickEvent event) -> {
            if (event.phase != TickEvent.Phase.END) return;
            Minecraft client = Minecraft.getInstance();
            try {
                HoverElevatorBlockEntity.processHover(false, client.level != null ? client.level.getGameTime() : -1L);
            } catch (ConcurrentModificationException exception){
                Oneironautfinal.LOGGER.error("Oopsie client-side hoverlift exception " + exception.getMessage());
            }
        });

        // Cache client
        cachedClient = Minecraft.getInstance();
        if (cachedClient != null){
            Oneironautfinal.LOGGER.info("Cached client object. Player:" + cachedClient.player);
        } else {
            Oneironautfinal.LOGGER.info("Could not cache client object.");
        }
        InactiveSlipwayBlock.init();

        // Register dimension effects into client map
        try {
            DimensionSpecialEffects.EFFECTS.put(Oneironautfinal.id("noosphere"), new NoosphereDimensionEffects());
            DimensionSpecialEffects.EFFECTS.put(Oneironautfinal.id("deep_noosphere"), new DeepNoosphereDimensionEffects());
        } catch (Exception e) {
            Oneironautfinal.LOGGER.warn("Failed to register dimension effects: " + e.getMessage());
        }

        // Item property registrations (use net.minecraft.client.renderer.item.ItemProperties)
        ItemPackagedHex[] castingItems = {OneironautItemRegistry.REVERBERATION_ROD.get(), OneironautItemRegistry.BOTTOMLESS_CASTING_ITEM.get()};
        for (ItemPackagedHex item : castingItems){
            ItemProperties.register(item, ItemPackagedHex.HAS_PATTERNS_PRED, (stack, world, holder, holderID) -> {
                return item.hasHex(stack) ? 0.99f : -0.01f;
            });
        }

        ItemProperties.register(OneironautItemRegistry.REVERBERATION_ROD.get(), ReverberationRod.CASTING_PREDICATE, (stack, world, holder, holderID) -> {
            if (holder != null){
                return holder.getUseItem().equals(stack) ? 0.99f : -0.01f;
            } else {
                return -0.01f;
            }
        });

        ItemProperties.register(OneironautItemRegistry.WISP_CAPTURE_ITEM.get(), WispCaptureItem.FILLED_PREDICATE, (stack, world, holder, holderID) -> {
            return ((WispCaptureItem)stack.getItem()).hasWisp(stack, world) ? 0.99f : -0.01f;
        });

        ItemProperties.register(OneironautItemRegistry.SHIFTING_PSEUDOAMETHYST.get(), new ResourceLocation(Oneironautfinal.MOD_ID, "observation"),
                OneironautClient::processObservationPredicate);
        ItemProperties.register(OneironautItemRegistry.LIBRARY_CARD.get(), new ResourceLocation(Oneironautfinal.MOD_ID, "written"), (stack, world, holder, holderID) -> {
            return ((ItemLibraryCard)stack.getItem()).getDimension(stack) != null ? 0.99f : -0.01f;
        });

        Item[] nameSensitiveStaves = {OneironautItemRegistry.ECHO_STAFF.get(), OneironautItemRegistry.BEACON_STAFF.get(), OneironautItemRegistry.SPOON_STAFF.get()};
        for (Item staff: nameSensitiveStaves) {
            ItemProperties.register(staff, ItemStaff.FUNNY_LEVEL_PREDICATE, (stack, level, holder, holderID) -> {
                if (!stack.hasCustomHoverName()) {
                    return 0;
                }
                var name = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
                if (name.contains("old")) {
                    return 1f;
                } else if (name.contains("wand of the forest")) {
                    return 2f;
                } else {
                    return 0f;
                }
            });
        }
    }

    // Mod event bus listener for client setup
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        doClientRegistration();
    }

    public static boolean isWorldClientNoosphere(Level world){
        if (world instanceof ClientLevel clientWorld){
            return clientWorld.effects().getClass() == NoosphereDimensionEffects.class
                    || clientWorld.effects().getClass() == DeepNoosphereDimensionEffects.class;
        }
        return false;
    }
    public static boolean isWorldClientDeepNoosphere(Level world){
        if (world instanceof ClientLevel clientWorld){
            return clientWorld.effects().getClass() == DeepNoosphereDimensionEffects.class;
        }
        return false;
    }

    // ---- Small shims/adapters used to avoid Architectury RegistrySupplier direct dependency in this file ----
    // These are tiny wrappers to access existing RegistrySupplier-like objects from the mod registries.
    private static class RegistrySupplierShim<T> {
        private final net.minecraft.core.RegistryObject<T> raw; // placeholder type, adapt if your registry type differs
        public RegistrySupplierShim(net.minecraft.core.RegistryObject<T> raw){
            this.raw = raw;
        }
        public T get(){ return raw.get(); }
    }

    // Adapter to iterate over the colorful modifier registry (replace with real access to your registry map)
    private static class OneironautBlockRegistryShim {
        public static Map<Integer, RegistrySupplierShim<ConceptDecoratorBlock>> COLORFUL_CONCEPT_MODIFIERS() {
            // TODO: Return a real view over OneironautBlockRegistry.COLORFUL_CONCEPT_MODIFIERS
            return Collections.emptyMap();
        }
    }
}