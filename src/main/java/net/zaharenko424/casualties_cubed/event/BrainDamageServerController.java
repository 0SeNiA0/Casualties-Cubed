package net.zaharenko424.casualties_cubed.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.common.Mod;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.Random;

@Mod.EventBusSubscriber
public class BrainDamageServerController {

    public static String modifyMessage(ServerPlayer player, String message) {
        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return message;

        float brain = data.getBrainHealth();
        if (brain < 30) return null; // unconscious

        float dislocatedJaw = data.getLimb(Limb.HEAD).getDislocation();
        boolean JawMissing = data.isMouthRemoved();

        float clarity = Mth.clamp(brain / 100f, 0, 1);

        message = distortScaled(message, clarity);
        if (dislocatedJaw > 0) {
            message = DislocatedJaw(message);
        } else if (JawMissing) {
            message = MissingJaw(message);
        }

        return message;
    }

    private static String distortScaled(String input, float clarity) {
        Random r = new Random();

        // The lower the clarity, the higher the error chance
        float dropChance = (1f - clarity) * 0.35f;      // up to 25% dropped letters
        float dupChance = (1f - clarity) * 0.25f;      // up to 15% doubles
        float caseChance = (1f - clarity) * 0.45f;       // up to 10% random case
        float gibChance = (1f - clarity) * 0.3f;       // up to 20% gibberish

        String[] gib = {"rrh", "h", "mn", "zz", "thh", "nn"};

        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (r.nextFloat() < dropChance) continue;

            if (r.nextFloat() < gibChance)
                sb.append(gib[r.nextInt(gib.length)]);

            if (r.nextFloat() < dupChance)
                sb.append(c);

            if (r.nextFloat() < caseChance && Character.isLetter(c))
                c = r.nextBoolean() ? Character.toUpperCase(c) : Character.toLowerCase(c);

            sb.append(c);
        }

        return sb.toString();
    }

    private static String MissingJaw(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            boolean drop = isSame(c, 'b') ||
                    isSame(c, 'p') ||
                    isSame(c, 'm') ||
                    isSame(c, 'f') ||
                    isSame(c, 't') ||
                    isSame(c, 'h') ||
                    isSame(c, 'v');
            if (isSame(c, 't')) {
                sb.append("tgh");
            }
            if (isSame(c, 'd')) {
                sb.append("dgh");
            }
            if (isSame(c, 'n')) {
                sb.append("ngh");
            }
            if (isSame(c, 'l')) {
                sb.append("lh");
            }
            if (isSame(c, 's')) {
                sb.append("h");
            }
            if (isSame(c, 'z')) {
                sb.append("zh");
            }
            if (drop) {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String DislocatedJaw(String input) {
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            boolean drop = isSame(c, 'b') ||
                    isSame(c, 'p') ||
                    isSame(c, 'm') ||
                    isSame(c, 'f') ||
                    isSame(c, 'v');

            if (isSame(c, 's')) {
                sb.append("th");
            }
            if (isSame(c, 'z')) {
                sb.append("ey");
            }
            if (isSame(c, 't') || isSame(c, 'd')) {
                sb.append("h");
            }
            if (drop) {
                sb.append("_");
            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    public static boolean isSame(char a, char b) {
        return Character.toLowerCase(a) == Character.toLowerCase(b);
    }
}
