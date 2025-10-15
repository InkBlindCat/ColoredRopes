package me.inkblindcat.coloredropes.item;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.util.DataComponentsUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ColoredRopeItem extends RopeItem{
    public ColoredRopeItem(Block block, Properties properties) {
        super(block, properties);
    }
    @Override
    public Component getName(ItemStack stack) {
        DyeColor color = DataComponentsUtil.getColor(stack);
        String glow = DataComponentsUtil.getGlow(stack) ? ".glow" : "";
        return Component.translatable("block." + ColoredRopes.MODID + ".colored_rope." + color.getSerializedName() + glow);
    }
}
