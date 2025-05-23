package net.une.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.une.mod.entity.ModEntities;
import net.une.mod.item.ModItems;
import net.une.mod.screen.CrushingScreen;
import net.une.mod.screen.ModScreenHandlers;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;


public class UNEModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.CRUSHING_SCREEN_HANDLER, CrushingScreen::new);

        EntityRendererRegistry.INSTANCE.register(ModEntities.LOCOMOTIVE_ENTITY,
                (context) -> new MinecartEntityRenderer<>(context, EntityModelLayers.MINECART));

        EntityRendererRegistry.INSTANCE.register(
                ModEntities.BALL_ENTITY,
                FlyingItemEntityRenderer::new // single‐arg ctor uses default scale/lit
        );
    }
}