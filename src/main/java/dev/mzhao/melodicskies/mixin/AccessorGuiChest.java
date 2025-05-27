package dev.mzhao.melodicskies.mixin;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiChest.class)
public interface AccessorGuiChest {
    @Accessor("upperChestInventory")
    IInventory getUpperChestInventory_melodicskies();

    @Accessor("lowerChestInventory")
    IInventory getLowerChestInventory_melodicskies();
}
