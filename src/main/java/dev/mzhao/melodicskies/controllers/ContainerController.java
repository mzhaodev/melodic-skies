package dev.mzhao.melodicskies.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContainerController {
    public static ContainerController instance = new ContainerController();

    // Minecraft special numbers
    private static final int slotWidth = 16;

    @NonNull
    volatile ContainerType containerType = ContainerType.NONE;
    volatile int offsetX;
    volatile int offsetY;

    @SubscribeEvent
    public void onChestOpen(GuiOpenEvent event) {
        if (event == null) {
            this.offsetX = 0;
            this.offsetY = 0;
            containerType = ContainerType.NONE;
        } else if (event.gui instanceof GuiChest) {
            containerType = ContainerType.CHEST;
        }
    }

    public void moveSlotToCenter(Slot slot) {
        int centerX = containerType.xSize / 2;
        int centerY = containerType.ySize / 2;
        this.offsetX = centerX - slot.xDisplayPosition;
        this.offsetY = centerY - slot.yDisplayPosition;
    }
}

@AllArgsConstructor
enum ContainerType {
    NONE(0, 0),
    CHEST(176, 166);
    final int xSize;
    final int ySize;
}