package dev.mzhao.melodicskies.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ChannelHandler.Sharable
public class CustomChannelHandler extends SimpleChannelInboundHandler<Packet<INetHandler>> {
    public static final CustomChannelHandler instance = new CustomChannelHandler();

    private final HashMap<Class<? extends Packet<? extends INetHandler>>,
                Set<Consumer<? extends Packet<? extends INetHandler>>>> callbacks = new HashMap<>();

    public <T extends Packet<? extends INetHandler>> void register(Class<T> packetType, Consumer<T> lambda) {
        log.info("Registering callback ({}, {})", packetType, lambda);
        callbacks.computeIfAbsent(packetType, type -> new HashSet<>()).add(lambda);
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet<? extends INetHandler>> void unregister(Class<T> packetType, Consumer<T> lambda) {
        log.info("Unregistering callback ({}, {})", packetType, lambda);
        callbacks.get(packetType).remove(lambda);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        Class<?> currentClass = packet.getClass();
        while (currentClass != Packet.class) {
            var callbackArray = callbacks.get(currentClass);
            if (callbackArray != null && !callbackArray.isEmpty())
                for (var lambda : callbackArray)
                    ((Consumer) lambda).accept(packet);
            currentClass = currentClass.getSuperclass();
        }
        context.fireChannelRead(packet); // propagate packet
    }
}
