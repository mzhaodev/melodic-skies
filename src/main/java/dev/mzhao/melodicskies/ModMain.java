package dev.mzhao.melodicskies;

import dev.mzhao.melodicskies.commands.UserCommandHandler;
import dev.mzhao.melodicskies.controllers.ContainerController;
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
        // register packet handler injector
        MinecraftForge.EVENT_BUS.register(ClientConnectedToServerEventHandler.class);

        // register controllers
        MinecraftForge.EVENT_BUS.register(ContainerController.instance);

        // register modules
        MinecraftForge.EVENT_BUS.register(TerminalCorrectAllPanesSolver.instance);

        // register command handler
        ClientCommandHandler.instance.registerCommand(UserCommandHandler.instance);
    }
}
