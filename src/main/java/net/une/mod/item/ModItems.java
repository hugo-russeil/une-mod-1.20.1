package net.une.mod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.une.mod.UNEMod;
import net.une.mod.item.custom.SaltItem;
import net.une.mod.item.custom.BrinePotionItem;
import net.une.mod.item.custom.BrineSplashPotionItem;

public class ModItems {
    public static final Item SALT = registerItem("salt", new SaltItem(new FabricItemSettings()));
    public static final Item BRINE_POTION_ITEM = registerItem("brine_potion", new BrinePotionItem(new FabricItemSettings()));
    public static final Item BRINE_SPLASH_POTION_ITEM = registerItem("brine_splash_potion", new BrineSplashPotionItem(new FabricItemSettings()));

    public static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(SALT);
        //entries.add(BRINE_POTION_ITEM); Commented out because it's useless for now
        entries.add(BRINE_SPLASH_POTION_ITEM);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(UNEMod.MOD_ID, name), item);
    }

    public static void registerModItems() {
        UNEMod.LOGGER.info("Registering ModItems for " + UNEMod.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }

}
