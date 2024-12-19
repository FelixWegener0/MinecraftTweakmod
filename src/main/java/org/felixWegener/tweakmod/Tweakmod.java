package org.felixWegener.tweakmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class Tweakmod implements ModInitializer {

    @Override
    public void onInitialize() {
        SignActions.register();
    }
}
