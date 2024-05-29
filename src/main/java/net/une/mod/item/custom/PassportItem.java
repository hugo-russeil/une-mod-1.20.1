package net.une.mod.item.custom;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.scoreboard.Team;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.CauldronBlock;

import java.util.List;
import java.util.UUID;

public class PassportItem extends Item implements DyeableItem {
    public PassportItem(Settings settings) {
        super(settings);
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> ((DyeableItem)stack.getItem()).getColor(stack), this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && user.isSneaking()) {
            // Check if the passport is already a player's
            if (!itemStack.getOrCreateNbt().contains("PlayerUUID")) {
                itemStack.getOrCreateNbt().putUuid("PlayerUUID", user.getUuid());
                itemStack.getOrCreateNbt().putString("PlayerName", user.getName().getString());

                Team team = (Team) user.getScoreboardTeam();
                if (team != null) {
                    itemStack.getOrCreateNbt().putString("TeamName", team.getName());
                } else {
                    itemStack.getOrCreateNbt().putString("TeamName", "Stateless");
                }

                return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
            }
        }
        return new TypedActionResult<>(ActionResult.PASS, itemStack);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (itemStack.hasNbt()) {
            if (itemStack.getNbt().contains("PlayerName")) {
                String playerName = itemStack.getNbt().getString("PlayerName");
                tooltip.add(Text.translatable("Name: " + playerName));
            }
            if (itemStack.getNbt().contains("PlayerUUID")) {
                UUID playerUUID = itemStack.getNbt().getUuid("PlayerUUID");
                tooltip.add(Text.translatable("UUID: " + playerUUID.toString()));
            }
            // Get the team name from the NBT data
            if (itemStack.getNbt().contains("TeamName")) {
                String teamName = itemStack.getNbt().getString("TeamName");
                tooltip.add(Text.translatable("Nation: " + teamName));
            }
        }
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("PlayerName")) {
            String playerName = stack.getNbt().getString("PlayerName");
            return Text.translatable("item.une-mod.passport.named", playerName);
        } else {
            return super.getName(stack);
        }
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("color")) {
            return nbtCompound.getInt("color");
        }
        return 16777215; // Default color (like leather armor)
    }

    @Override
    public boolean hasColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.contains("color");
    }

    @Override
    public void removeColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("color")) {
            nbtCompound.remove("color");
        }
    }

    @Override
    public void setColor(ItemStack stack, int color) {
        stack.getOrCreateNbt().putInt("color", color);
    }
}