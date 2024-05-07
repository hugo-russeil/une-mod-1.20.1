package net.une.mod.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SaltItem extends Item {
    public SaltItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();

        if(!world.isClient) {
            if (world.getBlockState(pos).isOf(Blocks.DIRT) || world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK)) {
                world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState()); // Salt turns dirt into coarse dirt

                assert player != null; // This line is used to avoid NullPointerException
                if (!player.isCreative()) {
                    context.getStack().decrement(1); // This line consumes 1 salt item from the player's inventory
                }

                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
