package net.une.mod.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.une.mod.screen.CrushingScreenHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CrusherBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, ImplementedInventory {

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 100;

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final Map<Item, Item> recipes = new HashMap<>();

    public CrusherBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRUSHER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CrusherBlockEntity.this.progress;
                    case 1 -> CrusherBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CrusherBlockEntity.this.progress = value;
                    case 1 -> CrusherBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };

        loadRecipes();
    }

    private void loadRecipes() {
        // Prismarine shards
        recipes.put(Items.PRISMARINE, Items.PRISMARINE_SHARD);

        // Red sand
        recipes.put(Items.RED_SANDSTONE, Items.RED_SAND);
        recipes.put(Items.CHISELED_RED_SANDSTONE, Items.RED_SAND);
        recipes.put(Items.CUT_RED_SANDSTONE, Items.RED_SAND);
        recipes.put(Items.SMOOTH_RED_SANDSTONE, Items.RED_SAND);

        // Sand
        recipes.put(Items.SANDSTONE, Items.SAND);
        recipes.put(Items.CHISELED_SANDSTONE, Items.SAND);
        recipes.put(Items.CUT_SANDSTONE, Items.SAND);
        recipes.put(Items.SMOOTH_SANDSTONE, Items.SAND);

        recipes.put(Items.GRAVEL, Items.SAND);

        // Gravel
        recipes.put(Items.GRANITE, Items.GRAVEL);
        recipes.put(Items.POLISHED_GRANITE, Items.GRAVEL);

        recipes.put(Items.DIORITE, Items.GRAVEL);
        recipes.put(Items.POLISHED_DIORITE, Items.GRAVEL);

        recipes.put(Items.ANDESITE, Items.GRAVEL);
        recipes.put(Items.POLISHED_ANDESITE, Items.GRAVEL);

        recipes.put(Items.COBBLESTONE, Items.GRAVEL);
        recipes.put(Items.MOSSY_COBBLESTONE, Items.GRAVEL);

        recipes.put(Items.BASALT, Items.GRAVEL);
        recipes.put(Items.SMOOTH_BASALT, Items.GRAVEL);
        recipes.put(Items.POLISHED_BASALT, Items.GRAVEL);

        recipes.put(Items.BLACKSTONE, Items.GRAVEL);
        recipes.put(Items.POLISHED_BLACKSTONE, Items.GRAVEL);
        recipes.put(Items.POLISHED_BLACKSTONE_BRICKS, Items.GRAVEL);
        recipes.put(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, Items.GRAVEL);
        recipes.put(Items.CHISELED_POLISHED_BLACKSTONE, Items.GRAVEL);

        recipes.put(Items.COBBLED_DEEPSLATE, Items.GRAVEL);

        // Cobblestone
        recipes.put(Items.STONE, Items.COBBLESTONE);
        recipes.put(Items.SMOOTH_STONE, Items.COBBLESTONE);
        recipes.put(Items.STONE_BRICKS, Items.COBBLESTONE);
        recipes.put(Items.MOSSY_STONE_BRICKS, Items.COBBLESTONE);
        recipes.put(Items.CRACKED_STONE_BRICKS, Items.COBBLESTONE);
        recipes.put(Items.CHISELED_STONE_BRICKS, Items.COBBLESTONE);

        // Cobbled Deepslate
        recipes.put(Items.DEEPSLATE, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.POLISHED_DEEPSLATE, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.DEEPSLATE_BRICKS, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.CRACKED_DEEPSLATE_BRICKS, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.CHISELED_DEEPSLATE, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.DEEPSLATE_TILES, Items.COBBLED_DEEPSLATE);
        recipes.put(Items.CRACKED_DEEPSLATE_TILES, Items.COBBLED_DEEPSLATE);

    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if(world.isClient()) {
            return;
        }

        if(isOutputSlotEmptyOrReceivable()) {
            if(this.hasRecipe()) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
        } else {
            this.resetProgress();
            markDirty(world, pos, state);
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private void craftItem() {
        Item inputItem = this.getStack(INPUT_SLOT).getItem();
        Item outputItem = this.recipes.get(inputItem);
        if (outputItem != null) {
            this.removeStack(INPUT_SLOT, 1);
            ItemStack result = new ItemStack(outputItem, 1);

            this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), this.getStack(OUTPUT_SLOT).getCount() + result.getCount()));
        }

        assert world != null; // This line is used to avoid NullPointerException
        if (!world.isClient()) {
            world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    private boolean hasCraftingFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftProgress() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Item inputItem = this.getStack(INPUT_SLOT).getItem();
        return this.recipes.containsKey(inputItem);
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.getStack(OUTPUT_SLOT).isEmpty() ||
              (this.getStack(OUTPUT_SLOT).getItem() == this.recipes.get(this.getStack(INPUT_SLOT).getItem()) &&
               this.getStack(OUTPUT_SLOT).getCount() < this.getStack(OUTPUT_SLOT).getMaxCount());
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot != INPUT_SLOT;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction side) {
        return slot != OUTPUT_SLOT;
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
        return Text.literal("Crusher");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("crusher_progress", this.progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("crusher_progress");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new CrushingScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }
}