package me.inkblindcat.coloredropes.loot_function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import me.inkblindcat.coloredropes.registries.ModLootFunctions;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class CopyColorFunction extends LootItemConditionalFunction {
    public static final MapCodec<CopyColorFunction> CODEC = RecordCodecBuilder.mapCodec(
            instance -> commonFields(instance).apply(instance, CopyColorFunction::new)
    );

    public CopyColorFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    public static LootItemConditionalFunction.Builder<?> copyColor() {
        return simpleBuilder(CopyColorFunction::new);
    }

    @Override
    public LootItemFunctionType<CopyColorFunction> getType() {
        return ModLootFunctions.COPY_COLOR.get();
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        var blockState = context.getParameter(LootContextParams.BLOCK_STATE);
        if (blockState != null && blockState.hasProperty(ColoredRopeBlock.COLOR)) {
            DyeColor color = blockState.getValue(ColoredRopeBlock.COLOR);
            stack.set(ModDataComponents.COLOR, color);
        }
        return stack;
    }
}
