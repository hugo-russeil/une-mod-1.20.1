package net.une.mod.item.custom;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.une.mod.entity.BallEntity;
import net.une.mod.entity.ModEntities;

public class BallItem extends Item {
    public BallItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos().offset(context.getSide());

        // must place onto an empty block
        if (!world.isAir(blockPos)) {
            return ActionResult.FAIL;
        }

        if (!world.isClient) {
            // spawn your BallEntity exactly like LocomotiveEntity
            BallEntity ball = new BallEntity(ModEntities.BALL_ENTITY, world);
            ball.updatePosition(
                    context.getHitPos().x,
                    context.getHitPos().y,
                    context.getHitPos().z
            );
            world.spawnEntity(ball);
        }

        // consume one ball
        context.getStack().decrement(1);
        return ActionResult.SUCCESS;
    }
}
