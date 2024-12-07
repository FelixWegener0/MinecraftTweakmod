package org.felixWegener.tweakmod.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "tickStatusEffects", at = @At("HEAD"))
    private void onStatusEffectTick(CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof ServerPlayerEntity player) {
            if (player.hasStatusEffect(StatusEffects.JUMP_BOOST)) {
                StatusEffectInstance effectInstance = player.getStatusEffect(StatusEffects.JUMP_BOOST);
                if (effectInstance != null && effectInstance.getDuration() <= 1) {
                    player.getAbilities().allowFlying = false;
                    player.getAbilities().flying = false;
                    player.sendAbilitiesUpdate();
                }
            }
        }
    }

}
