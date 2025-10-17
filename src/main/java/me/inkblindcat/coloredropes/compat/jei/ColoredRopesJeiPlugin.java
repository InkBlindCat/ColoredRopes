package me.inkblindcat.coloredropes.compat.jei;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.registries.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ColoredRopesJeiPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ColoredRopes.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(
                ModBlocks.COLORED_ROPE.asItem(),
                new ColoredRopeSubtypeInterpreter()
        );

        registration.registerSubtypeInterpreter(
                ModItems.COLORED_ROPE_ARROW.get(),
                new ColoredRopeSubtypeInterpreter()
        );

        registration.registerSubtypeInterpreter(
                ModItems.COLORED_ROPE_COIL.get(),
                new ColoredRopeSubtypeInterpreter()
        );
    }
}
