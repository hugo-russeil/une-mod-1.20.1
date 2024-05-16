package net.une.mod.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

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
            if (world.getBlockState(pos).isOf(Blocks.DIRT) ||
                world.getBlockState(pos).isOf(Blocks.GRASS_BLOCK) ||
                world.getBlockState(pos).isOf(Blocks.FARMLAND)) {

                world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.setBlockState(pos, Blocks.COARSE_DIRT.getDefaultState()); // Salt turns dirt into coarse dirt

                assert player != null; // This line is used to avoid NullPointerException
                if (!player.isCreative()) {
                    context.getStack().decrement(1); // This line consumes 1 salt item from the player's inventory
                }

                return ActionResult.SUCCESS;
            }
            else if (world.getBlockState(pos).isOf(Blocks.ICE) ||
                    world.getBlockState(pos).isOf(Blocks.PACKED_ICE) ||
                    world.getBlockState(pos).isOf(Blocks.BLUE_ICE)) {
                world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);

                if (!World.NETHER.equals(world.getRegistryKey())){ // Can't place water in the Nether
                    world.setBlockState(pos, Blocks.WATER.getDefaultState()); // Salt melts ice into water
                } else {
                    world.breakBlock(pos, false); // Just break the ice block in the Nether
                }

                assert player != null; // This line is used to avoid NullPointerException
                if (!player.isCreative()) {
                    context.getStack().decrement(1); // This line consumes 1 salt item from the player's inventory
                }

                return ActionResult.SUCCESS;
            }
            else if (world.getBlockState(pos).isOf(Blocks.SNOW_BLOCK) ||
                    world.getBlockState(pos).isOf(Blocks.SNOW)) {
                world.playSound(null, pos, SoundEvents.BLOCK_SNOW_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.setBlockState(pos, Blocks.AIR.getDefaultState()); // Salt melts snow into air

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
