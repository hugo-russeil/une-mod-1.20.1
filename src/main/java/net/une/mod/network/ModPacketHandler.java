package net.une.mod.network;

import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.une.mod.entity.LocomotiveEntity;

public class ModPacketHandler {
    public static void register() {
        ServerSidePacketRegistry.INSTANCE.register(LocomotiveSpeedPacket.ID, (PacketContext context, PacketByteBuf buf) -> {
            LocomotiveSpeedPacket packet = LocomotiveSpeedPacket.read(buf);
            context.getTaskQueue().execute(() -> {
                // Get the locomotive entity and update its speed
                World world = context.getPlayer().getWorld();
                if (world instanceof ServerWorld) {
                    LocomotiveEntity locomotive = (LocomotiveEntity) ((ServerWorld) world).getEntity(packet.getEntityId());
                    if (locomotive != null) {
                        locomotive.setSpeedIndex(packet.getSpeedIndex());
                    }
                }
            });
        });
    }
}