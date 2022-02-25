package coda.blackholeblock.registry;

import coda.blackholeblock.BlackHoleMod;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class BHTags {
    public static final Tag.Named<Item> BLACKLIST = itemTag("blackhole_blacklist");

    private static Tag.Named<Item> itemTag(String path) {
        return ItemTags.bind(BlackHoleMod.MOD_ID + ":" + path);
    }
}
