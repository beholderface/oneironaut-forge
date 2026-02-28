package org.arcticquests.dev.oneironaut.oneironautt.mixin;


/*
//this should have a significantly wider-reaching effect
@Mixin(FabricXplatImpl.class)
public abstract class OpBreakBlockImmunityMixin {

    @Inject(method = "isBreakingAllowed", at = @At(value = "HEAD", remap = false), remap = false, cancellable = true)
    public void dontBreakIfImmune(ServerLevel world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<Boolean> cir){
        if (state.is(OneironautTags.Blocks.breakImmune)){
            cir.setReturnValue(false);
        }
    }
}*/
