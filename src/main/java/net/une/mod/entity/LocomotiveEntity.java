package net.une.mod.entity;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.une.mod.network.LocomotiveSpeedPacket;

import java.awt.*;
import java.lang.reflect.Field;

public class LocomotiveEntity extends FurnaceMinecartEntity {
    private static final double[] SPEEDS = {0, 0.5, 1, 2, 3, 4};
    private int speedIndex = 2; // start at regular speed
    private int fuel = 0;

    public LocomotiveEntity(EntityType<? extends FurnaceMinecartEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected double getMaxSpeed() {
        return (this.isTouchingWater() ? 3.0 : 4.0) / 20.0 * SPEEDS[speedIndex];
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if (player.getWorld().isClient()) {
            // Get the key bindings for moving forward and backward
            KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
            KeyBinding backwardKey = MinecraftClient.getInstance().options.backKey;


            // Check if the player is right-clicking while pressing the forward key
            if (forwardKey.isPressed() && hand == Hand.MAIN_HAND) {
                if (speedIndex < SPEEDS.length - 1) {
                    speedIndex++;
                    LocomotiveSpeedPacket packet = new LocomotiveSpeedPacket(speedIndex, this.getUuid());
                    PacketByteBuf buf = PacketByteBufs.create();
                    packet.write(buf);
                    ClientPlayNetworking.send(LocomotiveSpeedPacket.ID, buf);
                    String speed = String.format("%.0f", 12 * SPEEDS[speedIndex]);
                    Text message = Text.translatable("Speed was set to " + speed + " km/h");
                    player.sendMessage(message, false);
                }

                return ActionResult.SUCCESS;
            }

            // Check if the player is right-clicking while pressing the backward key
            else if (backwardKey.isPressed() && hand == Hand.MAIN_HAND) {
                // Decrease the speed index, but don't go below 0
                if (speedIndex > 0) {
                    speedIndex--;
                    LocomotiveSpeedPacket packet = new LocomotiveSpeedPacket(speedIndex, this.getUuid());
                    PacketByteBuf buf = PacketByteBufs.create();
                    packet.write(buf);
                    ClientPlayNetworking.send(LocomotiveSpeedPacket.ID, buf);
                    String speed = String.format("%.0f", 12 * SPEEDS[speedIndex]);
                    Text message = Text.translatable("Speed was set to " + speed + " km/h");
                    player.sendMessage(message, false);
                }

                return ActionResult.SUCCESS;
            }
        }

        // If the player is not pressing the forward or backward key, do the normal interaction
        else {
            if ((item == Items.COAL || item == Items.CHARCOAL) && this.fuel + 3600 <= 32000) {
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                this.fuel += 3600;
            }

            if (this.fuel > 0) {
                this.pushX = this.getX() - player.getX();
                this.pushZ = this.getZ() - player.getZ();
            }

            return ActionResult.success(this.getWorld().isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            if (this.fuel > 0) {
                double consumptionMultiplier = SPEEDS[speedIndex];
                this.fuel -= consumptionMultiplier;
                this.fuel = Math.max(this.fuel, 0);
            }

            if (this.fuel <= 0) {
                this.pushX = 0.0;
                this.pushZ = 0.0;
            }else {
                Vec3d direction = this.getVelocity().normalize();
                this.pushX = direction.x;
                this.pushZ = direction.z;
            }

            this.setLit(this.fuel > 0);
        }

        if (this.isLit() && this.random.nextInt(4) == 0) {
            this.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.8, this.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public void setSpeedIndex(int speedIndex) {
        this.speedIndex = speedIndex;
    }
}
