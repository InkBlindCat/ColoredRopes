package me.inkblindcat.coloredropes;

import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.client.color.ColoredItemTintSource;
import me.inkblindcat.coloredropes.client.conditional.IsGlowItem;
import me.inkblindcat.coloredropes.entity.client.ColoredRopeArrowRenderer;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.registries.ModEntities;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = ColoredRopes.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = ColoredRopes.MODID, value = Dist.CLIENT)
public class ColoredRopesClient {
    public ColoredRopesClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COLORED_ROPE.get(), RenderType.CUTOUT);

        EntityRenderers.register(ModEntities.COLORED_ROPE_ARROW.get(), ColoredRopeArrowRenderer::new);
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, level, pos, tintIndex) -> {
                    if (state.getBlock() instanceof ColoredRopeBlock) {
                        DyeColor color = state.getValue(ColoredRopeBlock.COLOR);
                        if (color != null) {
                            return color.getMapColor().col;
                        }
                    }
                    return 0xFFFFFF;
                },
                ModBlocks.COLORED_ROPE.get()
        );
    }

    @SubscribeEvent
    public static void registerItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(ColoredRopes.MODID, "colored_item_tint_source"),
                ColoredItemTintSource.CODEC
        );
    }

    @SubscribeEvent
    public static void registerConditionalItemModelProperties(RegisterConditionalItemModelPropertyEvent event) {
        event.register(
                ResourceLocation.fromNamespaceAndPath(ColoredRopes.MODID, "is_glow_item"),
                IsGlowItem.MAP_CODEC
        );
    }
}
