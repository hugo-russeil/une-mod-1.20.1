package net.une.mod.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.lang.reflect.Field;

public class LocomotiveEntity extends FurnaceMinecartEntity {
    private static final double[] SPEEDS = {0, 0.5, 1, 2, 3, 4};
    private int speedIndex = 2; // start at regular speed

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

        // Get the key bindings for moving forward and backward
        KeyBinding forwardKey = MinecraftClient.getInstance().options.forwardKey;
        KeyBinding backwardKey = MinecraftClient.getInstance().options.backKey;


        // Check if the player is right-clicking while pressing the forward key
        if (forwardKey.isPressed() && hand == Hand.MAIN_HAND) {
            // Increase the speed index, but don't go past the end of the array
            if (speedIndex < SPEEDS.length - 1) {
                speedIndex++;
                if(player.getWorld().isClient()) {
                    String speed = String.format("%.0f", 12 * SPEEDS[speedIndex]);
                    Text message = Text.translatable("Speed was set to " + speed + " km/h");
                    player.sendMessage(message);
                }
            }

            return ActionResult.SUCCESS;
        }

        // Check if the player is right-clicking while pressing the backward key
        else if (backwardKey.isPressed() && hand == Hand.MAIN_HAND) {
            // Decrease the speed index, but don't go below 0
            if (speedIndex > 0) {
                speedIndex--;
                if(player.getWorld().isClient()) {
                    String speed = String.format("%.0f", 12 * SPEEDS[speedIndex]);
                    Text message = Text.translatable("Speed was set to " + speed + " km/h");
                    player.sendMessage(message);
                }
            }

            return ActionResult.SUCCESS;
        }

        // If the player is not pressing the forward or backward key, do the normal interaction
        else {
            return super.interact(player, hand);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient && this.isAlive()) {
            try {
                Field fuelField = FurnaceMinecartEntity.class.getDeclaredField("fuel");
                fuelField.setAccessible(true);
                int fuel = (int) fuelField.get(this);
                if (fuel > 0) {
                    double consumptionMultiplier = SPEEDS[speedIndex];
                    fuel -= consumptionMultiplier;
                    fuelField.set(this, Math.max(fuel, 0));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}