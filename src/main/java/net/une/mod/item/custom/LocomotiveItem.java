package net.une.mod.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MinecartItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.une.mod.entity.LocomotiveEntity;
import net.une.mod.entity.ModEntities;

public class LocomotiveItem extends MinecartItem {

    public LocomotiveItem(Settings settings) {
        super(AbstractMinecartEntity.Type.FURNACE, settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!blockState.isIn(BlockTags.RAILS)) {
            return ActionResult.FAIL;
        } else {
            if (!world.isClient) {
                LocomotiveEntity locomotiveEntity = new LocomotiveEntity(ModEntities.LOCOMOTIVE_ENTITY, world);
                locomotiveEntity.updatePosition(context.getHitPos().x, context.getHitPos().y, context.getHitPos().z);
                world.spawnEntity(locomotiveEntity);
            }
            context.getStack().decrement(1);
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true; // Make the item glint like enchanted items
    }
}