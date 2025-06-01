package dev.mzhao.melodicskies.modules.dungeons.terminals;

import dev.mzhao.melodicskies.controllers.ContainerController;
import dev.mzhao.melodicskies.events.EventsHandler;
import dev.mzhao.melodicskies.mixin.AccessorGuiChest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.var;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TerminalSelectAllTheXItemsSolver {
    public static final Pattern DETECTION_TEXT = Pattern.compile("Select all the (.*) items!");

    public static TerminalSelectAllTheXItemsSolver instance = new TerminalSelectAllTheXItemsSolver();

    private boolean isEnabled = false;
    private Consumer<TickEvent> lambda;

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            AccessorGuiChest accessorChest = (AccessorGuiChest) chest;
            String chestTitle = accessorChest.melodic_skies$getLowerChestInventory().getDisplayName().getUnformattedText();
            Matcher matcher = DETECTION_TEXT.matcher(chestTitle);
            boolean shouldActivate = matcher.find();
            if (isEnabled != shouldActivate) {
                if (isEnabled)
                    deactivate();
                else
                    activate(matcher.group(1));
            }
        } else if (event.gui == null && isEnabled) {
            deactivate();
        }
    }

    private void activate(String pattern) {
        log.info("Activating Terminal - Select all the {} items solver.", pattern);
        pattern = pattern.replace(' ', '_');
        int colorMetadata = EnumDyeColor.valueOf(pattern).getMetadata();
        this.isEnabled = true;
        this.lambda = tick -> adjustContainerPosition(colorMetadata);
        EventsHandler.instance.tickHandlers.add(this.lambda);
    }

    private void deactivate() {
        log.info("Deactivating Terminal - Select all the X items solver.");
        EventsHandler.instance.tickHandlers.remove(this.lambda);
        this.lambda = null;
        isEnabled = false;
        ContainerController.instance.resetOffset();
    }

    private void adjustContainerPosition(int color) {
        var chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
        var containerChest = (ContainerChest) chest.inventorySlots;
        var lowerInventory = containerChest.getLowerChestInventory();
        for (int i = 0; i < lowerInventory.getSizeInventory(); ++i) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == 5 || col == 0 || col == 8)
                continue;
            var itemStack = lowerInventory.getStackInSlot(i);
            if (itemStack == null)
                continue;
            if (itemStack.isItemEnchanted())
                continue;
            if (itemStack.getItem() == Items.dye && EnumDyeColor.byDyeDamage(itemStack.getMetadata()).getMetadata() == color
                    || itemStack.getMetadata() == color) {
                ContainerController.instance.moveSlotToMouse(containerChest.getSlot(i));
                return;
            }
        }
    }
}
