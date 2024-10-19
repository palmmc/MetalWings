package xyz.ashyboxy.mc.metalwings.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.ashyboxy.mc.metalwings.ArmoredElytra;
import xyz.ashyboxy.mc.metalwings.ArmoredElytraContents;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @ModifyVariable(method = "renderArmorPiece", at = @At("STORE"), ordinal = 0)
    private ItemStack replaceElytraWithChestplate(ItemStack itemStack) {
        if (!ArmoredElytra.isElytra(itemStack) || Minecraft.getInstance().player == null) return itemStack;
        ArmoredElytraContents contents = ArmoredElytraContents.tryGetContents(itemStack,
                Minecraft.getInstance().player.registryAccess());
        return contents != null ? contents.chestplate() : itemStack;
    }
}
