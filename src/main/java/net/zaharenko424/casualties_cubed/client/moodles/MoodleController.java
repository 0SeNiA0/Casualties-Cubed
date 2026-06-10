package net.zaharenko424.casualties_cubed.client.moodles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.MinecraftForge;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.event.RegisterMoodlesEvent;
import net.zaharenko424.casualties_cubed.client.gui.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.ChipState;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;

public class MoodleController {

    public static final int MOODLE_SIZE = 16;
    public static final int PADDING = 4;

    private static final OverflowMoodle overflowMoodle = new OverflowMoodle();

    private static final List<AbstractMoodleVisual> moodles;
    private static final List<AbstractMoodleVisual> toRender = new ArrayList<>();

    /**
     * Collects all visible moodles for given player. Returned list is reused.
     */
    @SuppressWarnings("SameReturnValue")
    public static List<AbstractMoodleVisual> updateAndGetToRender(Player player, boolean healthPanel) {
        toRender.clear();

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return toRender;

        ChipState state = data.getChip();
        for (AbstractMoodleVisual moodle : moodles) {
            if (!moodle.shouldBeDisplayed(state)) continue;
            if (moodle.isSideMoodle() && !healthPanel) continue;

            moodle.update(player, data);
            if (moodle.shouldRender()) toRender.add(moodle);
        }

        return toRender;
    }

    static {
        List<AbstractMoodleVisual> tmp = new ArrayList<>();

        //Positive
        tmp.add(new LifeSupportMoodle());
            //Side
        tmp.add(new AdrenalineMoodle());

        //Negative
        tmp.add(new BrainHealthMoodle());
        tmp.add(new LowBloodMoodle());
        tmp.add(new HighBloodMoodle());
        tmp.add(new RespiratoryArrestMoodle());
        tmp.add(new LungFaliureMoodle());
        tmp.add(new HemothoraxMoodle());
        tmp.add(new OxygenMoodle());//TODO Split off cardiac arrest
        tmp.add(new PainMoodle());
        tmp.add(new ShockMoodle());
        tmp.add(new OpiateMoodle());
        tmp.add(new WithdrawalMoodle());
        tmp.add(new ConsiousnessMoodle());
        tmp.add(new BleedInternalMoodle());
        tmp.add(new BleedMoodle());
        tmp.add(new FractureMoodle());
        tmp.add(new DislocationMoodle());
        tmp.add(new FracturedNeckMoodle());
        tmp.add(new FracturedRibsMoodle());
        tmp.add(new DislocatedJawMoodle());
        tmp.add(new DislocatedSpineMoodle());
        tmp.add(new InfectionMoodle());
        tmp.add(new TemperatureMoodle());
        tmp.add(new HearingLossMoodle());
        tmp.add(new DirtynessMoodle());
            //Side
        tmp.add(new DisfiguredMoodle());
        tmp.add(new AmputatedMoodle());
        tmp.add(new BlindMoodle());

        MinecraftForge.EVENT_BUS.post(new RegisterMoodlesEvent(tmp));

        moodles = List.copyOf(tmp);
    }

    /**
     * Render moodles as overlay (left-bottom, respecting hotbar)
     */
    public static void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int width, int height) {
        Minecraft minecraft = gui.getMinecraft();
        if (minecraft.screen instanceof HealthScreen) return;//draw moodles inside health screen instead

        Player player = minecraft.player;
        if (player == null) return;

        ProfilerFiller profiler = minecraft.getProfiler();
        profiler.push(CasualtiesCubed.MOD_ID + ":moodles");

        List<AbstractMoodleVisual> visible = updateAndGetToRender(minecraft.player, false);

        PoseStack stack = graphics.pose();
        stack.pushPose();
        stack.translate(0, 0, 150);

        int hotbarLeft = (width / 2) - 91;
        int x = 4;
        int y = height - MOODLE_SIZE - 4;

        for (int i = 0; i < visible.size(); i++) {
            if (x + MOODLE_SIZE + 16 > hotbarLeft) {
                overflowMoodle.setLeftover(visible.size() - i);
                overflowMoodle.render(graphics, partialTick, x, y);
                break;
            }
            visible.get(i).render(graphics, partialTick, x, y);
            x += MOODLE_SIZE + PADDING;
        }

        stack.popPose();
        profiler.pop();
    }
}
