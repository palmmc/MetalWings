package xyz.ashyboxy.mc.metalwings;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

public record ArmoredElytraContents(ItemStack chestplate, ItemStack elytra) {
    public static @Nullable ArmoredElytraContents tryGetContents(ItemStack combined, RegistryAccess access) {
        ArmoredElytraContents result;
        if ((result = tryGetContentsCustomData(combined, access)) != null) return result;
        if ((result = tryGetContentsBundle(combined)) != null) return result;
        return null;
    }

    public static @Nullable ArmoredElytraContents tryGetContentsCustomData(ItemStack combined,
                                                                           RegistryAccess access) {
        CompoundTag customData = combined.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        CompoundTag chestplateData = customData.getCompound(ArmoredElytra.CHESTPLATE_DATA.toString());
        CompoundTag elytraData = customData.getCompound(ArmoredElytra.ELYTRA_DATA.toString());
        if (chestplateData.isEmpty() || elytraData.isEmpty()) return null;
        // apologies for my laziness here
        try {
            ItemStack chestplate = ItemStack.SINGLE_ITEM_CODEC.decode(access.createSerializationContext(NbtOps.INSTANCE),
                    chestplateData).getOrThrow().getFirst();
            ItemStack elytra = ItemStack.SINGLE_ITEM_CODEC.decode(access.createSerializationContext(NbtOps.INSTANCE),
                    elytraData).getOrThrow().getFirst();
            return new ArmoredElytraContents(chestplate, elytra);
        }
        catch (Exception e) {
            return null;
        }
    }

    // TODO: make this more robust against custom elytra and elytra in the chest armor tag
    public static @Nullable ArmoredElytraContents tryGetContentsBundle(ItemStack combined) {
        BundleContents bundleContents = combined.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY);
        if (bundleContents.size() != 2) return null;
        ItemStack chestplate = null;
        ItemStack elytra = null;

        for (ItemStack itemStack : bundleContents.items()) {
            if (itemStack.getItem() instanceof ElytraItem) elytra = itemStack.copy();
            else if (itemStack.is(ItemTags.CHEST_ARMOR)) chestplate = itemStack.copy();
        }

        if (chestplate == null || elytra == null) return null;
        return new ArmoredElytraContents(chestplate, elytra);
    }
}
