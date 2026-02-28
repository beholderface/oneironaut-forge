package org.arcticquests.dev.oneironaut.oneironautt.network

import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.effect.MobEffects
import net.minecraftforge.network.NetworkEvent
import java.util.function.Supplier

//this packet is just for the server to tell the client "hey you recently used a hoverlift, make sure you aren't holding on to the status effect for too long"
class HoverliftAntiDesyncPacket {
    companion object {
        @JvmStatic
        fun encode(msg: HoverliftAntiDesyncPacket, buf: FriendlyByteBuf) {
            // no payload
        }

        @JvmStatic
        fun decode(buf: FriendlyByteBuf): HoverliftAntiDesyncPacket {
            return HoverliftAntiDesyncPacket()
        }

        @JvmStatic
        fun handle(msg: HoverliftAntiDesyncPacket, ctxSupplier: Supplier<NetworkEvent.Context>) {
            val ctx = ctxSupplier.get()
            ctx.enqueueWork {
                val mc = Minecraft.getInstance()
                val player = mc.player
                if (player != null && player.hasEffect(MobEffects.SLOW_FALLING)) {
                    player.removeEffect(MobEffects.SLOW_FALLING)
                }
            }
            ctx.packetHandled = true
        }
    }
}