package xyz.ashyboxy.mc.metalwings.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ashyboxy.mc.metalwings.ArmoredElytra;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin extends AbstractContainerMenu {
    protected GrindstoneMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Shadow
    @Final
    Container repairSlots;
    @Shadow
    @Final
    private Container resultSlots;

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/GrindstoneMenu;broadcastChanges()V"))
    private void createOriginalChestplate(CallbackInfo ci) {
        ItemStack input1 = this.repairSlots.getItem(0);
        ItemStack input2 = this.repairSlots.getItem(1);

        CompoundTag customData =
                input1.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag chestplateData = customData.getCompound(ArmoredElytra.CHESTPLATE_DATA.toString());

        if (customData.getCompound(ArmoredElytra.ELYTRA_DATA.toString()).isEmpty() || chestplateData.isEmpty())
            return;
        if (!input2.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }
        access.execute((l, b) -> this.resultSlots.setItem(0,
                ItemStack.SINGLE_ITEM_CODEC.decode(l.registryAccess().createSerializationContext(NbtOps.INSTANCE)
                        , chestplateData).getOrThrow().getFirst()));
    }

    @Mixin(GrindstoneMenu.class)
    public interface GrindStoneMenuAccessor {
        @Accessor
        Container getRepairSlots();
    }

    @Mixin(targets = "net/minecraft/world/inventory/GrindstoneMenu$4")
    public static abstract class ResultSlotMixin extends Slot {
        @Shadow
        @Final
        ContainerLevelAccess val$access;

        @Shadow
        @Final
        GrindstoneMenu field_16780;

        public ResultSlotMixin(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
        private void takeSeparatedChestplate(Player player, ItemStack stack, CallbackInfo ci) {
            CompoundTag customData =
                    ((GrindStoneMenuAccessor) field_16780).getRepairSlots().getItem(0).getOrDefault(DataComponents.CUSTOM_DATA,
                            CustomData.EMPTY).copyTag();
            CompoundTag elytraData = customData.getCompound(ArmoredElytra.ELYTRA_DATA.toString());
            if (elytraData.isEmpty() || customData.getCompound(ArmoredElytra.CHESTPLATE_DATA.toString()).isEmpty()) return;
            val$access.execute((level, blockPos) -> {
                level.levelEvent(LevelEvent.SOUND_GRINDSTONE_USED, blockPos, 0);
                ((GrindStoneMenuAccessor) field_16780).getRepairSlots().setItem(0,
                        ItemStack.SINGLE_ITEM_CODEC.decode(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), elytraData).getOrThrow().getFirst());
            });
            ci.cancel();
        }
    }
}
