package net.une.mod.block.entity;

import net.une.mod.UNEMod;
import net.une.mod.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class ModBlockEntities {
    public static final BlockEntityType<CrusherBlockEntity> CRUSHER =
            Registry.register(Registries.BLOCK_ENTITY_TYPE,
            new Identifier(UNEMod.MOD_ID, "crusher_be"),
            FabricBlockEntityTypeBuilder.create(CrusherBlockEntity::new, ModBlocks.CRUSHER).build());

    public static void registerModBlockEntities() {
        UNEMod.LOGGER.info("Registering ModBlockEntities for " + UNEMod.MOD_ID);
    }
}
