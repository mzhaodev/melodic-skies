package dev.mzhao.melodicskies.mixin;

import dev.mzhao.melodicskies.controllers.ContainerController;
import net.minecraft.client.gui.inventory.GuiChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiChest.class)
public class MixinGuiChest {
    @ModifyVariable(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("STORE"), ordinal = 2)
    private int adjustGuiChestLeftOffset_melodicskies(int value) {
        return value + ContainerController.instance.getOffsetX();
    }

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("STORE"), ordinal = 3)
    private int adjustGuiChestRightOffset_melodicskies(int value) {
        return value + ContainerController.instance.getOffsetY();
    }
}
