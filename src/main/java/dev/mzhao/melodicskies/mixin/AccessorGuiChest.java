package dev.mzhao.melodicskies.mixin;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public interface AccessorGuiChest {
    @Accessor("upperChestInventory")
    IInventory melodic_skies$getUpperChestInventory();

    @Accessor("lowerChestInventory")
    IInventory melodic_skies$getLowerChestInventory();
}
