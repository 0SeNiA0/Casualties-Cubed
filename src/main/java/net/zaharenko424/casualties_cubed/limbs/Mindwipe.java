package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.zaharenko424.casualties_cubed.util.Util;

public class Mindwipe {

    private final PlayerHealthData data;

    private int progress = -1;
    private boolean active;

    public Mindwipe(PlayerHealthData data) {
        this.data = data;
    }

    public void start() {
        progress = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void update(ServerPlayer player) {
        if (active) {
            data.trauma(Util.moveTowards(Util.TICK_TO_SEC * 5, data.trauma(), 0));
            data.happiness(Util.moveTowards(Util.TICK_TO_SEC * 5, data.happiness(), 0));
            if (data.brainHealth() > 96f) {
                data.brainHealth(Util.moveTowards(Util.TICK_TO_SEC * 0.1f, data.brainHealth(), 96));
            }
        } else if (progress >= 0) {
            if (progress > 40 + 60) {
                for (Limb limb : Limb.values()) {
                    data.getLimb(limb).pain(0);
                }

                data.energy(0);
                data.sleep(player);
                data.consciousness(0);
                data.hearingLoss(0.8f);
                if (player.isAlive()) {
                    data.brainHealth(data.brainHealth() + 50);
                }
                data.strokeAmount(0);
                data.skills.resetSkill(Stat.INT);

                active = true;
            } else if (progress > 40) {
                data.ragdoll(player);
                data.shock(20);
                data.hearingLoss(data.hearingLoss() + Util.TICK_TO_SEC * 0.5f);
                for (Limb limb : Limb.values()) {
                    data.getLimb(limb).addPain(Util.TICK_TO_SEC * 100);
                }
            }
            progress++;
        }
    }

    public void reset() {
        progress = -1;
        active = false;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("progress", progress);
        tag.putBoolean("active", active);
        return tag;
    }

    public void load(CompoundTag tag) {
        progress = tag.contains("progress") ? tag.getInt("progress") : -1;
        active = tag.getBoolean("active");
    }
}
