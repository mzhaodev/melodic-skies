package dev.mzhao.melodicskies;

import dev.mzhao.melodicskies.commands.UserCommandHandler;
import dev.mzhao.melodicskies.controllers.ContainerController;
import dev.mzhao.melodicskies.events.EventsHandler;
import dev.mzhao.melodicskies.modules.dungeons.terminals.TerminalChangeAllToSameColorSolver;
import dev.mzhao.melodicskies.modules.dungeons.terminals.TerminalClickInOrderSolver;
import dev.mzhao.melodicskies.modules.dungeons.terminals.TerminalCorrectAllPanesSolver;
import dev.mzhao.melodicskies.network.ClientConnectedToServerEventHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = ModConstants.MOD_ID, useMetadata=true)
public class ModMain {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // register handlers
        ClientCommandHandler.instance.registerCommand(UserCommandHandler.instance);
        MinecraftForge.EVENT_BUS.register(ClientConnectedToServerEventHandler.class);
        MinecraftForge.EVENT_BUS.register(EventsHandler.instance);

        // register controllers
        MinecraftForge.EVENT_BUS.register(ContainerController.instance);

        // register modules
        if (ModConfig.ENABLE_TERMINALS_HELPER) {
            MinecraftForge.EVENT_BUS.register(TerminalCorrectAllPanesSolver.instance);
            MinecraftForge.EVENT_BUS.register(TerminalChangeAllToSameColorSolver.instance);
            MinecraftForge.EVENT_BUS.register(TerminalClickInOrderSolver.instance);
        }
    }
}
