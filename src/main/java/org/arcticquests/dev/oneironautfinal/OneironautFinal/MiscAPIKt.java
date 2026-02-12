package org.arcticquests.dev.oneironautfinal.OneironautFinal;

import at.petrak.hexcasting.api.HexAPI;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.iota.EntityIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.api.casting.mishaps.MishapLocationInWrongDimension;
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs;
import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.conceptmodification.ConceptModifier;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.conceptmodification.ConceptModifierManager;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.environments.ReverbRodCastEnv;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.idea.IdeaKeyable;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.iotatypes.DimIota;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.iotatypes.SoulprintIota;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.mixin.GeneralCastEnvInvoker;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.mixin.IotaTypeInvoker;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.max;

public class MiscAPIKt {

    // this one isn't used anymore but I'm keeping it just in case
    public static DimIota getDimIota(List<Iota> list, int idx, int argc) {
        Iota x = getOrElse(list, idx);
        if (x instanceof DimIota dim) {
            return dim;
        }
        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : argc - (idx + 1), "oneironaut:imprint");
    }

    public static DimIota getDimIota(List<Iota> list, int idx) {
        return getDimIota(list, idx, 0);
    }

    public static ServerLevel getDimension(List<Iota> list, int idx, int argc, MinecraftServer server) {
        Iota x = getOrElse(list, idx);
        if (x instanceof DimIota dim) {
            ServerLevel world = dim.toWorld(server);
            assert world != null;
            return world;
        }
        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : argc - (idx + 1), "oneironaut:imprint");
    }

    public static ServerLevel getDimension(List<Iota> list, int idx, MinecraftServer server) {
        return getDimension(list, idx, 0, server);
    }

    public static void assertTeleportationAllowed(ServerLevel world) {
        ResourceKey<Level> worldKey = world.dimension();
        if (!HexConfig.server().canTeleportInThisDimension(worldKey)) {
            throw new MishapLocationInWrongDimension(worldKey.location());
        }
    }

    public static UUID getSoulprint(List<Iota> list, int idx, int argc) {
        Iota x = getOrElse(list, idx);
        if (x instanceof SoulprintIota soul) {
            return soul.getEntity();
        }
        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : argc - (idx + 1), "oneironaut:soulprint");
    }

    public static UUID getSoulprint(List<Iota> list, int idx) {
        return getSoulprint(list, idx, 0);
    }

    public static TagKey<Block> getBlockTagKey(ResourceLocation id) {
        return TagKey.create(Registries.BLOCK, id);
    }

    public static TagKey<Block> getBlockTagKey(String id) {
        return getBlockTagKey(new ResourceLocation(id));
    }

    public static TagKey<EntityType<?>> getEntityTagKey(ResourceLocation id) {
        return TagKey.create(Registries.ENTITY_TYPE, id);
    }

    public static TagKey<EntityType<?>> getEntityTagKey(String id) {
        return getEntityTagKey(new ResourceLocation(id));
    }

    public static TagKey<Item> getItemTagKey(ResourceLocation id) {
        return TagKey.create(Registries.ITEM, id);
    }

    public static TagKey<Item> getItemTagKey(String id) {
        return getItemTagKey(new ResourceLocation(id));
    }

    /**
     * @return Triple<resultState, mediaCost, advancement?> – String is currently always null.
     */
    public static Triple<BlockState, Long, String> getInfuseResult(BlockState targetState, Level world) {
        Triple<BlockState, Long, String> conversionResult;

        // at the moment this when thing is just for the wither rose transmutation,
        // since everything without special behavior is now handled in recipe jsons
        if (targetState.getBlock() == Blocks.WITHER_ROSE) {
            Block[] smallFlowers = new Block[]{
                    Blocks.DANDELION, Blocks.POPPY, Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET,
                    Blocks.RED_TULIP, Blocks.ORANGE_TULIP, Blocks.WHITE_TULIP, Blocks.PINK_TULIP,
                    Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY
            };
            int flowerIndex = ThreadLocalRandom.current().nextInt(0, smallFlowers.length);
            conversionResult = new Triple<>(smallFlowers[flowerIndex].defaultBlockState(), 5L, null);
        } else {
            conversionResult = new Triple<>(Blocks.BARRIER.defaultBlockState(), -1L, null);
        }

        boolean debugMessages = false;

        if (conversionResult.second() == -1L) {
            Oneironautfinal.boolLogger("did not find a hard-coded conversion", debugMessages);

            RecipeManager recipeManager = world.getRecipeManager();
            var infusionRecipes = recipeManager.getAllRecipesFor(OneironautRecipeTypes.INFUSION_TYPE);
            var recipe = infusionRecipes.stream()
                    .filter(r -> r.matches(targetState))
                    .findFirst()
                    .orElse(null);

            if (recipe != null) {
                Oneironautfinal.boolLogger(
                        "found a matching recipe, " + recipe.blockIn + " to " + recipe.blockOut.getBlock().getName().getString(),
                        debugMessages
                );
                conversionResult = new Triple<>(recipe.blockOut, recipe.mediaCost, null);
            } else {
                Oneironautfinal.boolLogger("no matching recipe found", debugMessages);
            }
        } else {
            Oneironautfinal.boolLogger("found a hard-coded conversion", debugMessages);
        }

        return new Triple<>(
                preserveStates(targetState, conversionResult.first()),
                conversionResult.second(),
                conversionResult.third()
        );
    }

    public static BlockState preserveStates(BlockState oldState, BlockState desiredState) {
        boolean debugMessages = false;
        BlockState newState = desiredState;

        if (!desiredState.equals(Blocks.BARRIER.defaultBlockState())) {
            List<Property<Boolean>> boolsToKeep =
                    Arrays.asList(BlockStateProperties.WATERLOGGED, BlockStateProperties.HANGING);
            for (Property<Boolean> property : boolsToKeep) {
                if (oldState.hasProperty(property)) {
                    Boolean value = oldState.getValue(property);
                    Oneironautfinal.boolLogger("property " + property.getName() + " has value " + value, debugMessages);
                    newState = newState.setValue(property, value);
                }
            }

            List<Property<Integer>> intsToKeep = Collections.singletonList(BlockStateProperties.ROTATION_16);
            for (Property<Integer> property : intsToKeep) {
                if (oldState.hasProperty(property)) {
                    Integer value = oldState.getValue(property);
                    Oneironautfinal.boolLogger("property " + property.getName() + " has value " + value, debugMessages);
                    newState = newState.setValue(property, value);
                }
            }

            List<Property<Direction>> dirsToKeep =
                    Arrays.asList(BlockStateProperties.FACING, BlockStateProperties.HORIZONTAL_FACING);
            for (Property<Direction> property : dirsToKeep) {
                if (oldState.hasProperty(property)) {
                    Direction value = oldState.getValue(property);
                    Oneironautfinal.boolLogger("property " + property.getName() + " has value " + value, debugMessages);
                    newState = newState.setValue(property, value);
                }
            }
        }
        return newState;
    }

    public static boolean isUnsafe(ServerLevel world, BlockPos pos, boolean up) {
        BlockState state = world.getBlockState(pos);
        boolean output;

        Block block = state.getBlock();
        if (block == Blocks.LAVA ||
                block == Blocks.FIRE ||
                block == Blocks.SOUL_FIRE ||
                block == Blocks.CAMPFIRE ||
                block == Blocks.SOUL_CAMPFIRE ||
                block == Blocks.MAGMA_BLOCK ||
                block == Blocks.CACTUS ||
                block == Blocks.SCULK_SHRIEKER) {
            output = true;
        } else {
            output = false;
        }

        if (state.canOcclude() && up) {
            output = true;
        }
        return output;
    }

    public static boolean isSolid(ServerLevel world, BlockPos pos) {
        boolean output = false;
        BlockState state = world.getBlockState(pos);

        if (state.getFluidState().isEmpty() && !state.isAir() && !state.getBlock().isPossibleToRespawnInThis(state)) {
            output = true;
        } else if (state.getBlock().defaultBlockState().getProperties().contains(BlockStateProperties.WATERLOGGED)
                && !state.isAir()) {
            if (Boolean.TRUE.equals(state.getBlock().defaultBlockState().getValue(BlockStateProperties.WATERLOGGED))) {
                output = true;
            }
        }
        return output;
    }

    public static ServerLevel stringToWorld(String key, MinecraftServer server) {
        ResourceKey<Level> regKey = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(key));
        return server.getLevel(regKey);
    }

    public static ServerPlayer playerUUIDtoServerPlayer(UUID uuid, MinecraftServer server) {
        return server.getPlayerList() != null ? server.getPlayerList().getPlayer(uuid) : null;
    }

    public static Vec3i toVec3i(Vec3 vec) {
        return new Vec3i((int) floor(vec.x), (int) floor(vec.y), (int) floor(vec.z));
    }

    public static BlockPos toBlockPos(Vec3 vec) {
        return new BlockPos((int) floor(vec.x), (int) floor(vec.y), (int) floor(vec.z));
    }

    public static int genCircle(WorldGenLevel world, BlockPos center, int diameter, BlockState state,
                                Block[] replaceable, double fillPortion) {
        Vec3 realCenter = new Vec3(center.getX() + 0.5, center.getY() + 0.5, center.getZ() + 0.5);
        double radius = diameter / 2.0;

        Vec3 corner = realCenter.add(-(radius + 0.5), 0.0, -(radius + 0.5));
        int placed = 0;

        List<Block> replaceableList = Arrays.asList(replaceable);

        for (int x = 0; x <= diameter; x++) {
            for (int y = 0; y <= diameter; y++) {
                Vec3 offset = new Vec3(x, 0.0, y);
                Vec3 current = corner.add(offset);
                if (world.getRandom().nextIntBetweenInclusive(0, 999) / 10.0 <= fillPortion * 100) {
                    BlockPos bp = new BlockPos(toVec3i(current));
                    if (current.distanceTo(realCenter) <= radius &&
                            replaceableList.contains(world.getBlockState(bp).getBlock())) {
                        world.setBlock(bp, state, 0b10);
                        placed++;
                    }
                }
            }
        }
        return placed;
    }

    public static boolean isPlayerEnlightened(ServerPlayer player) {
        var adv = player.getServer()
                .getAdvancements()
                .getAdvancement(HexAPI.modLoc("enlightenment"));
        var advs = player.getAdvancements();
        boolean enlightened;
        if (adv != null && advs.getOrStartProgress(adv) != null) {
            enlightened = advs.getOrStartProgress(adv).isDone();
        } else {
            enlightened = false;
        }
        return enlightened;
    }

    public static boolean isUsingRod(CastingEnvironment env) {
        return env instanceof ReverbRodCastEnv;
    }

    public static List<BlockPos> getPositionsInCuboid(BlockPos corner1, BlockPos corner2, List<BlockPos> pointsToExclude) {
        AABB cuboid = new AABB(corner1, corner2);
        BlockPos lowerCorner = new BlockPos(
                (int) cuboid.minX,
                (int) cuboid.minY,
                (int) cuboid.minZ
        );

        List<BlockPos> outputList = new ArrayList<>();

        int xLength = (int) cuboid.getXsize();
        int yLength = (int) cuboid.getYsize();
        int zLength = (int) cuboid.getZsize();

        for (int i = 0; i <= xLength; i++) {
            for (int j = 0; j <= yLength; j++) {
                for (int k = 0; k <= zLength; k++) {
                    BlockPos currentPos = lowerCorner.offset(i, j, k);
                    if (!pointsToExclude.contains(currentPos)) {
                        outputList.add(currentPos);
                    }
                }
            }
        }
        return Collections.unmodifiableList(outputList);
    }

    public static List<BlockPos> getPositionsInCuboid(BlockPos corner1, BlockPos corner2, BlockPos pointToExclude) {
        return getPositionsInCuboid(corner1, corner2, Collections.singletonList(pointToExclude));
    }

    public static List<BlockPos> getPositionsInCuboid(BlockPos corner1, BlockPos corner2) {
        BlockPos fakeExclude = corner2.offset(abs(corner1.getX() - corner2.getX()) + 20, 0, 0);
        return getPositionsInCuboid(corner1, corner2, Collections.singletonList(fakeExclude));
    }

    public static List<Vec3> corners(AABB box) {
        return Arrays.asList(
                new Vec3(box.minX, box.minY, box.minZ),
                new Vec3(box.maxX, box.minY, box.minZ),
                new Vec3(box.maxX, box.maxY, box.minZ),
                new Vec3(box.maxX, box.maxY, box.maxZ),
                new Vec3(box.minX, box.maxY, box.maxZ),
                new Vec3(box.minX, box.minY, box.maxZ),
                new Vec3(box.maxX, box.minY, box.maxZ),
                new Vec3(box.minX, box.maxY, box.minZ)
        );
    }

    public static double vecProximity(Vec3 a, Vec3 b) {
        // works for hexes; kept from original Kotlin logic
        return a.normalize().subtract(b.normalize()).length();
    }

    public static double vecProximity(Direction a, Vec3 b) {
        return vecProximity(Vec3.atLowerCornerOf(a.getNormal()), b);
    }

    public static void unbrainsweep(Mob patient) {
        assert false;

        if (!patient.level().isClientSide()) {
            IXplatAbstractions.INSTANCE.sendPacketNear(
                    patient.position(),
                    256.0,
                    (ServerLevel) patient.level(),
                    new UnBrainsweepPacket(patient.getId())
            );
        }

        var component = HexCardinalComponents.BRAINSWEPT.get(patient);
        component.setBrainswept(false);
        patient.setNoAi(false);

        var brain = patient.getBrain();
        patient.goalSelector = new GoalSelector(patient.level().getProfilerSupplier());
        brain.useDefaultActivity();
        brain.updateActivityFromSchedule(patient.level().getDayTime(), patient.level().getGameTime());

        if (patient instanceof VillagerDataHolder vdc) {
            VillagerData newData = vdc.getVillagerData()
                    .setLevel(0)
                    .setProfession(VillagerProfession.NITWIT);
            vdc.setVillagerData(newData);
        }

        CompoundTag refreshNBT = patient.saveWithoutId(new CompoundTag());
        patient.load(refreshNBT);
    }

    public static double longestAxisLength(AABB box) {
        double x = box.getXsize();
        double y = box.getYsize();
        double z = box.getZsize();
        if (x >= y && x >= z) {
            return x;
        } else if (y >= x && y >= z) {
            return y;
        } else {
            return z;
        }
    }

    public static boolean intersectsPermissive(AABB box,
                                               double minX, double minY, double minZ,
                                               double maxX, double maxY, double maxZ) {
        return box.minX <= maxX && box.maxX >= minX &&
                box.minY <= maxY && box.maxY >= minY &&
                box.minZ <= maxZ && box.maxZ >= minZ;
    }

    public static boolean intersectsPermissive(AABB a, AABB b) {
        return intersectsPermissive(a, b.minX, b.minY, b.minZ, b.maxX, b.maxY, b.maxZ);
    }

    public static boolean containsPermissive(AABB box, double x, double y, double z) {
        return x >= box.minX && x <= box.maxX &&
                y >= box.minY && y <= box.maxY &&
                z >= box.minZ && z <= box.maxZ;
    }

    public static boolean containsPermissive(AABB box, Vec3 vec) {
        return containsPermissive(box, vec.x, vec.y, vec.z);
    }

    public static double volume(AABB box) {
        return box.getXsize() * box.getYsize() * box.getZsize();
    }

    public static void rawColor(FrozenPigment pigment, float time, Vec3 pos) {
        pigment.getColorProvider().getColor(time, pos);
    }

    public static Entity getNonlivingIfAllowed(List<Iota> list, int idx, int argc) {
        Iota x = getOrElse(list, idx);
        boolean nonlivingAllowed = OneironautConfig.server.planeShiftNonliving;
        if (x instanceof EntityIota ei) {
            Entity e = ei.getEntity();
            if (nonlivingAllowed || (e instanceof LivingEntity && !(e instanceof ArmorStand))) {
                return e;
            }
        }
        String stub = nonlivingAllowed ? "entity" : "entity.living";
        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : argc - (idx + 1), stub);
    }

    public static Entity getNonlivingIfAllowed(List<Iota> list, int idx) {
        return getNonlivingIfAllowed(list, idx, 0);
    }

    public static IdeaKeyable getIdeaKey(List<Iota> list, int idx, int argc, CastingEnvironment env) {
        Iota x = getOrElse(list, idx);
        if (x instanceof IdeaKeyable key && key.isValidKey(env)) {
            return key;
        }
        throw MishapInvalidIota.ofType(x, (argc == 0) ? idx : argc - (idx + 1), "oneironaut:invalidkey");
    }

    public static IdeaKeyable getIdeaKey(List<Iota> list, int idx, CastingEnvironment env) {
        return getIdeaKey(list, idx, 0, env);
    }

    public static UUID toUUID(BlockPos pos) {
        return new UUID(0L, pos.asLong());
    }

    public static BlockPos toBlockPos(UUID uuid) {
        return BlockPos.of(uuid.getLeastSignificantBits());
    }

    public static boolean handleIncreasedStackLimit(
            CastingEnvironment env,
            CastingImage img,
            Iterable<Iota> examinee,
            Operation<Boolean> original
    ) {
        if (env.getCastingEntity() instanceof ServerPlayer player) {
            ConceptModifierManager manager = ConceptModifierManager.getServerState(Oneironaut.getCachedServer());
            if (manager.hasModifierType(player, ConceptModifier.ModifierType.STACK_LIMIT)) {
                int totalSize = 0;
                int modifiedMaximum = HexIotaTypes.MAX_SERIALIZATION_TOTAL * 2;

                for (Iota iota : examinee) {
                    if (IotaTypeInvoker.oneironaut$isTooLarge(Collections.singletonList(iota), 0)) {
                        img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, false);
                        return true;
                    }
                    totalSize += iota.size();
                }

                if (totalSize > modifiedMaximum) {
                    img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, false);
                    return true;
                } else if (totalSize > HexIotaTypes.MAX_SERIALIZATION_TOTAL) {
                    long remainingCost =
                            ((GeneralCastEnvInvoker) env).oneironaut$extractMediaEnvironment(
                                    (long) (totalSize - HexIotaTypes.MAX_SERIALIZATION_TOTAL),
                                    false
                            );

                    if (remainingCost <= 0) {
                        img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, true);
                        return false;
                    } else {
                        img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, false);
                        return true;
                    }
                }

                img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, true);
                return false;
            }
        }

        boolean originalResult = original.call(examinee);
        img.getUserData().putBoolean(MiscStaticData.TAG_ALLOW_SERIALIZE, originalResult);
        return originalResult;
    }

    public static Vec3 toVec3d(DyeColor color) {
        float[] comps = color.getTextureDiffuseColors();
        return new Vec3(comps[0], comps[1], comps[2]);
    }

    public static FrozenPigment colorToClosestPigment(int color) {
        int j = (color & 0xFF0000) >> 16;
        int k = (color & 0xFF00) >> 8;
        int l = (color & 0xFF);
        return colorToClosestPigment(
                new Vec3(
                        j / 255.0f,
                        k / 255.0f,
                        l / 255.0f
                )
        );
    }

    public static FrozenPigment colorToClosestPigment(Vec3 color) {
        double distance = Double.MAX_VALUE;
        DyeColor dye = DyeColor.RED;

        for (DyeColor checked : DyeColor.values()) {
            double current = toVec3d(checked).distanceTo(color);
            if (current < distance) {
                distance = current;
                dye = checked;
            }
        }
        return new FrozenPigment(HexItems.DYE_PIGMENTS.get(dye).getDefaultInstance(), Util.NIL_UUID);
    }

    public static Vec3 scaleBetweenDimensions(Vec3 vec, Level origin, Level destination) {
        double x = vec.x;
        double z = vec.z;
        double compression =
                origin.dimensionType().coordinateScale() / destination.dimensionType().coordinateScale();
        return new Vec3(x * compression, vec.y, z * compression);
    }

    public static Vec3 coerceWithinBorder(Vec3 vec, WorldBorder border) {
        double x = clamp(vec.x, border.getMinX(), border.getMaxX());
        double z = clamp(vec.z, border.getMinZ(), border.getMaxZ());
        return new Vec3(x, vec.y, z);
    }

    public static Vec3 coerceWithinBorder(Vec3 vec, Level world) {
        return coerceWithinBorder(vec, world.getWorldBorder());
    }

    private static <T> T getOrElse(List<T> list, int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new MishapNotEnoughArgs(idx + 1, list.size());
        }
        return list.get(idx);
    }

    private static double clamp(double value, double min, double maxVal) {
        return max(min, Math.min(value, maxVal));
    }

    public static final class MiscStaticData {
        public static final String TAG_ALLOW_SERIALIZE = "serializeAnyway";

        private MiscStaticData() {
        }
    }

    /**
     * Simple generic Triple since Kotlin's Triple doesn't exist in Java.
     */
    public static final class Triple<A, B, C> {
        private final A first;
        private final B second;
        private final C third;

        public Triple(A first, B second, C third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public A first() {
            return first;
        }

        public B second() {
            return second;
        }

        public C third() {
            return third;
        }
    }
}