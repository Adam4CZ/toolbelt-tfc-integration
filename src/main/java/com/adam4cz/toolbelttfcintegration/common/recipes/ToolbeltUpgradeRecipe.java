package com.adam4cz.toolbelttfcintegration.common.recipes;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ToolbeltUpgradeRecipe implements CraftingRecipe {

    private final ResourceLocation id;
    private final String group;
    private final Ingredient belt;
    private final Ingredient pouch;
    private final Ingredient string;
    private final Ingredient needle;
    private final ItemStack result;

    public ToolbeltUpgradeRecipe(ResourceLocation id, String group, Ingredient belt, Ingredient pouch, Ingredient string, Ingredient needle, ItemStack result) {
        this.id = id;
        this.group = group;
        this.belt = belt;
        this.pouch = pouch;
        this.string = string;
        this.needle = needle;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        boolean hasBelt = false, hasPouch = false, hasString = false, hasNeedle = false;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (belt.test(stack)) hasBelt = true;
            else if (pouch.test(stack)) hasPouch = true;
            else if (string.test(stack)) hasString = true;
            else if (needle.test(stack)) hasNeedle = true;
        }
        return hasBelt && hasPouch && hasString && hasNeedle;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess access) {
        ItemStack newBelt = ItemStack.EMPTY;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (belt.test(stack)) newBelt = stack.copy();
        }
        if (!newBelt.isEmpty()) {
            CompoundTag tag = newBelt.getOrCreateTag();
            int size = tag.getInt("Size");
            if (size == 0) size = 2;
            if (size >= 9) return ItemStack.EMPTY;
            tag.putInt("Size", size + 1);
        }
        return newBelt;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 4;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return this.result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializers.SEWING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public CraftingBookCategory category() {
    return CraftingBookCategory.MISC;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            
            ItemStack itemStack = inv.getItem(i);
            
            if (!itemStack.isEmpty() && itemStack.isDamageableItem()) {

                ItemStack needleItem = itemStack.copy();

                needleItem.hurt(1, RandomSource.create(), null); // sníží durability o 1
                if (needleItem.getDamageValue() >= needleItem.getMaxDamage()) {
                    needleItem = ItemStack.EMPTY;
                }
                remaining.set(i, needleItem);

            } else {
                remaining.set(i, itemStack.getItem().getCraftingRemainingItem(itemStack));
            }
        }
        return remaining;
    }

    @Override
    public String getGroup() {
        return this.group;
    }

    // Serializer třída
    public static class Serializer implements RecipeSerializer<ToolbeltUpgradeRecipe> {
        @Override
        public ToolbeltUpgradeRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = json.has("group") ? json.get("group").getAsString() : "";
            Ingredient belt = Ingredient.fromJson(json.get("belt"));
            Ingredient pouch = Ingredient.fromJson(json.get("pouch"));
            Ingredient string = Ingredient.fromJson(json.get("string"));
            Ingredient needle = Ingredient.fromJson(json.get("needle"));
            ItemStack result = net.minecraft.world.item.crafting.ShapedRecipe.itemStackFromJson(json.getAsJsonObject("result"));
            return new ToolbeltUpgradeRecipe(id, group, belt, pouch, string, needle, result);
        }

        @Override
        public ToolbeltUpgradeRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            String group = buf.readUtf(32767);
            Ingredient belt = Ingredient.fromNetwork(buf);
            Ingredient pouch = Ingredient.fromNetwork(buf);
            Ingredient string = Ingredient.fromNetwork(buf);
            Ingredient needle = Ingredient.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return new ToolbeltUpgradeRecipe(id, group, belt, pouch, string, needle, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ToolbeltUpgradeRecipe recipe) {
            buf.writeUtf(recipe.getGroup());
            recipe.belt.toNetwork(buf);
            recipe.pouch.toNetwork(buf);
            recipe.string.toNetwork(buf);
            recipe.needle.toNetwork(buf);
            buf.writeItem(recipe.getResultItem(null));
        }
    }
}
