package dev.mzhao.melodicskies.mixin;

import dev.mzhao.melodicskies.controllers.ContainerController;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiContainer.class)
public class MixinGuiContainer {
    @Shadow
    protected int guiLeft;

    @Shadow
    protected int guiTop;

    @Inject(method = "drawScreen", at = @At("HEAD"))
    private void preDrawScreen(CallbackInfo ci) {
        guiLeft += ContainerController.instance.getOffsetX();
        guiTop += ContainerController.instance.getOffsetY();
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void postDrawScreen(CallbackInfo ci) {
        guiLeft -= ContainerController.instance.getOffsetX();
        guiTop -= ContainerController.instance.getOffsetY();
    }
}
