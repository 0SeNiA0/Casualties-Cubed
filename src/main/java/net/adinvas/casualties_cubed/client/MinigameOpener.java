package net.adinvas.casualties_cubed.client;

import net.adinvas.casualties_cubed.client.gui.minigames.BandageMinigameScreen;
import net.adinvas.casualties_cubed.client.gui.minigames.InjectMingameScreen;
import net.adinvas.casualties_cubed.client.gui.minigames.ShrapnelMinigameScreen;
import net.adinvas.casualties_cubed.limbs.Limb;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class MinigameOpener {

    public static void OpenBandageMinigame(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand){
        Minecraft.getInstance().setScreen(new BandageMinigameScreen(Minecraft.getInstance().screen, target,stack,limb,hand));
    }
    public static void OpenBandageMinigame(Player target, ItemStack stack, int slot, @Nullable Limb limb, InteractionHand hand){
        Minecraft.getInstance().setScreen(new BandageMinigameScreen(Minecraft.getInstance().screen, target,stack, slot,limb,hand));
    }

    public static void OpenSyringeMinigame(Player target, ItemStack stack, @Nullable Limb limb, InteractionHand hand){
        Minecraft.getInstance().setScreen(new InjectMingameScreen(Minecraft.getInstance().screen, target,stack,limb,hand));
    }
    public static void OpenSyringeMinigame(Player target, ItemStack stack,ItemStack bagStack,int slot, @Nullable Limb limb, InteractionHand hand){
        Minecraft.getInstance().setScreen(new InjectMingameScreen(Minecraft.getInstance().screen, target,stack,bagStack,slot,limb,hand));
    }

    public static void OpenShrapnelMinigame(Player target, @Nullable Limb limb){
        Minecraft.getInstance().setScreen(new ShrapnelMinigameScreen(Minecraft.getInstance().screen, target,limb,true));
    }
}
