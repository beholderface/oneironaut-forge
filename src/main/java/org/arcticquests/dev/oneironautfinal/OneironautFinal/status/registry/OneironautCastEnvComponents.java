package org.arcticquests.dev.oneironautfinal.OneironautFinal.status.registry;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv;
import org.arcticquests.dev.oneironautfinal.OneironautFinal.utils.WorldUtils;

public class OneironautCastEnvComponents {    public static void init(){
    CastingEnvironment.addCreateEventListener((env, nbt)->{
        if (env instanceof PlayerBasedCastEnv){
            if (WorldUtils.isWorldNoosphere(env.getWorld())){
                env.addExtension(new NoosphereAmbitExtensionComponent(env));
            }
        }
    });
}

}
