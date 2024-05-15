package net.une.mod.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.une.mod.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FilterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    protected final PropertyDelegate propertyDelegate;
    private int tickCounter = 0;
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final Map<Item, Item> recipes = new HashMap<>();

    public FilterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILTER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {

            @Override
            public int get(int index) {
                return 0;
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int size() {
                return 0;
            }
        };
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        tickCounter++;

        // Check if the filter is touching water on top
        BlockPos topPos = pos.up();
        BlockState topState = world.getBlockState(topPos);
        if (topState.isOf(Blocks.WATER)) {

            // Bubbles to indicate that the filter is set up correctly
            double x = pos.getX() + 0.1f + world.random.nextFloat() * 0.9f;
            double y = pos.getY() + 1.1;
            double z = pos.getZ() + 0.1f + world.random.nextFloat() * 0.9f;
            world.addParticle(ParticleTypes.BUBBLE, x, y, z, 0, 0.5, 0);

            if(!world.isClient && !(tickCounter < 200)) {
                // Reset the tick counter
                tickCounter = 0;

                // Generate a random number between 1 and 100
                int randomNumber = world.getRandom().nextInt(100) + 1;

                // Determine the item to add based on the random number
                Item itemToAdd = null;
                if (randomNumber <= 60) {
                    // 60% chance to do nothing
                    return;
                } else if (randomNumber <= 85) {
                    // 25% chance to add salt
                    itemToAdd = ModItems.SALT;
                } else if (randomNumber <= 95) {
                    // 10% chance to add sand
                    itemToAdd = Items.SAND;
                } else {
                    // 5% chance to add clay
                    itemToAdd = Items.CLAY_BALL;
                }

                // Try to add the item to the inventory
                if (itemToAdd != null) {
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = inventory.get(i);
                        if (stack.isEmpty() || (stack.getItem() == itemToAdd && stack.getCount() < stack.getMaxCount())) {
                            inventory.set(i, new ItemStack(itemToAdd, stack.getCount() + 1));
                            markDirty(world, pos, state);
                            break;
                        }
                    }
                }
            }
        } else {
            // Smoke to indicate that the filter is not set up correctly
            double x = pos.getX() + 0.1f + world.random.nextFloat() * 0.9f;
            double y = pos.getY() + 1.0;
            double z = pos.getZ() + 0.1f + world.random.nextFloat() * 0.9f;
            world.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0, 0);
        }
    }
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Filter");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return null;
    }
}