package dev.mzhao.melodicskies.controllers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContainerController {
    public static ContainerController instance = new ContainerController();

    public enum PositionModificationType {
        NONE,
        RELATIVE,
        ABSOLUTE
    }

    @NonNull
    volatile ContainerType containerType = ContainerType.NONE;

    public PositionModificationType positionModificationType = PositionModificationType.NONE;
    public volatile int offsetX;
    public volatile int offsetY;

    @SubscribeEvent
    public void onChestOpen(GuiOpenEvent event) {
        if (event.gui == null) {
            positionModificationType = PositionModificationType.NONE;
            containerType = ContainerType.NONE;
        } else if (event.gui instanceof GuiChest) {
            containerType = ContainerType.CHEST;
        }
    }

    public void moveSlotToPosition(Slot slot, PositionModificationType positionModificationType, int x, int y) {
        this.positionModificationType = positionModificationType;
        this.offsetX = x - slot.xDisplayPosition;
        this.offsetY = y - slot.yDisplayPosition;
    }

    public void moveSlotToCenter(Slot slot) {
        int centerX = containerType.xSize / 2;
        int centerY = containerType.ySize / 2;
        moveSlotToPosition(slot, PositionModificationType.RELATIVE, centerX, centerY);
    }

    public void moveSlotToMouse(Slot slot) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int x = Mouse.getX() * scale.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
        int y = scale.getScaledHeight() - Mouse.getY() * scale.getScaledHeight() / Minecraft.getMinecraft().displayHeight;
        moveSlotToPosition(slot, PositionModificationType.ABSOLUTE, x - 9, y - 9);
    }

    public void resetOffset() {
        positionModificationType = PositionModificationType.NONE;
    }
}

@AllArgsConstructor
enum ContainerType {
    NONE(0, 0),
    CHEST(176, 166);
    final int xSize;
    final int ySize;
}
