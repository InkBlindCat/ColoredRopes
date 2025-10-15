package me.inkblindcat.coloredropes.compat.jei;

import me.inkblindcat.coloredropes.registries.ModDataComponents;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ColoredRopeSubtypeInterpreter implements ISubtypeInterpreter<ItemStack> {

    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        StringBuilder subtypeData = new StringBuilder();

        DyeColor color = ingredient.get(ModDataComponents.COLOR);
        if (color != null) {
            subtypeData.append("color:").append(color.getSerializedName());
        } else {
            subtypeData.append("color:white");
        }

        Boolean glow = ingredient.get(ModDataComponents.GLOW);
        if (glow != null && glow) {
            subtypeData.append("|glow:true");
        } else {
            subtypeData.append("|glow:false");
        }

        return subtypeData.toString();
    }
}
