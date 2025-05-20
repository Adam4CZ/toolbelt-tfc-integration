package com.adam4cz.toolbelttfcintegration.common.recipes;

import com.adam4cz.toolbelttfcintegration.ToolbeltTFCIntegration;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializers {
    
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ToolbeltTFCIntegration.MOD_ID);
    public static final RegistryObject<RecipeSerializer<ToolbeltUpgradeRecipe>> SEWING_SERIALIZER =
        RECIPE_SERIALIZERS.register("toolbelt_upgrade", ToolbeltUpgradeRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }

}
