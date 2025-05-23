package net.une.mod.entity;

import net.minecraft.entity.FlyingItemEntity;
import org.joml.Vector3f;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.Entity.RemovalReason;
import net.une.mod.item.ModItems;

public class BallEntity extends MobEntity implements FlyingItemEntity {
    private static final float BOUNCE_FACTOR      = 0.6f;
    private static final float INITIAL_ROLL_SPEED = 2.0f;
    private static final double FRICTION          = 0.95;
    private int waterTicks = 0;

    public BallEntity(EntityType<? extends BallEntity> type, World world) {
        super(type, world);
        this.setNoGravity(false);
    }

    public static DefaultAttributeContainer.Builder createMobAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,          1.0D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,       0.3D);
    }

    @Override
    public void tick() {
        super.tick();
        // friction on ground
        this.setVelocity(this.getVelocity().multiply(FRICTION, 1.0, FRICTION));

        // simple buoyancy
        if (this.isTouchingWater()) {
            waterTicks++;
            double targetY = this.getBlockPos().getY() + 1.3;
            double error   = targetY - this.getY();
            double velY    = waterTicks < 10
                    ? -0.05
                    : (Math.abs(error) < 0.02 ? 0 : error * 0.05);
            this.setVelocity(this.getVelocity().x, velY, this.getVelocity().z);
        } else {
            waterTicks = 0;
        }

        // bounce off walls
        if (this.horizontalCollision) {
            Vector3f v = new Vector3f(
                    -(float)this.getVelocity().x * BOUNCE_FACTOR,
                    (float)this.getVelocity().y,
                    -(float)this.getVelocity().z * BOUNCE_FACTOR
            );
            this.setVelocity(v.x, v.y, v.z);
        }

        // kick particles
        if (this.getVelocity().lengthSquared() > 0.003) {
            spawnKickParticles();
        }
    }

    private void spawnKickParticles() {
        DefaultParticleType particle = ParticleTypes.CRIT;
        this.getWorld().addParticle(
                particle,
                this.getX() + (this.random.nextDouble() - 0.5) * 0.2,
                this.getY() + (this.random.nextDouble() - 0.5) * 0.2,
                this.getZ() + (this.random.nextDouble() - 0.5) * 0.2,
                0, 0, 0
        );
    }

    @Override public boolean isPushable() { return true; }
    @Override public boolean damage(DamageSource src, float amt) { return false; }
    @Override public boolean isInvulnerable() { return true; }

    @Override
    public void pushAway(Entity entity) {
        if (entity instanceof PlayerEntity p) {
            p.playSound(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.25F, 0.8F + this.random.nextFloat() * 0.2F);
            Vector3f look = new Vector3f((float) p.getRotationVector().x, (float) 0, (float) p.getRotationVector().z)
                    .normalize()
                    .mul(INITIAL_ROLL_SPEED);
            this.setVelocity(look.x, this.getVelocity().y, look.z);
        } else {
            super.pushAway(entity);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            if (player.isSneaking()) {
                this.remove(RemovalReason.DISCARDED);
                ItemStack stack = new ItemStack(ModItems.BALL);
                if (!player.getInventory().insertStack(stack)) {
                    player.dropItem(stack, false);
                }
            } else {
                Vector3f look = new Vector3f((float) player.getRotationVector().x, (float) 0, (float) player.getRotationVector().z)
                        .normalize()
                        .mul(INITIAL_ROLL_SPEED);
                this.setVelocity(look.x, 0.25, look.z);
                this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_TURTLE, 0.25F, 0.8F + this.random.nextFloat() * 0.4F);
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ItemStack getStack() {
        // Must never return null!
        return new ItemStack(ModItems.BALL);
    }
}
