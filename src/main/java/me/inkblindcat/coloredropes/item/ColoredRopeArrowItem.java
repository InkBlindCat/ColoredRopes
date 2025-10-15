package me.inkblindcat.coloredropes.item;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.entity.ColoredRopeArrowEntity;
import me.inkblindcat.coloredropes.util.DataComponentsUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class ColoredRopeArrowItem extends ArrowItem{
    public ColoredRopeArrowItem(Properties properties) {
        super(properties);
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new ColoredRopeArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }

    @Override
    public Component getName(ItemStack stack) {
        DyeColor color = DataComponentsUtil.getColor(stack);
        String glow = DataComponentsUtil.getGlow(stack) ? ".glow" : "";
        return Component.translatable("item." + ColoredRopes.MODID + ".colored_rope_arrow." + color.getSerializedName() + glow);
    }
}
