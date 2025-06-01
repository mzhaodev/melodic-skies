package dev.mzhao.melodicskies.mixin;

import dev.mzhao.melodicskies.controllers.ContainerController;
import lombok.var;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {
    @Shadow
    protected int guiLeft;

    @Shadow
    protected int guiTop;

    @Unique
    protected int melodic_skies$originalGuiLeft;

    @Unique
    protected int melodic_skies$originalGuiTop;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void postInitGui(CallbackInfo ci) {
        this.melodic_skies$originalGuiLeft = this.guiLeft;
        this.melodic_skies$originalGuiTop = this.guiTop;
    }

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void preDrawScreen(CallbackInfo ci) {
        var type = ContainerController.instance.positionModificationType;
        if (type == ContainerController.PositionModificationType.RELATIVE) {
            this.guiLeft += ContainerController.instance.offsetX;
            this.guiTop += ContainerController.instance.offsetY;
        } else if (type == ContainerController.PositionModificationType.ABSOLUTE) {
            this.guiLeft = ContainerController.instance.offsetX;
            this.guiTop = ContainerController.instance.offsetY;
        } else if (type == ContainerController.PositionModificationType.NONE) {
            this.guiLeft = this.melodic_skies$originalGuiLeft;
            this.guiTop = this.melodic_skies$originalGuiTop;
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void postDrawScreen(CallbackInfo ci) {
    }
}
