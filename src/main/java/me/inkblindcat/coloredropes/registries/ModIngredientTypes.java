package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.ingredient_type.ArrayDataComponentIngredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModIngredientTypes {
    public static final DeferredRegister<IngredientType<?>> INGREDIENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, ColoredRopes.MODID);

    public static final Supplier<IngredientType<ArrayDataComponentIngredient>> ARRAY_DATA_COMPONENT_INGREDIENT_TYPE =
            INGREDIENT_TYPES.register("array_data_component_array",
                    () -> new IngredientType<>(ArrayDataComponentIngredient.CODEC));

    public static void register(IEventBus eventBus) {
        INGREDIENT_TYPES.register(eventBus);
    }
}
