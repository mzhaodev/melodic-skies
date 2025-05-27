package dev.mzhao.melodicskies.network;

import dev.mzhao.melodicskies.ModConstants;
import io.netty.channel.ChannelPipeline;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientConnectedToServerEventHandler {
    @SubscribeEvent
    public static void onConnectToServer(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        ChannelPipeline channelPipeline = event.manager.channel().pipeline();
        channelPipeline.addBefore("packet_handler", "packet_handler_" + ModConstants.MOD_ID, CustomChannelHandler.instance);
    }
}
