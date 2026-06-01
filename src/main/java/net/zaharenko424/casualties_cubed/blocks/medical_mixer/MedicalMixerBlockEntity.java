package net.zaharenko424.casualties_cubed.blocks.medical_mixer;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import net.zaharenko424.casualties_cubed.fluid_system.MultiFluidTankHandler;
import net.zaharenko424.casualties_cubed.menu.MedicalMixerMenu;
import net.zaharenko424.casualties_cubed.network.ModNetwork;
import net.zaharenko424.casualties_cubed.network.packet.ClientboundFluidSyncPacket;
import net.zaharenko424.casualties_cubed.recipe.MedicalMixerRecipe;
import net.zaharenko424.casualties_cubed.recipe.ingridients.CountIngredient;
import net.zaharenko424.casualties_cubed.recipe.ingridients.FluidIngredient;
import net.zaharenko424.casualties_cubed.registry.ModBlockEntities;
import net.zaharenko424.casualties_cubed.registry.ModRecipes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MedicalMixerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(19);
    private final IFluidHandler fluidHandler = new IFluidHandler() {
        @Override
        public int getTanks() {
            return Tanks.length;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int i) {
            return Tanks[i].getFluid();
        }

        @Override
        public int getTankCapacity(int i) {
            return Tanks[i].getCapacity();
        }

        @Override
        public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
            return i < 3;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {
            FluidStack remaining = resource.copy();
            for (int i = 0; i < 3; i++) {
                if (remaining.isEmpty()) return resource.getAmount();
                remaining.shrink(Tanks[i].fill(remaining, FluidAction.EXECUTE));
            }

            return resource.getAmount() - remaining.getAmount();
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
            int remaining = resource.getAmount();
            FluidStack drainedTotal = new FluidStack(resource.getFluid(), 0);

            // Drain only from output tanks 3–5
            for (int i = 3; i <= 5; i++) {
                FluidTank tank = Tanks[i];
                if (!tank.getFluid().isFluidEqual(resource)) continue;

                int toDrain = Math.min(tank.getFluid().getAmount(), remaining);
                FluidStack drained = tank.drain(toDrain, action);
                if (drained.isEmpty()) return FluidStack.EMPTY;
                drainedTotal.grow(drained.getAmount());
                remaining -= drained.getAmount();
                if (remaining <= 0) break;
            }

            return drainedTotal;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
            FluidStack drainedTotal = FluidStack.EMPTY;

            // Drain only from output tanks 3–5
            for (int i = 3; i <= 5; i++) {
                FluidStack inTank = Tanks[i].getFluid();
                if (inTank.isEmpty()) continue;

                int toDrain = Math.min(inTank.getAmount(), maxDrain - drainedTotal.getAmount());
                FluidStack drained = Tanks[i].drain(toDrain, action);

                if (drainedTotal.isEmpty()) drainedTotal = drained.copy();
                else drainedTotal.grow(drained.getAmount());

                if (drainedTotal.getAmount() >= maxDrain) break;
            }

            return drainedTotal;
        }
    };
    private final int TANK_CAPACITY = 1000;
    private final FluidTank[] Tanks = new FluidTank[]{
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 0, fluid));
                    }
                }
            },
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 1, fluid));
                    }
                }
            },
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 2, fluid));
                    }
                }
            },
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 3, fluid));
                    }
                }
            },
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 4, fluid));
                    }
                }
            },
            new FluidTank(TANK_CAPACITY) {
                @Override
                protected void onContentsChanged() {
                    super.onContentsChanged();
                    if (!level.isClientSide()) {
                        ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(worldPosition)), new ClientboundFluidSyncPacket(worldPosition, 5, fluid));
                    }
                }
            }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 60;

    public void setFluidInTank(FluidStack stack, int id) {
        if (id >= 0 && id < Tanks.length)
            Tanks[id].setFluid(stack);
    }

    public FluidStack getFluidInTank(int id) {
        if (id >= 0 && id < Tanks.length)
            return Tanks[id].getFluid();
        return FluidStack.EMPTY;
    }

    public int getProgress() {
        return progress;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();

    public MedicalMixerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MEDICAL_MIXER_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> MedicalMixerBlockEntity.this.progress;
                    case 1 -> MedicalMixerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> MedicalMixerBlockEntity.this.progress = i1;
                    case 1 -> MedicalMixerBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };

    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return lazyFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyFluidHandler = LazyOptional.of(() -> fluidHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyFluidHandler.invalidate();
    }

    public void drops() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            Block.popResource(level, getBlockPos(), itemHandler.getStackInSlot(i));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.casualties_cubed.medical_mixer");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new MedicalMixerMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Inventory", itemHandler.serializeNBT());
        pTag.putInt("medical_mixer.progress", progress);
        ListTag tankList = new ListTag();
        for (FluidTank tank : Tanks) {
            CompoundTag tankTag = new CompoundTag();
            tank.writeToNBT(tankTag);
            tankList.add(tankTag);
        }
        pTag.put("Tanks", tankList);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("Inventory"));
        progress = pTag.getInt("medical_mixer.progress");
        ListTag tankList = pTag.getList("Tanks", Tag.TAG_COMPOUND);
        for (int i = 0; i < tankList.size() && i < Tanks.length; i++) {
            CompoundTag tankTag = tankList.getCompound(i);

            MultiFluidTankHandler.dataFix(tankTag);

            Tanks[i].readFromNBT(tankTag);
        }
    }

    public void tick(Level pLevel, BlockPos pBlockPos, BlockState pState) {
        handleFluidItems();
        if (hasRecipe()) {
            increseCraftingProgress();
            setChanged(pLevel, pBlockPos, pState);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
                setChanged();
            }
        } else {
            resetProgress();
        }
    }

    private void handleFluidItems() {
        for (int slot = 10; slot <= 12; slot++) {
            ItemStack itemStack = itemHandler.getStackInSlot(slot);
            if (itemStack.isEmpty()) continue;

            int internalTankIndex = slot - 10;
            FluidTank internalTank = Tanks[internalTankIndex];

            int finalSlot = slot;
            itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(itemTank -> {
                FluidStack simulatedDrain = itemTank.drain(1000, IFluidHandler.FluidAction.SIMULATE);
                if (simulatedDrain.isEmpty()) return;

                int filled = internalTank.fill(simulatedDrain, IFluidHandler.FluidAction.SIMULATE);
                if (filled <= 0) return;

                FluidStack drained = itemTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                internalTank.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                // 🔴 THIS IS THE MISSING PART
                ItemStack newContainer = itemTank.getContainer();
                itemHandler.setStackInSlot(finalSlot, newContainer);

                setChanged();
            });
        }

        // --- INTERNAL TANKS -> OUTPUT SLOTS ---
        for (int slot = 13; slot <= 15; slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            int internalTankIndex = slot - 13;
            FluidTank internalTank = Tanks[internalTankIndex];

            int finalSlot = slot;
            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(itemTank -> {
                FluidStack available = internalTank.getFluid();
                if (available.isEmpty()) return;

                FluidStack toFill = available.copy();
                toFill.setAmount(Math.min(1000, toFill.getAmount()));

                int filled = itemTank.fill(toFill, IFluidHandler.FluidAction.SIMULATE);
                if (filled <= 0) return;

                FluidStack drained = internalTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                itemTank.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                // 🔴 REQUIRED
                ItemStack newContainer = itemTank.getContainer();
                itemHandler.setStackInSlot(finalSlot, newContainer);

                setChanged();
            });
        }

        for (int slot = 16; slot <= 18; slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            int internalTankIndex = slot - 16 + 3; // 16→3, 17→4, 18→5
            FluidTank internalTank = Tanks[internalTankIndex];

            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(itemTank -> {
                FluidStack fluidToFill = internalTank.getFluid().copy();
                if (fluidToFill.isEmpty()) return;

                fluidToFill.setAmount(Math.min(100, fluidToFill.getAmount()));
                int filled = itemTank.fill(fluidToFill, IFluidHandler.FluidAction.SIMULATE);
                if (filled > 0) {
                    FluidStack toDrain = internalTank.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                    itemTank.fill(toDrain, IFluidHandler.FluidAction.EXECUTE);
                    setChanged();
                }
            });
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increseCraftingProgress() {
        progress++;
    }

    private MedicalMixerRecipe cashedRecipe;

    private boolean hasRecipe() {
        List<MedicalMixerRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(ModRecipes.MEDICAL_MIXER_RECIPE.get());

        for (MedicalMixerRecipe recipe : recipes) {

            if (recipe.matches(itemHandler, getFluidsinTanks())) {

                if (canInsertInOutputSlot(recipe.getItemOutputs().toArray(new ItemStack[0])) &&
                        canInsertInOutputTank(recipe.getFluidOutputs().toArray(new FluidStack[0]))) {
                    cashedRecipe = recipe;
                    maxProgress = cashedRecipe.getProcessingTime();
                    return true;
                }
            }
        }
        return false;
    }

    private List<FluidStack> getFluidsinTanks() {
        List<FluidStack> stacks = new ArrayList<>();
        for (FluidTank tank : Tanks) {
            stacks.add(tank.getFluid());
        }
        return stacks;
    }

    private void craftItem() {
        if (cashedRecipe != null) {
            List<CountIngredient> inputitems = cashedRecipe.getItemInputs();
            int size = Math.min(5, inputitems.size());
            for (int i = 0; i < size; i++) {
                itemHandler.extractItem(i, inputitems.get(i).getCount(), false);
            }

            List<FluidIngredient> inputfluids = cashedRecipe.getFluidInputs();
            size = Math.min(3, inputfluids.size());
            for (int i = 0; i < size; i++) {
                Tanks[i].drain(inputfluids.get(i).getAmount(), IFluidHandler.FluidAction.EXECUTE);
            }

            List<ItemStack> stacks = cashedRecipe.getItemOutputs();
            addItemsToOutputSlot(stacks.toArray(new ItemStack[0]));
            List<FluidStack> fluidOutputs = cashedRecipe.getFluidOutputs();
            addFluidsToOutputTanks(fluidOutputs.toArray(new FluidStack[0]));
            setChanged();
        }
    }

    private void addFluidsToOutputTanks(FluidStack[] fluidResult) {
        FluidStack stack;
        for (FluidStack fs : fluidResult) {
            stack = fs.copy();
            for (int i = 3; i < Tanks.length; i++) {
                if (stack.isEmpty()) break;

                FluidTank tank = Tanks[i];
                stack.shrink(tank.fill(stack, IFluidHandler.FluidAction.EXECUTE));
            }
        }
    }

    private void addItemsToOutputSlot(ItemStack[] itemResults) {
        ItemStack leftover;
        for (ItemStack itemStack : itemResults) {
            leftover = itemStack.copy();

            for (int i = 5; i <= 9 && leftover.getCount() > 0; i++) {
                leftover = itemHandler.insertItem(i, leftover, false);
            }

            if (leftover.getCount() > 0) {
                Block.popResource(level, getBlockPos(), leftover);
            }
        }
    }

    private boolean canInsertInOutputTank(FluidStack[] fluidResult) {
        for (FluidStack fs : fluidResult) {
            boolean filled = false;
            for (int i = 0; i < Tanks.length; i++) {
                if (i <= 2) continue;
                FluidTank tank = Tanks[i];
                if (tank.fill(fs, IFluidHandler.FluidAction.SIMULATE) >= fs.getAmount()) {
                    filled = true;
                    break;
                }
            }
            if (!filled) return false;
        }
        return true;
    }

    private boolean canInsertInOutputSlot(ItemStack[] itemResults) {
        for (ItemStack itemResult : itemResults) {
            boolean filled = false;
            for (int i = 5; i < itemHandler.getSlots(); i++) {
                if (i > 9) break;
                ItemStack toCheck = itemHandler.getStackInSlot(i);
                if (toCheck.isEmpty()) {
                    filled = true;
                    break;
                }
                if (itemResult.is(toCheck.getItem())) {
                    if (toCheck.getCount() + itemResult.getCount() <= toCheck.getMaxStackSize()) {
                        filled = true;
                        break;
                    }
                }
            }
            if (!filled) return false;
        }

        return true;
    }

    public void sendUpdates(Level pLevel, Player pPlayer) {
        if (pLevel.isClientSide()) return;
        for (int i = 0; i < Tanks.length; i++) {
            ModNetwork.CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> (ServerPlayer) pPlayer)
                    , new ClientboundFluidSyncPacket(worldPosition, i, getFluidInTank(i)));
        }
    }
}
