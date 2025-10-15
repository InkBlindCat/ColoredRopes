package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.entity.ColoredRopeArrowEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ColoredRopes.MODID);

    public static ResourceKey<EntityType<?>> COLORED_ROPE_KEY = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace("colored_rope_arrow"));

    public static final Supplier<EntityType<ColoredRopeArrowEntity>> COLORED_ROPE_ARROW =
            ENTITY_TYPES.register("colored_rope_arrow", () -> EntityType.Builder.<ColoredRopeArrowEntity>of(ColoredRopeArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build(COLORED_ROPE_KEY));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
