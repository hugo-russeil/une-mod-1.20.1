package net.une.mod.item.custom;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class PassportItem extends Item {
    public PassportItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient && user.isSneaking()) {
            itemStack.getOrCreateNbt().putUuid("PlayerUUID", user.getUuid());
            return new TypedActionResult<>(ActionResult.SUCCESS, itemStack);
        }
        return new TypedActionResult<>(ActionResult.PASS, itemStack);
    }

    @Override
    public Text getName(ItemStack stack) {
        if (stack.hasNbt() && stack.getNbt().contains("PlayerUUID")) {
            UUID playerUUID = stack.getNbt().getUuid("PlayerUUID");
            return Text.translatable("item.yourmod.passport.named", playerUUID);
        } else {
            return super.getName(stack);
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (world == null || !itemStack.hasNbt() || !itemStack.getNbt().contains("PlayerUUID")) {
            return;
        }

        UUID playerUUID = itemStack.getNbt().getUuid("PlayerUUID");
        PlayerEntity player = world.getPlayerByUuid(playerUUID);
        if (player == null) {
            return;
        }

        tooltip.add(Text.translatable("Player Name: " + player.getName().getString()));
        tooltip.add(Text.translatable("Player UUID: " + player.getUuid().toString()));

        Team team = (Team) player.getScoreboardTeam();
        if (team != null) {
            tooltip.add(Text.translatable("Player Team: " + team.getName()));
        } else {
            tooltip.add(Text.translatable("Player Team: None"));
        }
        // Add more player information here
    }
}