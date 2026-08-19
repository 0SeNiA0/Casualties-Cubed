package net.zaharenko424.casualties_cubed.limbs;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.zaharenko424.casualties_cubed.config.ServerConfig;
import net.zaharenko424.casualties_cubed.registry.ModSounds;

import java.util.EnumMap;
import java.util.Map;

public class Skills {

    private final EnumMap<Stat, Skill> skills = new EnumMap<>(Stat.class);

    public Skills() {
        skills.put(Stat.STR, new Skill(9));
        skills.put(Stat.RES, new Skill(8));
        skills.put(Stat.INT, new Skill(7));
    }

    public static float expGainMult() {
        return ServerConfig.EXP_GAIN.get().floatValue();
    }

    public float skillToNext(Stat stat) {
        Skill skill = skills.get(stat);
        return Mth.inverseLerp(skill.exp, skill.minExp, skill.maxExp);
    }

    public float skillAbove10(Stat stat) {
        return skills.get(stat).level - 10;
    }

    public float exp(Stat stat) {
        return skills.get(stat).exp;
    }

    public int level(Stat stat) {
        return skills.get(stat).level;
    }

    public float minExp(Stat stat) {
        return skills.get(stat).minExp;
    }

    public float maxExp(Stat stat) {
        return skills.get(stat).maxExp;
    }

    public void addExp(ServerPlayer player, Stat stat, float amount) {
        Skill skill = skills.get(stat);
        skill.exp += amount * expGainMult();
        if (skill.checkLevelUp()) {
            //TODO some display stuff
            player.displayClientMessage(Component.literal(stat.toString() + " leveled up to " + skill.level), true);
            player.serverLevel().playSound(null, player, ModSounds.LEVEL_UP.get(), SoundSource.PLAYERS, 1, 1);
        }
    }

    public void resetSkill(Stat stat) {
        Skill skill = skills.get(stat);
        skill.exp = 0;
        skill.level = 0;
        skill.updateBoundaries();
    }

    public static int getExpForLevel(int level) {
        if (level <= 0) return 0;

        float exp = 0f;
        float increment = 100;
        for (int lvl = 1; lvl <= level; lvl++) {
            if (lvl < 10) {
                exp += 60f;
                continue;
            }

            if (lvl == 10) {
                exp += increment;
                continue;
            }

            increment *= 1.18f;
            if (lvl > 20) {
                increment *= 1.15f;
                if (lvl >= 30) {
                    increment *= 2f;
                }
            }
            exp += increment;
        }

        return Math.round(exp);
    }

    public static int getLevelForExp(float exp) {
        if (exp <= 0) return 0;

        float expNeeded = 0f;
        float increment = 100;
        int lvl = 1;
        for (;; lvl++) {
            if (lvl < 10) {
                expNeeded += 60f;
                if (expNeeded > exp) return lvl - 1;
                continue;
            }

            if (lvl == 10) {
                expNeeded += increment;
                if (expNeeded > exp) return lvl - 1;
                continue;
            }

            increment *= 1.18f;
            if (lvl > 20) {
                increment *= 1.15f;
                if (lvl >= 30) {
                    increment *= 2f;
                }
            }
            expNeeded += increment;
            if (expNeeded > exp) return lvl - 1;
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        for (Map.Entry<Stat, Skill> entry : skills.entrySet()) {
            tag.put(entry.getKey().toString(), entry.getValue().save());
        }
        return tag;
    }

    public void load(CompoundTag tag) {
        for (Map.Entry<Stat, Skill> entry : skills.entrySet()) {
            if (!tag.contains(entry.getKey().toString(), FloatTag.TAG_FLOAT)) continue;

            entry.getValue().load((FloatTag) tag.get(entry.getKey().toString()));
        }
    }

    static class Skill {

        float exp;
        int level;
        int minExp;
        int maxExp;

        Skill(int level) {
            this.level = level;
            this.exp = getExpForLevel(level);
            updateBoundaries();
        }

        boolean checkLevelUp() {
            boolean anyLvlUp = false;
            while (exp >= maxExp) {
                anyLvlUp = true;
                level++;
                updateBoundaries();
            }
            return anyLvlUp;
        }

        void updateBoundaries() {
            minExp = getExpForLevel(level);
            maxExp = getExpForLevel(level + 1);
        }

        FloatTag save() {
            return FloatTag.valueOf(exp);
        }

        void load(FloatTag tag) {
            exp = tag.getAsFloat();

            level = getLevelForExp(exp);
            updateBoundaries();
        }
    }
}
