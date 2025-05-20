package com.adam4cz.toolbelttfcintegration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.adam4cz.toolbelttfcintegration.common.recipes.RecipeSerializers;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ToolbeltTFCIntegration.MOD_ID)
public class ToolbeltTFCIntegration {

    public static final String MOD_ID = "toolbelt_tfc_integration";

    public static final Logger LOGGER = LogManager.getLogger();

    @SuppressWarnings("removal")
    public ToolbeltTFCIntegration() {
        // Add listener for setup
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        RecipeSerializers.RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        // Send a log when the mod setup is done
        LOGGER.info("ToolbeltTFCIntegration Common Setup");
    }

}
