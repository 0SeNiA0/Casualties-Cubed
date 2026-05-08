package net.adinvas.casualties_cubed.mixin.client;

import net.adinvas.casualties_cubed.client.gui.AbstractContainerScreenExt;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin implements AbstractContainerScreenExt {

    @Shadow
    private boolean skipNextRelease;

    @Override
    public void casualties_cubed$setSkipNextRelease() {
        skipNextRelease = true;
    }
}
