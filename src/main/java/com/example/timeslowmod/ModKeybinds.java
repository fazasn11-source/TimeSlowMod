package com.example.timeslowmod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static KeyBinding TIME_SLOW_KEY;

    public static void register() {
        TIME_SLOW_KEY = new KeyBinding(
            "key.timeslowmod.slow", 
            GLFW.GLFW_KEY_B, 
            "key.categories.timeslowmod"
        );
        
        ClientRegistry.registerKeyBinding(TIME_SLOW_KEY);
    }
}
