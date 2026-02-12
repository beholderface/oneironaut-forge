package org.arcticquests.dev.oneironautfinal.OneironautFinal.registry;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.casting.NoosphereAmbitExtensionComponent;

public class OneironautCastEnvComponents {
    public static void init(){
        CastingEnvironment.addCreateEventListener((env, nbt)->{
            if (env instanceof PlayerBasedCastEnv){
                if (Oneironaut.isWorldNoosphere(env.getWorld())){
                    env.addExtension(new NoosphereAmbitExtensionComponent(env));
                }
            }
        });
    }
}