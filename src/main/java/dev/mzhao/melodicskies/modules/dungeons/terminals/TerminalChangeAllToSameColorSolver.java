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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalChangeAllToSameColorSolver {
    public static final String DETECTION_TEXT = "Change all to same color!";
    private static final Map<Integer, Integer> colors;
    static {
            Map<Integer, Integer> colorsMutable = new HashMap<>();
            colorsMutable.put(EnumDyeColor.ORANGE.getMetadata(), 0);
            colorsMutable.put(EnumDyeColor.YELLOW.getMetadata(), 1);
            colorsMutable.put(EnumDyeColor.GREEN.getMetadata(), 2);
            colorsMutable.put(EnumDyeColor.BLUE.getMetadata(), 3);
            colorsMutable.put(EnumDyeColor.RED.getMetadata(), 4);
            colors = Collections.unmodifiableMap(colorsMutable);
    }

    public static TerminalChangeAllToSameColorSolver instance = new TerminalChangeAllToSameColorSolver();

    private boolean isEnabled = false;
    private Consumer<TickEvent> adjustGuiLambda;

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
        log.info("Activating Terminal - Change All To Same Color solver.");
        this.isEnabled = true;
        this.adjustGuiLambda = tick -> adjustContainerPosition();
        EventsHandler.instance.tickHandlers.add(this.adjustGuiLambda);
    }

    private void deactivate() {
        log.info("Deactivating Terminal - Change All To Same Color solver.");
        EventsHandler.instance.tickHandlers.remove(this.adjustGuiLambda);
        this.adjustGuiLambda = null;
        isEnabled = false;
        ContainerController.instance.resetOffset();
    }

    private int countClicksNeeded(Map<Integer, Integer> freq, int target) {
        int clicks = 0;
        for (var entry : freq.entrySet()) {
            int ord = colors.get(entry.getKey());
            int ord2 = colors.get(target);
            int dist = ord2 - ord;
            if (dist < 0)
                dist += colors.size();
            clicks += dist * entry.getValue();
        }
        return clicks;
    }

    private void adjustContainerPosition() {
        var chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        var containerChest = (ContainerChest) chest.inventorySlots;
        var lowerInventory = containerChest.getLowerChestInventory();

        Map<Integer, Integer> colorFreq = new HashMap<>();
        for (int i = 0; i < lowerInventory.getSizeInventory(); ++i) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == 4 || col <= 2 || col >= 6)
                continue;
            var itemStack = lowerInventory.getStackInSlot(i);
            if (itemStack == null)
                continue;
            var item = itemStack.getItem();
            if (item instanceof ItemBlock
                    && ((ItemBlock) item).getBlock() instanceof BlockStainedGlassPane) {
                if (colors.containsKey(itemStack.getMetadata())) {
                    colorFreq.merge(itemStack.getMetadata(), 1, Integer::sum);
                }
            }
        }

        int target = 0;
        int targetClicks = Integer.MAX_VALUE;
        for (var color : colors.keySet()) {
            int actions = countClicksNeeded(colorFreq, color);
            if (actions < targetClicks) {
                target = color;
                targetClicks = actions;
            }
        }

        for (int i = 0; i < lowerInventory.getSizeInventory(); ++i) {
            var itemStack = lowerInventory.getStackInSlot(i);
            if (itemStack == null)
                continue;
            var item = itemStack.getItem();
            if (item instanceof ItemBlock
                    && ((ItemBlock) item).getBlock() instanceof BlockStainedGlassPane) {
                if (colors.containsKey(itemStack.getMetadata()) && itemStack.getMetadata() != target) {
                    ContainerController.instance.moveSlotToMouse(containerChest.getSlot(i));
                    return;
                }
            }
        }
    }
}
