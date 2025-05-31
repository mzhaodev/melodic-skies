package dev.mzhao.melodicskies.mixin;

import dev.mzhao.melodicskies.controllers.ContainerController;
import lombok.var;
import net.minecraft.client.gui.inventory.GuiChest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiChest.class)
public class MixinGuiChest extends MixinGuiContainer {
    @ModifyVariable(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("STORE"), ordinal = 2)
    private int melodic_skies$adjustGuiChestLeftOffset(int value) {
        var type = ContainerController.instance.positionModificationType;
        if (type == ContainerController.PositionModificationType.RELATIVE) {
            return value + ContainerController.instance.offsetX;
        } else if (type == ContainerController.PositionModificationType.ABSOLUTE) {
            return value + ContainerController.instance.offsetX - this.melodic_skies$originalGuiLeft;
        }
        return value;
    }

    @ModifyVariable(method = "drawGuiContainerBackgroundLayer(FII)V", at = @At("STORE"), ordinal = 3)
    private int melodic_skies$adjustGuiChestRightOffset(int value) {
        var type = ContainerController.instance.positionModificationType;
        if (type == ContainerController.PositionModificationType.RELATIVE) {
            return value + ContainerController.instance.offsetY;
        } else if (type == ContainerController.PositionModificationType.ABSOLUTE) {
            return value + ContainerController.instance.offsetY - this.melodic_skies$originalGuiTop;
        }
        return value;
    }
}
