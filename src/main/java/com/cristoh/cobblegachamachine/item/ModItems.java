package com.cristoh.cobblegachamachine.item;

import com.cristoh.cobblegachamachine.CobbleGachaMachine;
import com.cristoh.cobblegachamachine.item.custom.CapsuleItem;
import com.cristoh.cobblegachamachine.item.custom.CapsuleRarity;
import com.cristoh.cobblegachamachine.item.custom.InvisibleItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;


import java.util.List;

public class ModItems {

    public static Item NORMAL_CAPSULE;
    public static Item RARE_CAPSULE;
    public static Item ULTRA_RARE_CAPSULE;
    public static Item LEGENDARY_CAPSULE;
    public static Item INVISIBLE_TOOLTIP;

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(CobbleGachaMachine.MOD_ID, name), item);
    }

    public static void register() {
        CobbleGachaMachine.LOGGER.info("Registering custom items for " + CobbleGachaMachine.MOD_ID);

        NORMAL_CAPSULE = registerItem("normal_capsule", new CapsuleItem(new Item.Settings(),
                CapsuleRarity.NORMAL));
        RARE_CAPSULE = registerItem("rare_capsule", new CapsuleItem(new Item.Settings(),
                CapsuleRarity.RARE));
        ULTRA_RARE_CAPSULE = registerItem("ultra_rare_capsule", new CapsuleItem(new Item.Settings(),
                CapsuleRarity.ULTRA_RARE));
        LEGENDARY_CAPSULE = registerItem("legendary_capsule", new CapsuleItem(new Item.Settings(),
                CapsuleRarity.LEGENDARY));
        INVISIBLE_TOOLTIP = registerItem("invisible_tooltip", new InvisibleItem(new Item.Settings()));

    }
}
