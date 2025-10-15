package me.inkblindcat.coloredropes.util;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Map;

public class ColorMaps {
    public static final Map<Item, DyeColor> DYE_MAP = Map.ofEntries(
            Map.entry(Items.BLACK_DYE, DyeColor.BLACK),
            Map.entry(Items.BLUE_DYE, DyeColor.BLUE),
            Map.entry(Items.BROWN_DYE, DyeColor.BROWN),
            Map.entry(Items.CYAN_DYE, DyeColor.CYAN),
            Map.entry(Items.GRAY_DYE, DyeColor.GRAY),
            Map.entry(Items.GREEN_DYE, DyeColor.GREEN),
            Map.entry(Items.LIGHT_BLUE_DYE, DyeColor.LIGHT_BLUE),
            Map.entry(Items.LIGHT_GRAY_DYE, DyeColor.LIGHT_GRAY),
            Map.entry(Items.LIME_DYE, DyeColor.LIME),
            Map.entry(Items.MAGENTA_DYE, DyeColor.MAGENTA),
            Map.entry(Items.ORANGE_DYE, DyeColor.ORANGE),
            Map.entry(Items.PINK_DYE, DyeColor.PINK),
            Map.entry(Items.PURPLE_DYE, DyeColor.PURPLE),
            Map.entry(Items.RED_DYE, DyeColor.RED),
            Map.entry(Items.YELLOW_DYE, DyeColor.YELLOW),
            Map.entry(Items.WHITE_DYE, DyeColor.WHITE)
    );

    public static final Map<Item, DyeColor> WOOL_MAP = Map.ofEntries(
            Map.entry(Items.BLACK_WOOL, DyeColor.BLACK),
            Map.entry(Items.BLUE_WOOL, DyeColor.BLUE),
            Map.entry(Items.BROWN_WOOL, DyeColor.BROWN),
            Map.entry(Items.CYAN_WOOL, DyeColor.CYAN),
            Map.entry(Items.GRAY_WOOL, DyeColor.GRAY),
            Map.entry(Items.GREEN_WOOL, DyeColor.GREEN),
            Map.entry(Items.LIGHT_BLUE_WOOL, DyeColor.LIGHT_BLUE),
            Map.entry(Items.LIGHT_GRAY_WOOL, DyeColor.LIGHT_GRAY),
            Map.entry(Items.LIME_WOOL, DyeColor.LIME),
            Map.entry(Items.MAGENTA_WOOL, DyeColor.MAGENTA),
            Map.entry(Items.ORANGE_WOOL, DyeColor.ORANGE),
            Map.entry(Items.PINK_WOOL, DyeColor.PINK),
            Map.entry(Items.PURPLE_WOOL, DyeColor.PURPLE),
            Map.entry(Items.RED_WOOL, DyeColor.RED),
            Map.entry(Items.YELLOW_WOOL, DyeColor.YELLOW),
            Map.entry(Items.WHITE_WOOL, DyeColor.WHITE)
    );
}
