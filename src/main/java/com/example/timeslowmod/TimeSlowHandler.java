package com.example.timeslowmod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TimeSlowHandler {
    private float smoothTransition = 0f;
    private final float TRANSITION_SPEED = 0.03f;
    private int cooldown = 0;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (ModKeybinds.TIME_SLOW_KEY.consumeClick() && mc.player != null && cooldown <= 0) {
            TimeSlowMod.isTimeSlowed = !TimeSlowMod.isTimeSlowed;
            
            if (TimeSlowMod.isTimeSlowed) {
                mc.player.displayClientMessage(
                    new StringTextComponent(TextFormatting.AQUA + "⏰ Время замедлено!"), 
                    true
                );
                
                mc.level.playLocalSound(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 
                    SoundCategory.PLAYERS, 0.5f, 0.3f, false
                );
            } else {
                mc.player.displayClientMessage(
                    new StringTextComponent(TextFormatting.GRAY + "⏰ Обычное время"), 
                    true
                );
                
                mc.level.playLocalSound(
                    mc.player.getX(), mc.player.getY(), mc.player.getZ(),
                    SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, 
                    SoundCategory.PLAYERS, 0.3f, 0.7f, false
                );
            }
            
            cooldown = 5;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            
            if (mc.level != null) {
                if (cooldown > 0) cooldown--;
                
                if (TimeSlowMod.isTimeSlowed && smoothTransition < 0.5f) {
                    smoothTransition = Math.min(smoothTransition + TRANSITION_SPEED, 0.5f);
                } else if (!TimeSlowMod.isTimeSlowed && smoothTransition > 0) {
                    smoothTransition = Math.max(smoothTransition - TRANSITION_SPEED, 0);
                }
                
                float targetTickRate = 20.0f - (smoothTransition * 20.0f);
                mc.level.tickRate(targetTickRate);
            }
        }
    }
    
    public float getSlowFactor() {
        return smoothTransition;
    }
}
