package com.example.timeslowmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TimeSlowMod.MOD_ID)
public class TimeSlowMod {
    public static final String MOD_ID = "timeslowmod";
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static boolean isTimeSlowed = false;

    public TimeSlowMod() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ModKeybinds.register();
        MinecraftForge.EVENT_BUS.register(new TimeSlowHandler());
        MinecraftForge.EVENT_BUS.register(new TimeOverlay());
        LOGGER.info("Time Slow Mod (Client Only) loaded!");
    }
}
