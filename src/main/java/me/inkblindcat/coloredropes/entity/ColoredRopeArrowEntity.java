package me.inkblindcat.coloredropes.entity;

import me.inkblindcat.coloredropes.item.ColoredRopeCoilItem;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import me.inkblindcat.coloredropes.registries.ModEntities;
import me.inkblindcat.coloredropes.registries.ModItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ColoredRopeArrowEntity extends AbstractArrow {
    public ColoredRopeArrowEntity(EntityType<ColoredRopeArrowEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ColoredRopeArrowEntity(LivingEntity shooter, Level level) {
        super(ModEntities.COLORED_ROPE_ARROW.get(), shooter, level, new ItemStack(ModItems.COLORED_ROPE_ARROW.get()), null);
    }

    public ColoredRopeArrowEntity(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityType.ARROW, owner, level, pickupItemStack, firedFromWeapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ModItems.COLORED_ROPE_ARROW.get());
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        Level level = level();
        Entity owner = getOwner();
        if (owner instanceof Player player) {
            ItemStack itemStack = this.getPickupItemStackOrigin();

            InteractionResult interactionResult =
                    ColoredRopeCoilItem.processRopeAttachment(level, hitResult, player, itemStack, 16);
            if (interactionResult == InteractionResult.SUCCESS) {
                level.broadcastEntityEvent(this, (byte)0);
                this.discard();
            }
        }

        super.onHitBlock(hitResult);
    }
}
