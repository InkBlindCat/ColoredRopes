package me.inkblindcat.coloredropes.client.conditional;

import com.mojang.serialization.MapCodec;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public record IsGlowItem() implements ConditionalItemModelProperty {
    public static final MapCodec<IsGlowItem> MAP_CODEC = MapCodec.unit(new IsGlowItem());

    @Override
    public boolean get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed,
                       ItemDisplayContext displayContext) {
        return stack.has(ModDataComponents.GLOW) && Boolean.TRUE.equals(stack.get(ModDataComponents.GLOW));
    }

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }
}
