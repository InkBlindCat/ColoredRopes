package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.loot_function.CopyColorFunction;
import me.inkblindcat.coloredropes.loot_function.CopyGlowFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModLootFunctions {
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_FUNCTION_TYPES =
            DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ColoredRopes.MODID);

    public static final Supplier<LootItemFunctionType<CopyColorFunction>> COPY_COLOR =
            LOOT_FUNCTION_TYPES.register("copy_color",
                    () -> new LootItemFunctionType<>(CopyColorFunction.CODEC));

    public static final Supplier<LootItemFunctionType<CopyGlowFunction>> COPY_GLOW =
            LOOT_FUNCTION_TYPES.register("copy_glow",
                    () -> new LootItemFunctionType<>(CopyGlowFunction.CODEC));

    public static void register(IEventBus eventBus) {
        LOOT_FUNCTION_TYPES.register(eventBus);
    }
}
