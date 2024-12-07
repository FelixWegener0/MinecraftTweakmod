package org.felixWegener.tweakmod;

import net.fabricmc.api.ModInitializer;

public class Tweakmod implements ModInitializer {

    @Override
    public void onInitialize() {
        SignActions.register();
    }
}
