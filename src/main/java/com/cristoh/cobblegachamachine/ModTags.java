package com.cristoh.cobblegachamachine;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {

        public static final TagKey<Item> COIN_ITEMS = createTag("coin_items");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM,  Identifier.of(CobbleGachaMachine.MOD_ID, name));
        }
    }
}