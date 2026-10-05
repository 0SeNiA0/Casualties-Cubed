package net.zaharenko424.casualties_cubed.item.usable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.zaharenko424.casualties_cubed.PlayerHealthProvider;
import net.zaharenko424.casualties_cubed.config.ClientConfig;
import net.zaharenko424.casualties_cubed.item.api.ItemWithDescription;
import net.zaharenko424.casualties_cubed.registry.ModItems;
import net.zaharenko424.casualties_cubed.util.ColorUtil;

public class ThermometerItem extends ItemWithDescription {

    public ThermometerItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);

        if (slotIndex != selectedIndex) return;
        if (player.tickCount % 10 == 0) {
            if (level.isClientSide()) return;
            player.getCapability(PlayerHealthProvider.PLAYER_HEALTH_DATA).ifPresent(h -> {
                float ambientTemp = h.getAmbientTemperature(player);
                float temperatureScale = Mth.clamp((ambientTemp - 27) / 15, 0, 1);
                int color = ColorUtil.gradient(temperatureScale, 0x242bff, 0xff3624);
                Component colorText = Component.literal("(").withStyle(ChatFormatting.GRAY).append(ClientConfig.TEMPERATURE_UNIT.get().compFunc.get(ambientTemp).withStyle(Style.EMPTY.withColor(color))).append(Component.literal(")").withStyle(ChatFormatting.GRAY));
                player.displayClientMessage(colorText, true);
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ClientThermoTooltip implements ClientTooltipComponent, TooltipComponent {

        @Override
        public int getHeight() {
            return 20;
        }

        @Override
        public int getWidth(Font font) {
            return 80;
        }

        @Override
        public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
            pGuiGraphics.renderItem(ModItems.THERMOMETER.get().getDefaultInstance(), pX, pY);
        }
    }
}
