package net.une.mod;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.une.mod.screen.CrushingScreen;
import net.une.mod.screen.ModScreenHandlers;

public class UNEModClient implements ClientModInitializer{
    @Override
    public void onInitializeClient() {

        HandledScreens.register(ModScreenHandlers.CRUSHING_SCREEN_HANDLER, CrushingScreen::new);

    }
}
