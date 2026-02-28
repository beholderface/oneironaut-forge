package org.arcticquests.dev.oneironaut.oneironautt.mixin;

/*@Mixin(WanderingWisp.class)
public class DecorativeWispMixin {
    @Unique
    WanderingWisp wisp = (WanderingWisp) (Object) this;

    @Inject(method = "getMedia()J", at = @At(value = "HEAD", remap = false), cancellable = true, remap = false)
    public void nomedia(CallbackInfoReturnable<Long> cir){
        //thank you [
        ComponentKey<BoolComponent> decorative = OneironautComponents.WISP_DECORATIVE;
        BoolComponent decoComponent = decorative.get(wisp);
        if (decoComponent.getValue()){
            //approximately net-zero media from consuming it rather than just eating a shard for no media
            cir.setReturnValue(MediaConstants.SHARD_UNIT);
        }
    }
}*/
