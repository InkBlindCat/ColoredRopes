package me.inkblindcat.coloredropes.util;

import me.inkblindcat.coloredropes.registries.ModDataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public class DataComponentsUtil {
    public static void setColor(ItemStack stack, DyeColor color) {
        stack.set(ModDataComponents.COLOR, color);
    }

    public static DyeColor getColor(ItemStack stack) {
        if (stack.has(ModDataComponents.COLOR)) {
            return stack.get(ModDataComponents.COLOR);
        }
        return DyeColor.WHITE;
    }

    public static void setGlow(ItemStack stack, boolean flag) {
        stack.set(ModDataComponents.GLOW, flag);
    }

    public static boolean getGlow(ItemStack stack) {
        if (stack.has(ModDataComponents.GLOW)) {
            return Boolean.TRUE.equals(stack.get(ModDataComponents.GLOW));
        }
        return false;
    }
}
