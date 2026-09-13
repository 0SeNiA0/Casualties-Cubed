package net.zaharenko424.casualties_cubed.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.item.api.IBag;

import java.util.ArrayList;
import java.util.List;

public class MaybeBagItemWidget extends HealthScreenItemWidget {

    private final List<HealthScreenItemWidget> bagSlots = new ArrayList<>();

    public MaybeBagItemWidget(HealthScreen screen, HumanoidArm arm) {
        super(screen, null);
        this.arm = arm;
    }

    @Override
    public int slot() {
        return -1;
    }

    public boolean isBag() {
        return stack.getItem() instanceof IBag;
    }

    public void init() {
        super.init();
        updateItems();
        bagSlots.forEach(item -> {
            item.init();
            screen.addItem(item);
        });
    }

    public void tick() {
        updateItems();
    }

    private void updateItems() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack stack = player.getMainArm() == arm ? player.getMainHandItem() : player.getOffhandItem();
        if (ItemStack.isSameItemSameTags(this.stack, stack)) return;

        this.stack = stack.copy();
        dragging = false;

        if (!ServerConfig.ALLOW_BAG_IN_HEALTH_SCREEN.get() || !(this.stack.getItem() instanceof IBag bag)) {
            if (!bagSlots.isEmpty()) {
                bagSlots.forEach(screen::removeItem);
                bagSlots.clear();
            }
            return;
        }

        List<HealthScreenItemWidget> tmp = new ArrayList<>(bagSlots);
        bagSlots.clear();

        int slot = 0;
        int rows = 2;
        int columns = (int) Math.ceil(bag.size() / (double) rows);

        int start_x = (screen.width / 2) - 16;
        int start_y = (screen.height / 4) - 25;

        int centerX = start_x + (arm == HumanoidArm.RIGHT ? -72 : 88);// +9 to roughly center by half slot
        int centerY = start_y + 8;

        int totalWidth = (columns - 1) * width;
        int startX = centerX - totalWidth / 2;
        int startY = centerY - (width * rows) - 4; // small vertical gap (4px)

        HealthScreenItemWidget itemWidget;
        List<ItemStack> bagItems = bag.getItems(stack);
        for (int i = 0; i < bagItems.size(); i++) {
            if (!tmp.isEmpty()) {
                itemWidget = tmp.remove(0);
            } else {
                itemWidget = new HealthScreenItemWidget(screen, this);
                screen.addItem(itemWidget);
            }

            int col = i % columns;  // horizontal index
            int row = i / columns;  // vertical index (0 = top, 1 = bottom)
            itemWidget.set(arm, slot, bagItems.get(i), startX + col * width, startY + row * height);

            bagSlots.add(itemWidget);
            slot++;
        }

        tmp.forEach(screen::removeItem);
    }
}
