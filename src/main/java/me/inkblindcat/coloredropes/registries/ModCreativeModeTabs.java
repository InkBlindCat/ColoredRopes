package me.inkblindcat.coloredropes.registries;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.util.DataComponentsUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ColoredRopes.MODID);

    public static final Supplier<CreativeModeTab> COLORED_ROPES = CREATIVE_MODE_TAB.register("coloredropes_tab",
            () -> CreativeModeTab.builder().icon(() -> {
                        ItemStack stack = new ItemStack(ModBlocks.COLORED_ROPE.get());
                        stack.set(ModDataComponents.COLOR, DyeColor.LIGHT_GRAY);
                        return stack;
                    })
                    .title(Component.translatable("creativetab."+ ColoredRopes.MODID+".coloredropes_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRope = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
                            DataComponentsUtil.setColor(coloredRope, color);
                            output.accept(coloredRope);
                        }
                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRope = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
                            DataComponentsUtil.setColor(coloredRope, color);
                            DataComponentsUtil.setGlow(coloredRope, true);
                            output.accept(coloredRope);
                        }

                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRopeCoil = new ItemStack(ModItems.COLORED_ROPE_COIL.get());
                            DataComponentsUtil.setColor(coloredRopeCoil, color);
                            output.accept(coloredRopeCoil);
                        }
                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRopeCoil = new ItemStack(ModItems.COLORED_ROPE_COIL.get());
                            DataComponentsUtil.setColor(coloredRopeCoil, color);
                            DataComponentsUtil.setGlow(coloredRopeCoil, true);
                            output.accept(coloredRopeCoil);
                        }

                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRopeArrow = new ItemStack(ModItems.COLORED_ROPE_ARROW.get());
                            DataComponentsUtil.setColor(coloredRopeArrow, color);
                            output.accept(coloredRopeArrow);
                        }
                        for (DyeColor color: DyeColor.values()) {
                            ItemStack coloredRopeArrow = new ItemStack(ModItems.COLORED_ROPE_ARROW.get());
                            DataComponentsUtil.setColor(coloredRopeArrow, color);
                            DataComponentsUtil.setGlow(coloredRopeArrow, true);
                            output.accept(coloredRopeArrow);
                        }
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
