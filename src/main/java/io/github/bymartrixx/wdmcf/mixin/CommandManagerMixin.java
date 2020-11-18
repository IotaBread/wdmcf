package io.github.bymartrixx.wdmcf.mixin;

import net.minecraft.server.command.CommandManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
    @Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;isDebugEnabled()Z"), method = "execute")
    private boolean isDebugEnabled(Logger logger) {
        return true;
    }
}
