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

    public static final int MOODLE_SIZE = 20;
    public static final int PADDING = 4;

    private static final OverflowMoodle overflowMoodle = new OverflowMoodle();

    private static final List<AbstractMoodle> moodles;
    private static final List<AbstractMoodle> toRender = new ArrayList<>();

    /**
     * Collects all visible moodles for given player. Returned list is reused.
     */
    @SuppressWarnings("SameReturnValue")
    public static List<AbstractMoodle> updateAndGetToRender(Player player, boolean sideMoodles) {
        toRender.clear();

        PlayerHealthData data = PlayerHealthData.of(player).orElse(null);
        if (data == null) return toRender;

        ChipState state = data.getChip();
        for (AbstractMoodle moodle : moodles) {
            if (moodle.isSideMoodle() && !sideMoodles) continue;
            if (!moodle.shouldBeDisplayed(state)) continue;

            moodle.update(player, data);
            if (moodle.shouldRender()) toRender.add(moodle);
        }

        return toRender;
    }

    static {
        List<AbstractMoodle> tmp = new ArrayList<>();

        tmp.add(new LifeSupportMoodle());//not in CU


        tmp.add(new LastStandMoodle());
        tmp.add(new BrainHealthMoodle());
        tmp.add(new StrokeMoodle());
        tmp.add(new CardiacArrestMoodle());
        tmp.add(new ArrhythmiaMoodle());
        tmp.add(new BloodPressureMoodle());
        tmp.add(new HypoventilationMoodle());
        tmp.add(new LungFailureMoodle());
        tmp.add(new HemothoraxMoodle());
        tmp.add(new OxygenMoodle());
        //irradiated
        tmp.add(new PainMoodle());
        tmp.add(new OpiateMoodle());
        tmp.add(new WithdrawalMoodle());
        tmp.add(new ShockMoodle());
        tmp.add(new StimulatedMoodle());
        tmp.add(new ConcussionMoodle());
        tmp.add(new SleepMoodle());
        tmp.add(new ConsciousnessMoodle());
        tmp.add(new DrugOverdoseMoodle());
        tmp.add(new InternalBleedingMoodle());
        tmp.add(new BleedingMoodle());
        //exertion (stamina)
        tmp.add(new FractureMoodle());
        tmp.add(new DislocationMoodle());
        tmp.add(new FracturedNeckMoodle());//TODO severity based on fracture time
        tmp.add(new FracturedRibsMoodle());
        tmp.add(new DislocatedJawMoodle());//TODO severity based on dislocation time
        tmp.add(new DislocatedSpineMoodle());
        tmp.add(new InfectionMoodle());
        tmp.add(new SepsisMoodle());
        tmp.add(new ToxicosisMoodle());
        //tiredness (energy)
        tmp.add(new HungerMoodle());
        //thirst
        //overhydration
        tmp.add(new SicknessMoodle());
        tmp.add(new TemperatureMoodle());
        //happiness
        //claw health
        tmp.add(new HearingLossMoodle());
        tmp.add(new DirtinessMoodle());
        //encumbrance
        tmp.add(new WetnessMoodle());
        tmp.add(new ImmunityMoodle());
        //keratin booster
        //under/overweight
        //trauma
        tmp.add(new EnergizedMoodle());
        //bad sleep
        tmp.add(new ImpairedSpeechMoodle());
        tmp.add(new BrainGrowSicknessMoodle());
        tmp.add(new DisfiguredMoodle());
        tmp.add(new AmputatedMoodle());
        tmp.add(new BlindMoodle());
        tmp.add(new AdrenalineMoodle());
        //hollow

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

        List<AbstractMoodle> visible = updateAndGetToRender(minecraft.player, false);

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
