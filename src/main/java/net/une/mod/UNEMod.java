package net.une.mod;

import net.fabricmc.api.ModInitializer;

import net.une.mod.block.ModBlocks;
import net.une.mod.block.entity.ModBlockEntities;
import net.une.mod.item.ModItems;
import net.une.mod.screen.ModScreenHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNEMod implements ModInitializer {
	public static final String MOD_ID = "une-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger("une-mod");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntities.registerModBlockEntities();
		ModScreenHandlers.registerModScreenHandlers();
	}
}