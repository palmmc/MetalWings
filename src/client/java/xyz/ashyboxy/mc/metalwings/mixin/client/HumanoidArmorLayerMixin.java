package xyz.ashyboxy.mc.metalwings.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.ashyboxy.mc.metalwings.ArmoredElytra;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @ModifyVariable(method = "renderArmorPiece", at = @At("STORE"), ordinal = 0)
    private ItemStack replaceElytraWithChestplate(ItemStack itemStack) {
        if (!itemStack.is(Items.ELYTRA) || Minecraft.getInstance().player == null) return itemStack;
        CompoundTag chestplateData =
                itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompound(ArmoredElytra.CHESTPLATE_DATA.toString());
        if (chestplateData.isEmpty()) return itemStack;
        return ItemStack.SINGLE_ITEM_CODEC.decode(Minecraft.getInstance().player.registryAccess().createSerializationContext(NbtOps.INSTANCE), chestplateData).getOrThrow().getFirst();
    }
}
