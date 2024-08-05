package xyz.ashyboxy.mc.metalwings.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.LevelEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ashyboxy.mc.metalwings.ArmoredElytraContents;

import java.util.concurrent.atomic.AtomicReference;

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

        AtomicReference<ArmoredElytraContents> contents = new AtomicReference<>();
        access.execute((l, b) -> contents.set(ArmoredElytraContents.tryGetContents(input1, l.registryAccess())));
        if (contents.get() == null) return;

        if (!input2.isEmpty()) {
            this.resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        this.resultSlots.setItem(0, contents.get().chestplate());
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
            AtomicReference<ArmoredElytraContents> contents = new AtomicReference<>();
            val$access.execute((l, b) -> contents.set(ArmoredElytraContents.tryGetContents(((GrindStoneMenuAccessor) field_16780).getRepairSlots().getItem(0), l.registryAccess())));
            if (contents.get() == null) return;

            val$access.execute((level, blockPos) -> {
                level.levelEvent(LevelEvent.SOUND_GRINDSTONE_USED, blockPos, 0);
                ((GrindStoneMenuAccessor) field_16780).getRepairSlots().setItem(0, contents.get().elytra());
            });

            ci.cancel();
        }
    }
}
