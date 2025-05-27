package dev.mzhao.melodicskies.mixin;

import dev.mzhao.melodicskies.ModConstants;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Log4j2
@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public class MixinModList {
    @Shadow
    private Map<String, String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"))
    private void removeMod(List<ModContainer> modList, CallbackInfo ci) {
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) return;
        if (modTags.remove(ModConstants.MOD_ID) == null) {
            // If another mod is removing it, comment this out
            throw new RuntimeException("Cannot find modid " + ModConstants.MOD_ID);
        }
    }
}
