package me.inkblindcat.coloredropes.loot_function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import me.inkblindcat.coloredropes.registries.ModLootFunctions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class CopyGlowFunction extends LootItemConditionalFunction {
    public static final MapCodec<CopyGlowFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, CopyGlowFunction::new)
    );

    public CopyGlowFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    public static Builder<?> copyGlow() {
        return simpleBuilder(CopyGlowFunction::new);
    }

    @Override
    public LootItemFunctionType<CopyGlowFunction> getType() {
        return ModLootFunctions.COPY_GLOW.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        var blockState = context.getParameter(LootContextParams.BLOCK_STATE);
        if (blockState != null && blockState.hasProperty(ColoredRopeBlock.GLOW)) {
            boolean glow = blockState.getValue(ColoredRopeBlock.GLOW);
            stack.set(ModDataComponents.GLOW, glow);
        }
        return stack;
    }
}
