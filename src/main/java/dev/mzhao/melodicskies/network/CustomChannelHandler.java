package dev.mzhao.melodicskies.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import java.util.function.Consumer;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ChannelHandler.Sharable
public class CustomChannelHandler extends SimpleChannelInboundHandler<Packet<INetHandler>> {
    public static final CustomChannelHandler instance = new CustomChannelHandler();

    private final Object2ObjectOpenHashMap<Class<? extends Packet<? extends INetHandler>>,
            ObjectSet<Consumer<? extends Packet<? extends INetHandler>>>> callbacks = new Object2ObjectOpenHashMap<>();

    public <T extends Packet<? extends INetHandler>> void register(Class<T> packetType, Consumer<T> lambda) {
        synchronized (callbacks) {
            callbacks.computeIfAbsent(packetType, type -> new ObjectOpenHashSet<>()).add(lambda);
        }
    }

    public <T extends Packet<? extends INetHandler>> void unregister(Class<T> packetType, Consumer<T> lambda) {
        synchronized (callbacks) {
            callbacks.getOrDefault(packetType, ObjectSets.EMPTY_SET).remove(lambda);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        Class<?> currentClass = packet.getClass();
        while (currentClass != Packet.class) {
            synchronized (callbacks) {
                var callbackArray = callbacks.get(currentClass);
                if (callbackArray != null && !callbackArray.isEmpty())
                    for (var lambda : callbackArray)
                        ((Consumer<Packet>) lambda).accept(packet);
            }
            currentClass = currentClass.getSuperclass();
        }
        context.fireChannelRead(packet); // propagate packet
    }
}
