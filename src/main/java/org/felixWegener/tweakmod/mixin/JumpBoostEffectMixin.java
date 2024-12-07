package org.felixWegener.tweakmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatusEffectInstance.class)
public abstract class JumpBoostEffectMixin  {

    @Inject(method = "onApplied", at = @At("HEAD"))
    public void enableCreativeFlightWhenSlowFalling(LivingEntity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayerEntity player && player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
            player.getAbilities().allowFlying = true;
            player.sendAbilitiesUpdate();
        }
    }

}
