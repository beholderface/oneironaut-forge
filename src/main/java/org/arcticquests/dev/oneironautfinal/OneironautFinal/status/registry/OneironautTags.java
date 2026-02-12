package org.arcticquests.dev.oneironautfinal.OneironautFinal.status.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.MiscAPI;

public class OneironautTags {
    public static class Blocks {
        public static final TagKey<Block> growsMonkfruit = MiscAPI.getBlockTagKey("oneironautfinal:growsmonkfruit");
        public static final TagKey<Block> erosionImmune = MiscAPI.getBlockTagKey("oneironautfinal:deeperosionimmune");
        public static final TagKey<Block> breakImmune = MiscAPI.getBlockTagKey("oneironautfinal:hexbreakimmune");
        public static final TagKey<Block> blocksRaycast = MiscAPI.getBlockTagKey("oneironautfinal:blocksraycast");
    }
    public static class Entities {
        public static final TagKey<EntityType<?>> impulseRedirectBlacklist = MiscAPI.getEntityTagKey("oneironautfinal:impulse_redirect_blacklist");
        public static final TagKey<EntityType<?>> livingInterchangeWhitelist = MiscAPI.getEntityTagKey("oneironautfinal:living_interchange_whitelist");
        public static final TagKey<EntityType<?>> monkfruitBlacklist = MiscAPI.getEntityTagKey("oneironautfinal:monkfruit_blacklist");
        public static final TagKey<EntityType<?>> mindRenderAutospare = MiscAPI.getEntityTagKey("oneironautfinal:render_autospare");
        public static final TagKey<EntityType<?>> mindRenderFlayBlacklist = MiscAPI.getEntityTagKey("oneironautfinal:render_flay_blacklist");
        public static final TagKey<EntityType<?>> mindRenderFlayWhitelist = MiscAPI.getEntityTagKey("oneironautfinal:render_flay_whitelist");
        public static final TagKey<EntityType<?>> ideaUnstorable = MiscAPI.getEntityTagKey("oneironautfinal:unstorable");
    }
    public static class Items {
        public static final TagKey<Item> datapackStaves = MiscAPI.getItemTagKey("oneironautfinal:datapack_staves");
    }
}
