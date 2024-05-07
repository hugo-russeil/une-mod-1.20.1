package net.une.mod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.une.mod.UNEMod;
import net.une.mod.entity.BrineSplashPotionEntity;

public class ModEntities {
    public static final EntityType<Entity> BRINE_SPLASH_POTION_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            "brine_splash_potion_entity",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, BrineSplashPotionEntity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).build());

    public static void registerModEntities() {
        UNEMod.LOGGER.info("Registering ModEntities for " + UNEMod.MOD_ID);
    }
}