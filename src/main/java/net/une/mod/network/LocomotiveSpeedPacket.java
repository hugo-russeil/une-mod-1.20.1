package net.une.mod.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import java.util.UUID;

public class LocomotiveSpeedPacket {
    public static final Identifier ID = new Identifier("une-mod", "locomotive_speed");
    private final int speedIndex;
    private final UUID entityId;

    public LocomotiveSpeedPacket(int speedIndex, UUID entityId) {
        this.speedIndex = speedIndex;
        this.entityId = entityId;
    }

    public int getSpeedIndex() {
        return speedIndex;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void write(PacketByteBuf buf) {
        buf.writeInt(speedIndex);
        buf.writeUuid(entityId);
    }

    public static LocomotiveSpeedPacket read(PacketByteBuf buf) {
        return new LocomotiveSpeedPacket(buf.readInt(), buf.readUuid());
    }
}