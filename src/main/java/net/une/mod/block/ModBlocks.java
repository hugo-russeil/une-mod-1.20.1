package net.une.mod.block;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.item.*;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.une.mod.UNEMod;
import net.une.mod.block.custom.CrusherBlock;
import net.une.mod.block.custom.FilterBlock;
import net.minecraft.block.*;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
public class ModBlocks {

    public static final Block CRUSHER = registerBlock("crusher",
            new CrusherBlock(FabricBlockSettings.copyOf(Blocks.STONE)
                    .requiresTool().strength(3.5F, 3.5F)));

    public static final Block FILTER = registerBlock("filter",
            new FilterBlock(FabricBlockSettings.copyOf(Blocks.STONE)
                    .requiresTool().strength(3.5F, 3.5F)));

    private static void addItemsToFunctionnalItemGroup(FabricItemGroupEntries entries) {
        entries.addAfter(Items.STONECUTTER, CRUSHER); // Crusher is added right after the Stonecutter, it sounds logical to me
        entries.addAfter(Items.STONECUTTER, FILTER);
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(UNEMod.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(UNEMod.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        UNEMod.LOGGER.info("Registering ModBlocks for " + UNEMod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModBlocks::addItemsToFunctionnalItemGroup);
    }
}
