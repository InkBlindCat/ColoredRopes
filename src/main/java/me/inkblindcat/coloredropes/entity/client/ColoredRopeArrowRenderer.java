package me.inkblindcat.coloredropes.entity.client;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.entity.ColoredRopeArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class ColoredRopeArrowRenderer extends ArrowRenderer<ColoredRopeArrowEntity, ArrowRenderState> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ColoredRopes.MODID, "textures/entity/projectiles/colored_rope_arrow.png");

    public ColoredRopeArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ResourceLocation getTextureLocation(ArrowRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public ArrowRenderState createRenderState() {
        return new ArrowRenderState();
    }
}
