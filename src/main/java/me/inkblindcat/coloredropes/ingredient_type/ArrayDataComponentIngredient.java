package me.inkblindcat.coloredropes.ingredient_type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.inkblindcat.coloredropes.registries.ModIngredientTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ArrayDataComponentIngredient implements ICustomIngredient {
    public static final MapCodec<ArrayDataComponentIngredient> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(
                            HolderSetCodec.create(Registries.ITEM, BuiltInRegistries.ITEM.holderByNameCodec(), false).fieldOf("items").forGetter(ArrayDataComponentIngredient::itemSet),
                            DataComponentExactPredicate.CODEC.listOf().fieldOf("components").forGetter(i -> Arrays.asList(i.components)),
                            Codec.BOOL.optionalFieldOf("strict", false).forGetter(ArrayDataComponentIngredient::isStrict))
                    .apply(builder, (items, componentsList, strict) ->
                            new ArrayDataComponentIngredient(items, componentsList.toArray(new DataComponentExactPredicate[0]), strict)));

    private final HolderSet<Item> items;
    private final DataComponentExactPredicate[] components;
    private final boolean strict;
    private final ItemStack[] stacks;

    public ArrayDataComponentIngredient(HolderSet<Item> items, DataComponentExactPredicate[] components, boolean strict) {
        this.items = items;
        this.components = components;
        this.strict = strict;

        this.stacks = items.stream()
                .flatMap(item -> Arrays.stream(components)
                        .map(predicate -> new ItemStack(item, 1, predicate.asPatch())))
                .toArray(ItemStack[]::new);
    }

    @Override
    public boolean test(ItemStack stack) {
        if (!this.items.contains(stack.getItemHolder())) {
            return false;
        }

        if (strict) {
            for (DataComponentExactPredicate predicate : components) {
                ItemStack testStack = new ItemStack(stack.getItem().builtInRegistryHolder(), 1, predicate.asPatch());
                if (ItemStack.isSameItemSameComponents(stack, testStack)) {
                    return true;
                }
            }
            return false;
        } else {
            for (DataComponentExactPredicate predicate : components) {
                if (predicate.test(stack)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public Stream<Holder<Item>> items() {
        return items.stream();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModIngredientTypes.ARRAY_DATA_COMPONENT_INGREDIENT_TYPE.get();
    }

    @Override
    public SlotDisplay display() {
        return new SlotDisplay.Composite(Stream.of(stacks)
                .map(stack -> {
                    SlotDisplay display = new SlotDisplay.ItemStackSlotDisplay(stack);
                    ItemStack remainder = stack.getCraftingRemainder();
                    if (!remainder.isEmpty()) {
                        SlotDisplay remainderDisplay = new SlotDisplay.ItemStackSlotDisplay(remainder);
                        return new SlotDisplay.WithRemainder(display, remainderDisplay);
                    } else {
                        return display;
                    }
                })
                .toList());
    }

    public HolderSet<Item> itemSet() {
        return items;
    }

    public DataComponentExactPredicate[] components() {
        return components;
    }

    public boolean isStrict() {
        return strict;
    }

    public static Ingredient of(boolean strict, DataComponentExactPredicate[] predicates, ItemLike... items) {
        return of(strict, predicates, HolderSet.direct(Arrays.stream(items).map(ItemLike::asItem).map(Item::builtInRegistryHolder).toList()));
    }

    @SafeVarargs
    public static Ingredient of(boolean strict, DataComponentExactPredicate[] predicates, Holder<Item>... items) {
        return of(strict, predicates, HolderSet.direct(items));
    }

    public static Ingredient of(boolean strict, DataComponentExactPredicate[] predicates, HolderSet<Item> items) {
        return new ArrayDataComponentIngredient(items, predicates, strict).toVanilla();
    }

    @SafeVarargs
    public static <T> Ingredient of(boolean strict, DataComponentType<? super T> type, T[] values, ItemLike... items) {
        DataComponentExactPredicate[] predicates = Arrays.stream(values)
                .map(value -> DataComponentExactPredicate.builder().expect(type, value).build())
                .toArray(DataComponentExactPredicate[]::new);
        return of(strict, predicates, items);
    }

    @SafeVarargs
    public static <T> Ingredient of(boolean strict, Supplier<? extends DataComponentType<? super T>> type, T[] values, ItemLike... items) {
        return of(strict, type.get(), values, items);
    }

    public static Ingredient of(boolean strict, DataComponentMap[] maps, ItemLike... items) {
        DataComponentExactPredicate[] predicates = Arrays.stream(maps)
                .map(DataComponentExactPredicate::allOf)
                .toArray(DataComponentExactPredicate[]::new);
        return of(strict, predicates, items);
    }
}