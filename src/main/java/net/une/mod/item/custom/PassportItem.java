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
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        if (itemStack.hasNbt()) {
            if (itemStack.getNbt().contains("PlayerName")) {
                String playerName = itemStack.getNbt().getString("PlayerName");
                tooltip.add(Text.translatable("Name: " + playerName));
            }
            if (itemStack.getNbt().contains("PlayerUUID")) {
                UUID playerUUID = itemStack.getNbt().getUuid("PlayerUUID");
                tooltip.add(Text.translatable("UUID: " + playerUUID.toString()));
                PlayerEntity player = world.getPlayerByUuid(playerUUID);
                if (player != null) {
                    Team team = (Team) player.getScoreboardTeam();
                    if (team != null) {
                        tooltip.add(Text.translatable("Nation: " + team.getName()));
                    } else {
                        tooltip.add(Text.translatable("Nation: Stateless"));
                    }
                }
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
}