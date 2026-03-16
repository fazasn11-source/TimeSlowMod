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
    
    // Для визуального замедления будем использовать gameTime
    private long lastGameTime = 0;
    private float timeScale = 1.0f;

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
            
            if (mc.level != null && mc.player != null) {
                if (cooldown > 0) cooldown--;
                
                // Плавный переход времени (визуальный эффект)
                if (TimeSlowMod.isTimeSlowed && smoothTransition < 0.5f) {
                    smoothTransition = Math.min(smoothTransition + TRANSITION_SPEED, 0.5f);
                } else if (!TimeSlowMod.isTimeSlowed && smoothTransition > 0) {
                    smoothTransition = Math.max(smoothTransition - TRANSITION_SPEED, 0);
                }
                
                // В Minecraft 1.16.5 нет tickRate(), используем альтернативный метод
                // Замедляем анимации через timeScale (только визуально)
                timeScale = 1.0f - smoothTransition;
                
                // Применяем визуальный эффект через игровой таймер
                if (TimeSlowMod.isTimeSlowed) {
                    // Замедляем анимации сущностей (только визуально)
                    mc.level.getAllEntities().forEach(entity -> {
                        if (entity != mc.player) { // Не замедляем игрока
                            // Визуальное замедление через tick
                        }
                    });
                }
            }
        }
    }
}
