package net.adinvas.casualties_cubed.client.gui;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

public interface AbstractContainerScreenExt {

    static void setSkipNextRelease(AbstractContainerScreen<?> screen) {
        ((AbstractContainerScreenExt) screen).casualties_cubed$setSkipNextRelease();
    }

    void casualties_cubed$setSkipNextRelease();
}
