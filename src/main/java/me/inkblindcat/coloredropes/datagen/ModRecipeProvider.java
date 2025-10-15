package me.inkblindcat.coloredropes.datagen;

import me.inkblindcat.coloredropes.ColoredRopes;
import me.inkblindcat.coloredropes.ingredient_type.ArrayDataComponentIngredient;
import me.inkblindcat.coloredropes.registries.ModBlocks;
import me.inkblindcat.coloredropes.registries.ModDataComponents;
import me.inkblindcat.coloredropes.registries.ModItems;
import me.inkblindcat.coloredropes.util.ColorMaps;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final HolderGetter<Item> items;
    public ModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "My Recipes";
        }
    }


    @Override
    protected void buildRecipes() {
        buildColoredRopeFromWoolRecipes();
        buildGlowingColoredRopeRecipes();
        buildReDyeColoredRopeRecipes();
        buildReDyeGlowingColoredRopeRecipes();
        buildColoredRopeCoilRecipes();
        buildGlowingColoredRopeCoilRecipes();
        buildColoredRopeArrowRecipes();
        buildGlowingColoredRopeArrowRecipes();
    }


    private void buildColoredRopeFromWoolRecipes() {
        for (Map.Entry<Item, DyeColor> entry : ColorMaps.WOOL_MAP.entrySet()) {
            Item wool = entry.getKey();
            DyeColor color = entry.getValue();

            ItemStack itemStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.setCount(32);

            shaped(RecipeCategory.MISC, itemStack)
                    .pattern("WWW")
                    .pattern("  W")
                    .define('W', wool)
                    .unlockedBy("has_" + getItemName(wool), has(wool))
                    .save(output, getConversionColoredRecipeResourceKey(ModBlocks.COLORED_ROPE.asItem(), color, "wool"));
        }
    }

    private void buildGlowingColoredRopeRecipes() {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.set(ModDataComponents.GLOW.get(), true);
            itemStack.setCount(8);

            Ingredient ropeIngredient = DataComponentIngredient.of(false, ModDataComponents.COLOR, color, ModBlocks.COLORED_ROPE.asItem());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 8)
                    .requires(Items.GLOWSTONE_DUST)
                    .unlockedBy("has_" + getItemName(ModBlocks.COLORED_ROPE.asItem()), has(ModBlocks.COLORED_ROPE.asItem()))
                    .save(output, withPrefix(getColoredRecipeResourceKey(ModBlocks.COLORED_ROPE.asItem(), color), "glowing"));
        }
    }

    private void buildReDyeColoredRopeRecipes() {
        for (Map.Entry<Item, DyeColor> entry : ColorMaps.DYE_MAP.entrySet()) {
            Item dye = entry.getKey();
            DyeColor color = entry.getValue();

            ItemStack itemStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.setCount(8);

            Ingredient ropeIngredient = ArrayDataComponentIngredient.of(false,
                    ModDataComponents.COLOR.get(), DyeColor.values(), ModBlocks.COLORED_ROPE.asItem());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 8)
                    .requires(dye)
                    .unlockedBy("has_" + getItemName(dye), has(dye))
                    .save(output, withSuffix(getColoredRecipeResourceKey(ModBlocks.COLORED_ROPE.asItem(), color), "dyeing"));
        }
    }

    private void buildReDyeGlowingColoredRopeRecipes() {
        for (Map.Entry<Item, DyeColor> entry : ColorMaps.DYE_MAP.entrySet()) {
            Item dye = entry.getKey();
            DyeColor color = entry.getValue();

            ItemStack itemStack = new ItemStack(ModBlocks.COLORED_ROPE.asItem());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.set(ModDataComponents.GLOW.get(), true);
            itemStack.setCount(8);

            DataComponentExactPredicate[] predicates = Arrays.stream(DyeColor.values())
                    .map(value -> DataComponentExactPredicate.builder()
                            .expect(ModDataComponents.COLOR.get(), value)
                            .expect(ModDataComponents.GLOW.get(), true)
                            .build())
                    .toArray(DataComponentExactPredicate[]::new);

            Ingredient ropeIngredient = ArrayDataComponentIngredient.of(false, predicates, ModBlocks.COLORED_ROPE.asItem());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 8)
                    .requires(dye)
                    .unlockedBy("has_" + getItemName(dye), has(dye))
                    .save(output, withSuffix(withPrefix(getColoredRecipeResourceKey(ModBlocks.COLORED_ROPE.asItem(), color), "glowing"), "dyeing"));
        }
    }

    private void buildColoredRopeCoilRecipes() {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = new ItemStack(ModItems.COLORED_ROPE_COIL.get());
            itemStack.set(ModDataComponents.COLOR.get(), color);

            Ingredient ropeIngredient = DataComponentIngredient.of(false, ModDataComponents.COLOR, color, ModBlocks.COLORED_ROPE.asItem());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 8)
                    .requires(Items.STICK)
                    .unlockedBy("has_" + getItemName(ModBlocks.COLORED_ROPE.asItem()), has(ModBlocks.COLORED_ROPE.asItem()))
                    .save(output, getColoredRecipeResourceKey(ModItems.COLORED_ROPE_COIL.get(), color));
        }
    }

    private void buildGlowingColoredRopeCoilRecipes() {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = new ItemStack(ModItems.COLORED_ROPE_COIL.get());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.set(ModDataComponents.GLOW.get(), true);

            DataComponentExactPredicate predicate = DataComponentExactPredicate.builder()
                    .expect(ModDataComponents.COLOR.get(), color)
                    .expect(ModDataComponents.GLOW.get(), true)
                    .build();

            Ingredient ropeIngredient = DataComponentIngredient.of(false, predicate, ModBlocks.COLORED_ROPE.asItem());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 8)
                    .requires(Items.STICK)
                    .unlockedBy("has_" + getItemName(ModBlocks.COLORED_ROPE.asItem()), has(ModBlocks.COLORED_ROPE.asItem()))
                    .save(output, withPrefix(getColoredRecipeResourceKey(ModItems.COLORED_ROPE_COIL.get(), color), "glowing"));
        }
    }

    private void buildColoredRopeArrowRecipes() {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = new ItemStack(ModItems.COLORED_ROPE_ARROW.get());
            itemStack.set(ModDataComponents.COLOR.get(), color);

            Ingredient ropeIngredient = DataComponentIngredient.of(false, ModDataComponents.COLOR, color,
                    ModItems.COLORED_ROPE_COIL.get());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 2)
                    .requires(Items.ARROW)
                    .unlockedBy("has_" + getItemName(ModItems.COLORED_ROPE_COIL.get()), has(ModItems.COLORED_ROPE_COIL.get()))
                    .save(output, getColoredRecipeResourceKey(ModItems.COLORED_ROPE_ARROW.get(), color));
        }
    }

    private void buildGlowingColoredRopeArrowRecipes() {
        for (DyeColor color : DyeColor.values()) {
            ItemStack itemStack = new ItemStack(ModItems.COLORED_ROPE_ARROW.get());
            itemStack.set(ModDataComponents.COLOR.get(), color);
            itemStack.set(ModDataComponents.GLOW.get(), true);

            DataComponentExactPredicate predicate = DataComponentExactPredicate.builder()
                    .expect(ModDataComponents.COLOR.get(), color)
                    .expect(ModDataComponents.GLOW.get(), true)
                    .build();

            Ingredient ropeIngredient = DataComponentIngredient.of(false, predicate, ModItems.COLORED_ROPE_COIL.get());

            shapeless(RecipeCategory.MISC, itemStack)
                    .requires(ropeIngredient, 2)
                    .requires(Items.ARROW)
                    .unlockedBy("has_" + getItemName(ModItems.COLORED_ROPE_COIL.get()), has(ModItems.COLORED_ROPE_COIL.get()))
                    .save(output, withPrefix(getColoredRecipeResourceKey(ModItems.COLORED_ROPE_ARROW.get(), color), "glowing"));
        }
    }



    protected static ResourceKey<Recipe<?>> getColoredRecipeResourceKey(ItemLike result, DyeColor color) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(
                ColoredRopes.MODID,
                getItemName(result) + "_" + color.getName());
        return ResourceKey.create(Registries.RECIPE, resourceLocation);
    }

    protected static ResourceKey<Recipe<?>> getConversionColoredRecipeResourceKey(ItemLike result, DyeColor color, String ingredientName) {
        ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(
                ColoredRopes.MODID,
                getItemName(result) + "_" + color.getName() + "_from_" + ingredientName);
        return ResourceKey.create(Registries.RECIPE, resourceLocation);
    }

    protected static ResourceKey<Recipe<?>> withPrefix(ResourceKey<Recipe<?>> recipeKey, String prefix) {
        ResourceLocation originalLocation = recipeKey.location();
        ResourceLocation prefixedLocation = ResourceLocation.fromNamespaceAndPath(
                originalLocation.getNamespace(),
                prefix + "_" + originalLocation.getPath());
        return ResourceKey.create(Registries.RECIPE, prefixedLocation);
    }

    protected static ResourceKey<Recipe<?>> withSuffix(ResourceKey<Recipe<?>> recipeKey, String suffix) {
        ResourceLocation originalLocation = recipeKey.location();
        ResourceLocation suffixedLocation = ResourceLocation.fromNamespaceAndPath(
                originalLocation.getNamespace(),
                originalLocation.getPath() + "_" + suffix);
        return ResourceKey.create(Registries.RECIPE, suffixedLocation);
    }

    protected ShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
        return ShapedRecipeBuilder.shaped(this.items, category, result);
    }
}