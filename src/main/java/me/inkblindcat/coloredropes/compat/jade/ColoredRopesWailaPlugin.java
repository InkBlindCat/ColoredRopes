package me.inkblindcat.coloredropes.compat.jade;

import me.inkblindcat.coloredropes.block.ColoredRopeBlock;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ColoredRopesWailaPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback((hitResult, accessor, originalAccessor) -> {
            if (accessor instanceof BlockAccessor blockAccessor) {
                BlockState blockState = blockAccessor.getBlockState();
                if (blockState.is(ModBlocks.COLORED_ROPE)) {
                    DyeColor color = blockState.getValue(ColoredRopeBlock.COLOR);
                    boolean glow = blockState.getValue(ColoredRopeBlock.GLOW);

                    ItemStack itemStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
                    itemStack.set(ModDataComponents.COLOR, color);
                    itemStack.set(ModDataComponents.GLOW, glow);

                    return registration.blockAccessor().from(blockAccessor).serversideRep(itemStack).build();
                }
            }
            return accessor;
        });
    }
}
