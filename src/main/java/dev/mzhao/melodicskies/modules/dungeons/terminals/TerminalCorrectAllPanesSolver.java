package dev.mzhao.melodicskies.modules.dungeons.terminals;

import dev.mzhao.melodicskies.controllers.ContainerController;
import dev.mzhao.melodicskies.mixin.AccessorGuiChest;
import dev.mzhao.melodicskies.network.CustomChannelHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.Consumer;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalCorrectAllPanesSolver {
    public static final String DETECTION_TEXT = "Correct all the panes!";

    public static TerminalCorrectAllPanesSolver instance = new TerminalCorrectAllPanesSolver();

    private Consumer<S2FPacketSetSlot> slotHandler;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            AccessorGuiChest accessorChest = (AccessorGuiChest) chest;
            if (accessorChest.getLowerChestInventory_melodicskies().getDisplayName().getUnformattedText().equals(DETECTION_TEXT)) {
                enableCorrectAllPanesSolver(chest);
                adjustContainerPosition();
            }
        } else if (event.gui == null) {
            CustomChannelHandler.instance.unregister(S2FPacketSetSlot.class, slotHandler);
            slotHandler = null;
        }
    }

    private void enableCorrectAllPanesSolver(GuiChest guiChest) {
        this.slotHandler = packet -> adjustContainerPosition();
        CustomChannelHandler.instance.register(S2FPacketSetSlot.class, slotHandler);
    }

    private void adjustContainerPosition() {
        var chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        // TODO calculate which slot needs to be clicked
        ContainerController.instance.moveSlotToCenter(null);
    }
}
