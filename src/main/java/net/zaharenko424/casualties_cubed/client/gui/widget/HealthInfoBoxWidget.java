package net.zaharenko424.casualties_cubed.client.gui.widget;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.screen.HealthScreen;
import net.zaharenko424.casualties_cubed.limbs.*;
import net.zaharenko424.casualties_cubed.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HealthInfoBoxWidget extends AbstractWidget {

    private final RenderableImage background = new RenderableImage(newTex, 159, 265).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage glow = new RenderableImage(newTexGlow, 126, 134).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage limbGlow = new RenderableImage(newTexLimb, 128, 66).offsetMode(OffsetMode.TOP_LEFT);

    private final RenderableImage brainBarImg = new RenderableImage(brainBar, 96, 2).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage bloodBarImg = new RenderableImage(bloodBar, 2, 19).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);
    private final RenderableImage thermometerBarImg = new RenderableImage(thermometerBar, 7, 24).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);
    private final RenderableImage waterBarImg = new RenderableImage(waterBar, 49, 1).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage waterBarOverfillImg = new RenderableImage(waterBarOrange, 49, 1).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.RIGHT_TO_LEFT);
    private final RenderableImage foodBarImg = new RenderableImage(foodBar, 61, 1).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage foodBarOverfillImg = new RenderableImage(foodBarOrange, 61, 1).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.RIGHT_TO_LEFT);
    private final RenderableImage staminaRight = new RenderableImage(lungFillR, 7, 15).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);
    private final RenderableImage staminaLeft = new RenderableImage(lungFillL, 7, 15).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);
    private final RenderableImage hemothoraxFillImg = new RenderableImage(hemothoraxFill, 22, 17).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);

    private final RenderableImage muscleBar = new RenderableImage(limbBar, 88, 3).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage skinBar = new RenderableImage(limbBar, 88, 3).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage forceBarImg = new RenderableImage(forceBar, 19, 19).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);
    private final RenderableImage fracturedBoneImg = new RenderableImage(fracturedBone, 20, 27).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage dislocatedBoneImg = new RenderableImage(dislocatedBone, 20, 27).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage limbBleedOuterImg = new RenderableImage(limbBleedOuter, 33, 23).offsetMode(OffsetMode.TOP_LEFT);
    private final RenderableImage limbBleedInnerImg = new RenderableImage(limbBleedInner, 11, 17).offsetMode(OffsetMode.TOP_LEFT).fillMode(RenderableImage.FillMode.BOTTOM_TO_TOP);

    private static final float textScale = 0.75f;

    private final RenderableText brainText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText consciousnessText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText avgPainText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText bloodText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText bleedText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText immunityText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText temperatureText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText waterText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText foodText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText sicknessText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText weightText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText happinessText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText energyText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText respRateText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText radText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText heartRatePressureText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText oxygenText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);

    private final RenderableText limbText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.CENTER);
    private final RenderableText muscleText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText skinText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText forceText = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);
    private final RenderableText infectionText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText painText = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText injuryHealTime = new RenderableText().scale(textScale, textScale, 1);
    private final RenderableText limbBleed = new RenderableText().scale(textScale, textScale, 1).alignment(RenderableText.TextAlignment.RIGHT);

    private final Player target;
    private final PlayerHealthData data;

    private Limb selectedLimb = Limb.HEAD;


    public HealthInfoBoxWidget(int pX, int pY, int pWidth, int pHeight, Player target, PlayerHealthData data) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.target = target;
        this.data = data;

        background.offset.set(6, 6, 0);
        glow.offset.set(6 + 4, 6 + 31, 0);
        limbGlow.offset.set(6 + 3, 6 + 4 + 187 + 4, 0);

        brainBarImg.offset.set(6 + 4 + 24, 6 + 31 + 4, 0);
        bloodBarImg.offset.set(6 + 4 + 5, 6 + 31 + 18, 0);
        thermometerBarImg.offset.set(6 + 4 + 114, 6 + 31 + 35, 0);
        waterBarImg.offset.set(6 + 4 + 15, 6 + 31 + 48, 0);
        waterBarOverfillImg.offset.set(waterBarImg.offset);
        foodBarImg.offset.set(6 + 4 + 15, 6 + 31 + 59, 0);
        foodBarOverfillImg.offset.set(foodBarImg.offset);
        staminaRight.offset.set(6 + 4 + 5, 6 + 31 + 93, 0);
        staminaLeft.offset.set(6 + 4 + 18, 6 + 31 + 93, 0);
        hemothoraxFillImg.offset.set(6 + 4 + 4, 6 + 31 + 92, 0);

        muscleBar.offset.set(6 + 4 + 5, 6 + 31 + 180, 0);
        skinBar.offset.set(6 + 4 + 5, 6 + 31 + 190, 0);
        forceBarImg.offset.set(6 + 4 + 5, 6 + 31 + 206, 0);
        fracturedBoneImg.offset.set(6 + 4 + 70, 6 + 31 + 200, 0);
        dislocatedBoneImg.offset.set(fracturedBoneImg.offset);
        limbBleedOuterImg.offset.set(6 + 4 + 93, 6 + 31 + 204, 0);
        limbBleedInnerImg.offset.set(6 + 4 + 113, 6 + 31 + 208, 0);

        brainText.offset.set(6 + 4 + 21, 46, 0);
        consciousnessText.offset.set(6 + 4 + 115, 48, 0);
        avgPainText.offset.set(6 + 4 + 115, 57, 0);
        bloodText.offset.set(6 + 4 + 18, 58, 0);
        bleedText.offset.set(6 + 4 + 18, 69, 0);
        immunityText.offset.set(6 + 4 + 76, 58, 0);
        temperatureText.offset.set(6 + 4 + 114, 77, 0);
        waterText.offset.set(6 + 4 + 70, 82, 0);
        foodText.offset.set(6 + 4 + 80, 94, 0);
        sicknessText.offset.set(6 + 4 + 24, 104, 0);
        weightText.offset.set(6 + 4 + 24, 114, 0);
        happinessText.offset.set(6 + 4 + 110, 104, 0);
        energyText.offset.set(6 + 4 + 111, 114, 0);
        respRateText.offset.set(6 + 4 + 5, 149, 0);
        radText.offset.set(6 + 4 + 121, 148, 0);
        heartRatePressureText.offset.set(6 + 4 + 12, 163, 0);
        oxygenText.offset.set(6 + 4 + 115, 163, 0);

        limbText.offset.set(6 + 67, 204, 0);
        muscleText.offset.set(6 + 4 + 112, 216, 0);
        skinText.offset.set(6 + 4 + 112, 226, 0);
        forceText.offset.set(6 + 4 + 22, 234, 0);
        infectionText.offset.set(6 + 4 + 41, 241, 0);
        painText.offset.set(6 + 4 + 41, 253, 0);
        injuryHealTime.offset.set(6 + 4 + 60, 250, 0);
        limbBleed.offset.set(6 + 4 + 110, 6 + 31 + 209, 0);
    }

    public void setSelectedLimb(Limb limb) {
        this.selectedLimb = limb;
        limbText.component(limb.comp.copy().withStyle(ChatFormatting.BOLD));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int glowColor = FastColor.ARGB32.color(255, 47, 224, 129);

        background.render(guiGraphics, partialTick);
        glow.tint = glowColor;
        glow.render(guiGraphics, partialTick);
        limbGlow.tint = glowColor;
        limbGlow.render(guiGraphics, partialTick);

        Minecraft mc = Minecraft.getInstance();
        guiGraphics.drawCenteredString(mc.font, target.getName(), 6 + 67, 6 + 8, glowColor);

        renderTintedFilledImage(brainBarImg, guiGraphics, partialTick, glowColor, data.brainHealth() * 0.01f);
        brainText.component(Component.literal("" + Math.round(data.brainHealth())));
        renderFlashingText(brainText, guiGraphics, glowColor, data.brainHealth() < 50);

        consciousnessText.component(Component.literal(Math.round(data.getConsciousness()) + "%"));
        renderFlashingText(consciousnessText, guiGraphics, glowColor, data.getConsciousness() < 40);

        avgPainText.component(Component.literal(Math.round(data.getAveragePain()) + "%"));
        renderFlashingText(avgPainText, guiGraphics, glowColor, data.getAveragePain() > 75);

        renderTintedFilledImage(bloodBarImg, guiGraphics, partialTick, glowColor, data.getBloodVolume() / 5);
        bloodText.component(Component.translatable("casualties_cubed.gui.health.blood_volume", String.format("%.2f", data.getBloodVolume())));
        renderFlashingText(bloodText, guiGraphics, glowColor, data.getBloodVolume() < Util.CUBloodPointsToL(25));

        //blood loss bleed * 20 * 60
        float totalBleed = data.totalBleedSpeed() * 20;
        if (totalBleed > Util.CUBloodPointsToL(0.0001f)) {
            bleedText.component(Component.translatable("casualties_cubed.gui.health.bleed_rate", String.format("%.2f", data.totalBleedSpeed() * 60)));
            renderFlashingText(bleedText, guiGraphics, glowColor, totalBleed > Util.CUBloodPointsToL(0.06f));
        }

        immunityText.component(Component.literal(Math.round(data.getImmunity()) + "%"));
        renderFlashingText(immunityText, guiGraphics, glowColor, data.getImmunity() < 50);

        renderTintedFilledImage(thermometerBarImg, guiGraphics, partialTick, glowColor, (data.getTemperature() - 28) * 0.08f);
        temperatureText.component(Component.literal(String.format("%.1f", data.getTemperature()) + "°c"));
        renderFlashingText(temperatureText, guiGraphics, glowColor, data.getTemperature() < 30 || data.getTemperature() > 41);

        renderTintedFilledImage(waterBarImg, guiGraphics, partialTick, glowColor, data.thirst() * 0.01f);
        waterBarOverfillImg.fillAmount = (data.thirst() - 100) * 0.01f;
        waterBarOverfillImg.render(guiGraphics, partialTick);
        waterText.component(Component.literal(Math.round(data.thirst()) + ""));
        renderFlashingText(waterText, guiGraphics, glowColor, data.thirst() < 10);

        renderTintedFilledImage(foodBarImg, guiGraphics, partialTick, glowColor, data.hunger() * 0.01f);
        foodBarOverfillImg.fillAmount = (data.hunger() - 100) * 0.01f;
        foodBarOverfillImg.render(guiGraphics, mouseX, mouseY, partialTick);
        foodText.component(Component.literal(Math.round(data.hunger()) + ""));
        renderFlashingText(foodText, guiGraphics, glowColor, data.hunger() < 10);

        renderTintedFilledImage(staminaRight, guiGraphics, partialTick, glowColor, data.stamina() / 50);
        renderTintedFilledImage(staminaLeft, guiGraphics, partialTick, glowColor,  (data.stamina() - 50) / 50);

        hemothoraxFillImg.fillAmount = data.getHemothorax() * 0.01f;
        hemothoraxFillImg.render(guiGraphics, mouseX, mouseY, partialTick);

        sicknessText.component(Component.literal(Math.round(data.getSickness()) + "%"));
        renderFlashingText(sicknessText, guiGraphics, glowColor, data.getSickness() > 70);

        weightText.component(Component.literal(Util.ONE_OPTIONAL.format(data.weightOffset() * 0.34f + 50) + "kg"));
        renderFlashingText(weightText, guiGraphics, glowColor, Math.abs(data.weightOffset()) > 55);

        happinessText.component(Component.literal(String.format("%.2f", data.happiness())));
        renderFlashingText(happinessText, guiGraphics, glowColor, data.happiness() <= -75);

        energyText.component(Component.literal(Math.round(data.energy()) + "%"));
        renderFlashingText(energyText, guiGraphics, glowColor, data.energy() < 5);

        respRateText.component(Component.literal(Math.round(data.respiratoryRate() * 0.25f) + "/m"));
        renderFlashingText(respRateText, guiGraphics, glowColor, data.respiratoryRate() < 0.5f);

        radText.component(Component.literal(String.format("%.1fgy", data.radiationSickness() * 0.3f)));
        renderFlashingText(radText, guiGraphics, glowColor, data.radiationSickness() > 25);

        float bloodPressure = data.bloodPressure();
        heartRatePressureText.component(Component.literal(Math.round(data.getHeartRate()) + " | " + Math.round(bloodPressure) + "/" + Math.round(bloodPressure * 0.66f)));
        renderFlashingText(heartRatePressureText, guiGraphics, glowColor, data.isCardiacArrest() || data.fibrillationRising() || bloodPressure > 160 || bloodPressure < 75);

        oxygenText.component(Component.literal(Math.round(data.getBloodOxygen()) + "%"));
        renderFlashingText(oxygenText, guiGraphics, glowColor, data.getBloodOxygen() < 70);


        //Limb stats
        limbText.color = glowColor;
        limbText.render(guiGraphics);
        LimbStatistics stats = data.getLimb(selectedLimb);

        renderTintedFilledImage(muscleBar, guiGraphics, partialTick, glowColor, stats.getMuscleHealth() * 0.01f);
        muscleText.component(Component.literal(Math.round(stats.getMuscleHealth()) + ""));
        renderFlashingText(muscleText, guiGraphics, glowColor, stats.getMuscleHealth() < 10);

        renderTintedFilledImage(skinBar, guiGraphics, partialTick, glowColor, stats.getSkinHealth() * 0.01f);
        skinText.component(Component.literal(Math.round(stats.getSkinHealth()) + ""));
        renderFlashingText(skinText, guiGraphics, glowColor, stats.getSkinHealth() < 10);

        renderTintedFilledImage(forceBarImg, guiGraphics, partialTick, glowColor, stats.totalForce());
        forceText.component(Component.literal(Math.round(stats.totalForce() * 100) + ""));
        renderFlashingText(forceText, guiGraphics, glowColor, stats.totalForce() < 0.25f);

        infectionText.component(Component.literal(stats.getInfection() >= 25 || stats.showInfection() ? Math.round(stats.getInfection()) + "" : "-"));
        renderFlashingText(infectionText, guiGraphics, glowColor, stats.getInfection() >= 25);

        painText.component(Component.literal(stats.getPain() <= 0 ? "-" : Math.round(stats.getPain()) + ""));
        renderFlashingText(painText, guiGraphics, glowColor, stats.getPain() >= 50);

        RenderableImage injury = null;
        if (stats.getBoneHealTimer() > 0) {
            injury = fracturedBoneImg;
        } else if (stats.getDislocationTimer() > 0) {
            injury = dislocatedBoneImg;
        }

        if (injury != null) {
            injury.tint = glowColor;
            injury.render(guiGraphics, mouseX, mouseY, partialTick);

            float time = stats.injuryHealTime();
            renderText(injuryHealTime, guiGraphics, Component.literal(TimeUnit.SECONDS.toMinutes(Math.round(time)) + "m\n" + Math.round(time % 60) + "s"), glowColor);
        }

        renderSkill(guiGraphics, partialTick, glowColor);

        float bleedAmount = stats.getBleedRate() / stats.bleedSpeedMult() * 20;
        if (bleedAmount <= Util.CUBloodPointsToL(0.1f)) return;

        limbBleedOuterImg.tint = glowColor;
        limbBleedOuterImg.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTintedFilledImage(limbBleedInnerImg, guiGraphics, partialTick, glowColor, bleedAmount / Util.CUBloodPointsToL(50));
        limbBleed.component(Component.literal(String.format("%.2f", stats.getBleedRate() * 20 * 60) + "\nl/m"));
        renderFlashingText(limbBleed, guiGraphics, glowColor, bleedAmount > Util.CUBloodPointsToL(8));
    }

    private final List<Renderable> renderables = new ArrayList<>();

    private final RenderableImage skillBg = addRenderable(new RenderableImage(SKILL_BG, 134, 72));
    private final RenderableImage skillOutline = addRenderable(new RenderableImage(SKILL_OUTLINE, 128, 66));

    private final RenderableImage strBar = addRenderable(new RenderableImage(skillBar, 96, 8).offsetMode(OffsetMode.TOP_LEFT));
    private final RenderableText strLvlText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText strMinText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText strCurText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText strMaxText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableImage resBar = addRenderable(new RenderableImage(skillBar, 96, 8).offsetMode(OffsetMode.TOP_LEFT));
    private final RenderableText resLvlText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText resMinText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText resCurText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText resMaxText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableImage intBar = addRenderable(new RenderableImage(skillBar, 96, 8).offsetMode(OffsetMode.TOP_LEFT));
    private final RenderableText intLvlText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText intMinText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText intCurText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);
    private final RenderableText intMaxText = addRenderable(new RenderableText()).scale(textScale, textScale, 1);

    private <R extends Renderable> R addRenderable(R renderable) {
        renderables.add(renderable);
        return renderable;
    }

    public void init(HealthScreen screen) {
        int width = screen.width;
        skillBg.offset.set(width - 5 - 67, 4 + 36, 0);
        skillOutline.offset.set(width - 5 - 67, 4 + 36, 0);

        strBar.offset.set(width - 110, 10, 0);
        strLvlText.offset.set(width - 132, 22, 0);
        strMinText.offset.set(width - 110, 22, 0);
        strCurText.offset.set(width - 78, 22, 0);
        strMaxText.offset.set(width - 44, 22, 0);
        resBar.offset.set(width - 110, 30, 0);
        resLvlText.offset.set(width - 132, 42, 0);
        resMinText.offset.set(width - 110, 42, 0);
        resCurText.offset.set(width - 78, 42, 0);
        resMaxText.offset.set(width - 44, 42, 0);
        intBar.offset.set(width - 110, 50, 0);
        intLvlText.offset.set(width - 132, 62, 0);
        intMinText.offset.set(width - 110, 62, 0);
        intCurText.offset.set(width - 78, 62, 0);
        intMaxText.offset.set(width - 44, 62, 0);
    }

    private void renderSkill(GuiGraphics graphics, float partialTick, int glowColor) {
        Skills skills = data.skills;
        skillOutline.tint = strBar.tint = resBar.tint = intBar.tint = glowColor;
        strBar.fillAmount = skills.skillToNext(Stat.STR);
        resBar.fillAmount = skills.skillToNext(Stat.RES);
        intBar.fillAmount = skills.skillToNext(Stat.INT);
        strLvlText.component(Component.literal(skills.level(Stat.STR) + "")).color = glowColor;
        resLvlText.component(Component.literal(skills.level(Stat.RES) + "")).color = glowColor;
        intLvlText.component(Component.literal(skills.level(Stat.INT) + "")).color = glowColor;
        strMinText.component(Component.literal(Math.round(skills.minExp(Stat.STR)) + "")).color = glowColor;
        resMinText.component(Component.literal(Math.round(skills.minExp(Stat.RES)) + "")).color = glowColor;
        intMinText.component(Component.literal(Math.round(skills.minExp(Stat.INT)) + "")).color = glowColor;
        strCurText.component(Component.literal(Math.round(skills.exp(Stat.STR)) + "")).color = glowColor;
        resCurText.component(Component.literal(Math.round(skills.exp(Stat.RES)) + "")).color = glowColor;
        intCurText.component(Component.literal(Math.round(skills.exp(Stat.INT)) + "")).color = glowColor;
        strMaxText.component(Component.literal(Math.round(skills.maxExp(Stat.STR)) + "")).color = glowColor;
        resMaxText.component(Component.literal(Math.round(skills.maxExp(Stat.RES)) + "")).color = glowColor;
        intMaxText.component(Component.literal(Math.round(skills.maxExp(Stat.INT)) + "")).color = glowColor;

        renderables.forEach(renderable -> renderable.render(graphics, 0 ,0, partialTick));
    }

    private void renderTintedFilledImage(RenderableImage image, GuiGraphics graphics, float partialTick, int tint, float fillAmount) {
        image.tint = tint;
        image.fillAmount = fillAmount;
        image.render(graphics, partialTick);
    }

    private final int flashingColor = FastColor.ARGB32.color(255, 255, 0, 8);

    private void renderText(RenderableText text, GuiGraphics graphics, Component comp, int color) {
        text.component(comp).color = color;
        text.render(graphics);
    }

    private void renderFlashingText(RenderableText text, GuiGraphics graphics, int color, boolean flashing) {
        if (flashing && Math.sin(System.currentTimeMillis() / 1000f * 20) <= 0) return;

        text.color = flashing ? flashingColor : color;
        text.render(graphics);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

    private static final ResourceLocation newTex = CasualtiesCubed.texLoc("gui/computer_back");
    private static final ResourceLocation newTexGlow = CasualtiesCubed.texLoc("gui/computer_glow");
    private static final ResourceLocation newTexLimb = CasualtiesCubed.texLoc("gui/computer_limb_glow");
    private static final ResourceLocation brainBar = CasualtiesCubed.texLoc("gui/bar_brain");
    private static final ResourceLocation bloodBar = CasualtiesCubed.texLoc("gui/bar_blood");
    private static final ResourceLocation thermometerBar = CasualtiesCubed.texLoc("gui/bar_thermometer");
    private static final ResourceLocation waterBar = CasualtiesCubed.texLoc("gui/bar_water");
    private static final ResourceLocation waterBarOrange = CasualtiesCubed.texLoc("gui/bar_water_orange");
    private static final ResourceLocation foodBar = CasualtiesCubed.texLoc("gui/bar_food");
    private static final ResourceLocation foodBarOrange = CasualtiesCubed.texLoc("gui/bar_food_orange");
    private static final ResourceLocation lungFillR = CasualtiesCubed.texLoc("gui/lung_fill_right");
    private static final ResourceLocation lungFillL = CasualtiesCubed.texLoc("gui/lung_fill_left");
    private static final ResourceLocation hemothoraxFill = CasualtiesCubed.texLoc("gui/hemothorax_fill");
    private static final ResourceLocation limbBar = CasualtiesCubed.texLoc("gui/bar_limb");
    private static final ResourceLocation forceBar = CasualtiesCubed.texLoc("gui/bar_force");
    private static final ResourceLocation fracturedBone = CasualtiesCubed.texLoc("gui/fractured_bone");
    private static final ResourceLocation dislocatedBone = CasualtiesCubed.texLoc("gui/dislocated_bone");
    private static final ResourceLocation limbBleedOuter = CasualtiesCubed.texLoc("gui/limb_bleed_outer");
    private static final ResourceLocation limbBleedInner = CasualtiesCubed.texLoc("gui/limb_bleed_inner");
    private static final ResourceLocation skillBar = CasualtiesCubed.texLoc("gui/skill_bar");
    private static final ResourceLocation SKILL_BG = CasualtiesCubed.texLoc("gui/screen_skills");
    private static final ResourceLocation SKILL_OUTLINE = CasualtiesCubed.texLoc("gui/screen_skills_glow");
}
