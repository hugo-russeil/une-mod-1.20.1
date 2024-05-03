package net.une.mod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.une.mod.UNEMod;
import net.une.mod.block.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup UNE_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(UNEMod.MOD_ID, "UNE"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.une"))
                    .icon(() -> new ItemStack(Items.BLUE_BANNER)).entries((displayContext, entries) -> {
                        entries.add(ModBlocks.CRUSHER);


                    }).build());

    public static void registerModItemGroups() {
        UNEMod.LOGGER.info("Registering ModItemGroups for " + UNEMod.MOD_ID);
    }

}
