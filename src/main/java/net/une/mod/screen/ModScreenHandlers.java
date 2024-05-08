package net.une.mod.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandlerType;
import net.une.mod.UNEMod;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<CrushingScreenHandler> CRUSHING_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(UNEMod.MOD_ID, "crushing"),
                    new ExtendedScreenHandlerType<>(CrushingScreenHandler::new));

    public static final ScreenHandlerType<FilterScreenHandler> FILTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(UNEMod.MOD_ID, "filter"),
                    new ExtendedScreenHandlerType<>(FilterScreenHandler::new));

    public static void registerModScreenHandlers() {
            UNEMod.LOGGER.info("Registering ModScreenHandlers for " + UNEMod.MOD_ID);
        }
}
