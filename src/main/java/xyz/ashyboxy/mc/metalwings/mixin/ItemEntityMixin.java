package xyz.ashyboxy.mc.metalwings.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.ashyboxy.mc.metalwings.ArmoredElytra;
import xyz.ashyboxy.mc.metalwings.ArmoredElytraContents;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    public abstract ItemStack getItem();

    // splitting on any damage accounts for mods changing ItemStack#canBeHurtBy
    @Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V"))
    private void splitOnDeath(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = this.getItem();
        if (!(ArmoredElytra.isElytra(itemStack))) return;
        ArmoredElytraContents contents = ArmoredElytraContents.tryGetContents(itemStack,
                this.registryAccess());
        if (contents == null) return;
        this.spawnAtLocation(serverLevel, contents.chestplate());
        this.spawnAtLocation(serverLevel, contents.elytra());
    }
}
