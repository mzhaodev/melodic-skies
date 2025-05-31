package dev.mzhao.melodicskies.modules.dungeons.terminals;

import dev.mzhao.melodicskies.controllers.ContainerController;
import dev.mzhao.melodicskies.events.EventsHandler;
import dev.mzhao.melodicskies.mixin.AccessorGuiChest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.function.Consumer;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalClickInOrderSolver {
    public static final String DETECTION_TEXT = "Click in order!";

    public static TerminalClickInOrderSolver instance = new TerminalClickInOrderSolver();

    private boolean isEnabled = false;
    private Consumer<TickEvent> lambda;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            AccessorGuiChest accessorChest = (AccessorGuiChest) chest;
            boolean shouldActivate = accessorChest.melodic_skies$getLowerChestInventory().getDisplayName().getUnformattedText()
                    .equals(DETECTION_TEXT);
            if (isEnabled != shouldActivate) {
                if (isEnabled)
                    deactivate();
                else
                    activate();
            }
        } else if (event.gui == null && isEnabled) {
            deactivate();
        }
    }

    private void activate() {
        log.info("Activating Terminal - Click In Order solver.");
        this.isEnabled = true;
        this.lambda = tick -> adjustContainerPosition();
        EventsHandler.instance.tickHandlers.add(this.lambda);
    }

    private void deactivate() {
        log.info("Deactivating Terminal - Click In Order solver.");
        EventsHandler.instance.tickHandlers.remove(this.lambda);
        this.lambda = null;
        isEnabled = false;
        ContainerController.instance.resetOffset();
    }

    private void adjustContainerPosition() {
        var chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        var containerChest = (ContainerChest) chest.inventorySlots;
        var lowerInventory = containerChest.getLowerChestInventory();

        int smallestSlot = 0;
        int smallestValue = 16;
        for (int i = 0; i < lowerInventory.getSizeInventory(); ++i) {
            var itemStack = lowerInventory.getStackInSlot(i);
            if (itemStack == null)
                continue;
            var item = itemStack.getItem();
            if (item instanceof ItemBlock
                    && ((ItemBlock) item).getBlock() instanceof BlockStainedGlassPane
                    && itemStack.getMetadata() == EnumDyeColor.RED.getMetadata()) {
                if (itemStack.stackSize < smallestValue) {
                    smallestValue = itemStack.stackSize;
                    smallestSlot = i;
                }
            }
        }

        if (smallestSlot != 0) {
            ContainerController.instance.moveSlotToMouse(containerChest.getSlot(smallestSlot));
        }
    }
}
