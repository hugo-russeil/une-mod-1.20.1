package net.une.mod;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNEMod implements ModInitializer {
	public static final String MODID = "une-mod";
    public static final Logger LOGGER = LoggerFactory.getLogger("une-mod");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}