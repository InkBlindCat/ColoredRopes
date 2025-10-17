package me.inkblindcat.coloredropes.client.color;

import com.mojang.serialization.MapCodec;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;


public record ColoredItemTintSource(int defaultColor) implements ItemTintSource {
    public static final MapCodec<ColoredItemTintSource> CODEC = MapCodec.unit(new ColoredItemTintSource(0xFFFFFF));

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        DyeColor color = stack.getOrDefault(ModDataComponents.COLOR, DyeColor.WHITE);
        int rgb = color.getTextColor();
        return 0xFF000000 | rgb;
    }

    @Override
    public MapCodec<? extends ItemTintSource> type() {
        return CODEC;
    }
}
