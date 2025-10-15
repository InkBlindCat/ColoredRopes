package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.item.ColoredRopeArrowItem;
import me.inkblindcat.coloredropes.item.ColoredRopeCoilItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ColoredRopes.MODID);

    public static final DeferredItem<Item> COLORED_ROPE_ARROW = registerItem("colored_rope_arrow",
            properties -> new ColoredRopeArrowItem(properties));

    public static final DeferredItem<Item> COLORED_ROPE_COIL = registerItem("colored_rope_coil",
            properties -> new ColoredRopeCoilItem(properties));

    public static DeferredItem<Item> registerItem(String name, Function<Item.Properties, Item> function) {
        return ITEMS.register(name, registryName -> function.apply(new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, registryName))));
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
