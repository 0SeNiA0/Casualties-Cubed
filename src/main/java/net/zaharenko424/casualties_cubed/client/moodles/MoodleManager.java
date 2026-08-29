package net.zaharenko424.casualties_cubed.client.moodles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.common.MinecraftForge;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.Keybinds;
import net.zaharenko424.casualties_cubed.client.event.RegisterMoodlesEvent;
import net.zaharenko424.casualties_cubed.client.gui.minigames.Minigame;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.client.gui.widget.ImageButton;
import net.zaharenko424.casualties_cubed.client.gui.widget.RenderableImage;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoodleManager {

    public static final int MOODLE_SIZE = 20;
    public static final int PADDING = 3;

    public static final ImageButton HEALTH_PANEL_BUTTON = new ImageButton(32, 32, new RenderableImage(CasualtiesCubed.texLoc("gui/health_panel"), 32, 32), () -> {
        if (Minecraft.getInstance().screen instanceof HealthScreen screen) screen.onClose();
    });
    private static final RenderableImage TIME_WARP = new RenderableImage(CasualtiesCubed.texLoc("gui/time_warp"), 64, 16);

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

        for (AbstractMoodle moodle : moodles) {
            if (moodle.isSideMoodle() && !sideMoodles) continue;
            if (!moodle.shouldBeDisplayed(data)) continue;

            moodle.update(player, data);
            if (moodle.shouldRender()) toRender.add(moodle);
        }

        return toRender;
    }

    static {
        HEALTH_PANEL_BUTTON.tooltip(Component.translatable("tooltip.casualties_cubed.health_panel_button", Component.keybind(Keybinds.OPEN_PAIN_GUI.getName())));
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
        tmp.add(new IrradiatedMoodle());
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
        tmp.add(new StaminaMoodle());
        tmp.add(new FractureMoodle());
        tmp.add(new DislocationMoodle());
        tmp.add(new FracturedNeckMoodle());
        tmp.add(new FracturedRibsMoodle());
        tmp.add(new DislocatedJawMoodle());
        tmp.add(new DislocatedSpineMoodle());
        tmp.add(new InfectionMoodle());
        tmp.add(new SepsisMoodle());
        tmp.add(new ToxicosisMoodle());
        tmp.add(new EnergyMoodle());
        tmp.add(new HungerMoodle());
        tmp.add(new ThirstMoodle());
        tmp.add(new SicknessMoodle());
        tmp.add(new TemperatureMoodle());
        tmp.add(new HappinessMoodle());
        //claw health
        tmp.add(new HearingLossMoodle());
        tmp.add(new DirtinessMoodle());
        //encumbrance
        tmp.add(new WetnessMoodle());
        tmp.add(new ImmunityMoodle());
        //keratin booster
        tmp.add(new WeightMoodle());
        //trauma
        tmp.add(new EnergizedMoodle());
        tmp.add(new BadSleepMoodle());
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
        Screen screen = gui.getMinecraft().screen;
        if (screen instanceof HealthScreen || screen instanceof Minigame) return;
        render(graphics, partialTick, width, height, true, 0, 0);
    }

    /**
     * Render moodles as overlay (left-bottom, respecting hotbar)
     */
    public static void render(GuiGraphics graphics, float partialTick, int width, int height, boolean hotbar, int mouseX, int mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        ProfilerFiller profiler = minecraft.getProfiler();
        profiler.push(CasualtiesCubed.MOD_ID + ":moodles");

        List<AbstractMoodle> visible = updateAndGetToRender(minecraft.player, !hotbar);

        PoseStack stack = graphics.pose();
        stack.pushPose();
        if (hotbar) stack.translate(0, 0, 150);

        int hotbarLeft = hotbar ? (width / 2) - 91 : (int) (width * 0.667f);
        int x = 0;
        int y = height - MOODLE_SIZE - 2;

        HEALTH_PANEL_BUTTON.offset.set(16, height - 20, 0);
        HEALTH_PANEL_BUTTON.render(graphics, mouseX, mouseY, partialTick);
        x += 32 + 5;

        AbstractMoodle hovered = null, moodle;
        for (int i = 0; i < visible.size(); i++) {
            if (x + MOODLE_SIZE + 16 > hotbarLeft) {
                overflowMoodle.setLeftover(visible.size() - i);
                overflowMoodle.render(graphics, partialTick, x, y);
                break;
            }

            moodle = visible.get(i);
            moodle.render(graphics, partialTick, x, y);

            if (!hotbar && moodle.isMouseOver(mouseX, mouseY, x, y)) {
                hovered = moodle;
            }

            x += MOODLE_SIZE + PADDING;
        }

        if (!hotbar && hovered != null) {
            graphics.renderTooltip(minecraft.font, hovered.getTooltip(player), Optional.empty(), mouseX, mouseY);
        }

        TIME_WARP.offset.set(width - 32, height - 11, 0);
        TIME_WARP.render(graphics, mouseX, mouseY, partialTick);

        stack.popPose();
        profiler.pop();
    }
}
