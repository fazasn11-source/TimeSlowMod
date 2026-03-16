package com.example.timeslowmod;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class TimeOverlay {
    
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        
        Minecraft mc = Minecraft.getInstance();
        
        if (TimeSlowMod.isTimeSlowed && mc.player != null) {
            int width = event.getWindow().getGuiScaledWidth();
            
            String text = "⏰ SLOW-MO";
            int textWidth = mc.font.width(text);
            int x = width - textWidth - 10;
            int y = 10;
            
            float pulse = (float) Math.sin(System.currentTimeMillis() * 0.01) * 0.2f + 0.8f;
            
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            
            mc.font.draw(event.getMatrixStack(), text, x, y, 
                0x55FFFF | ((int)(pulse * 200) << 24));
            
            RenderSystem.disableBlend();
        }
    }
}
