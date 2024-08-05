package xyz.ashyboxy.mc.metalwings.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.ashyboxy.mc.metalwings.ArmoredElytra;
import xyz.ashyboxy.mc.metalwings.ArmoredElytraContents;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(AnvilMenu.class)
@Debug(export = true)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
    public AnvilMenuMixin(MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @ModifyExpressionValue(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 1))
    private boolean checkChestplateElytra(boolean original, @Local(ordinal = 1) ItemStack itemStack2,
                                          @Local(ordinal = 2) ItemStack itemStack3) {
        if (itemStack2.is(ItemTags.CHEST_ARMOR) && itemStack3.getItem() instanceof ElytraItem)
            return access.evaluate((l, b) -> ArmoredElytraContents.tryGetContents(itemStack3, l.registryAccess()) != null, true);
        return original;
    }

    @Inject(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;has" +
            "(Lnet/minecraft/core/component/DataComponentType;)Z", ordinal = 0))
    private void createChestplateElytra(CallbackInfo ci, @Local(ordinal = 1) LocalRef<ItemStack> itemStack2,
                                        @Local(ordinal = 2) ItemStack itemStack3,
                                        @Local(ordinal = 0) LocalIntRef cost) {
        this.access.execute((l, b) -> itemStack2.set(ArmoredElytra.createChestplateElytra(itemStack2.get(),
                itemStack3, cost, l.getServer())));
    }
}
