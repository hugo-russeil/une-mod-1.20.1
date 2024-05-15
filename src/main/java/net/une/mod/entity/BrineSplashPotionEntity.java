package net.une.mod.entity;

import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class BrineSplashPotionEntity extends PotionEntity {

    public BrineSplashPotionEntity(EntityType<? extends Entity> entityType, World world) {
        super((EntityType<? extends PotionEntity>) entityType, world);
    }

    public BrineSplashPotionEntity(World world, LivingEntity owner) {
        super(world, owner);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);

        // Get the hit location
        int hitX = blockHitResult.getBlockPos().getX();
        int hitY = blockHitResult.getBlockPos().getY();
        int hitZ = blockHitResult.getBlockPos().getZ();

        // Iterate over a 10-block radius around the hit location
        for (int x = hitX - 10; x <= hitX + 10; x++) {
            for (int y = hitY - 10; y <= hitY + 10; y++) {
                for (int z = hitZ - 10; z <= hitZ + 10; z++) {
                    // Get the block at the current location
                    Block block = this.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();

                    // Check if the block is a leaf block, plant or flower
                    if (block instanceof LeavesBlock || block instanceof PlantBlock || block instanceof FlowerBlock) {
                        // Break the block
                        this.getWorld().breakBlock(new BlockPos(x, y, z), true);
                    }
                }
            }
        }
    }
}