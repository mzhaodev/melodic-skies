package dev.mzhao.melodicskies.events;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsHandler {
    public static EventsHandler instance = new EventsHandler();

    public Set<Consumer<TickEvent>> tickHandlers = new HashSet<>();

    @SubscribeEvent
    public void handleTick(TickEvent event) {
        for (var consumer : tickHandlers)
            consumer.accept(event);
    }
}
