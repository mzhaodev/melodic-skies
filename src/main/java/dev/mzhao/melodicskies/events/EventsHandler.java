package dev.mzhao.melodicskies.events;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.var;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventsHandler {
    public static EventsHandler instance = new EventsHandler();

    public ObjectSet<Consumer<TickEvent>> tickHandlers = new ObjectOpenHashSet<>();

    @SubscribeEvent
    public void handleTick(TickEvent event) {
        for (var consumer : tickHandlers)
            consumer.accept(event);
    }
}
