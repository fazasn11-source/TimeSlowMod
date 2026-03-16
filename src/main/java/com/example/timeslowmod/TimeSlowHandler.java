package com.example.timeslowmod;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import java.util.Iterator;

public class TimeSlowHandler {
    private float smoothTransition = 0f;
    private final float TRANSITION_SPEED = 0.03f;
    private int cooldown = 0;
    
    private long lastGameTime = 0;
    private float visualTimeScale = 1.0f;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        
        if (ModKeybinds.TIME_SLOW_KEY.isDown() && mc.player != null && cooldown <= 0) {
            // Проверяем что клавиша была нажата в этом тике
            if (cooldown == 0) {
                TimeSlowMod.isTimeSlowed = !TimeSlowMod.isTimeSlowed;
                
                if (TimeSlowMod.isTimeSlowed) {
                    mc.player.displayClientMessage(
                        new StringTextComponent(TextFormatting.AQUA + "⏰ Время замедлено!"), 
                        true
                    );
                    
                    if (mc.level != null) {
                        mc.level.playSound(mc.player, mc.player.blockPosition(),
                            SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_AMBIENT, 
                            SoundCategory.PLAYERS, 0.5f, 0.3f);
                    }
                } else {
                    mc.player.displayClientMessage(
                        new StringTextComponent(TextFormatting.GRAY + "⏰ Обычное время"), 
                        true
                    );
                    
                    if (mc.level != null) {
                        mc.level.playSound(mc.player, mc.player.blockPosition(),
                            SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, 
                            SoundCategory.PLAYERS, 0.3f, 0.7f);
                    }
                }
                
                cooldown = 5;
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            
            if (mc.level != null && mc.player != null) {
                if (cooldown > 0) cooldown--;
                
                // Плавный переход
                if (TimeSlowMod.isTimeSlowed && smoothTransition < 0.5f) {
                    smoothTransition = smoothTransition + TRANSITION_SPEED;
                    if (smoothTransition > 0.5f) smoothTransition = 0.5f;
                } else if (!TimeSlowMod.isTimeSlowed && smoothTransition > 0) {
                    smoothTransition = smoothTransition - TRANSITION_SPEED;
                    if (smoothTransition < 0) smoothTransition = 0;
                }
                
                // В Minecraft 1.16.5 мы не можем напрямую замедлить время,
                // но можем создать визуальный эффект через управление рендером
                visualTimeScale = 1.0f - smoothTransition;
                
                // Здесь можно добавить другие визуальные эффекты
            }
        }
    }
}
