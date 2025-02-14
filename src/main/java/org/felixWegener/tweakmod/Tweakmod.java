package org.felixWegener.tweakmod;

import net.fabricmc.api.ModInitializer;
import org.felixWegener.tweakmod.SignActionStuff.SignActions;
import org.felixWegener.tweakmod.gravestone.GraveStone;
import org.felixWegener.tweakmod.homepoint.HomePoint;

public class Tweakmod implements ModInitializer {

    @Override
    public void onInitialize() {
        SignActions.register();
        GraveStone.register();
        HomePoint.register();
    }
}
