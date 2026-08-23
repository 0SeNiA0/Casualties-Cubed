package net.zaharenko424.casualties_cubed.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.zaharenko424.casualties_cubed.CasualtiesCubed;
import net.zaharenko424.casualties_cubed.client.gui.StatusSprites;
import net.zaharenko424.casualties_cubed.limbs.Limb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.casualties_cubed.limbs.LimbStatistics;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;

import java.util.*;

public class LimbWidget extends AbstractWidget {

    private final Limb limb;
    private final PlayerHealthData data;
    private final LimbStatistics stats;
    private final ResourceLocation borderTxt;
    private final ResourceLocation baseTxt;
    Random random = new Random();
    private float shake = 0;
    private float border_red = 0;
    private float base_red = 0;
    private boolean amputated;
    private final int txt_height;
    private final int txt_width;

    private boolean LeftEyeGone = false;
    private boolean RightEyeGone = false;
    private boolean MouthGone = false;

    public void setMouthGone(boolean mouthGone) {
        MouthGone = mouthGone;
    }

    public void setRightEyeGone(boolean rightEyeGone) {
        RightEyeGone = rightEyeGone;
    }

    public void setLeftEyeGone(boolean leftEyeGone) {
        LeftEyeGone = leftEyeGone;
    }

    public void setAmputated(boolean amputated) {
        this.amputated = amputated;
        visible = !amputated;
    }

    public boolean isAmputated() {
        return amputated;
    }

    private final Map<StatusSprites, SubSprite> subSprites = new EnumMap<>(StatusSprites.class);
    private boolean expanded = false;

    public LimbWidget(Limb limb, PlayerHealthData data) {
        super(0, 0, 0, 0, Component.empty());
        this.limb = limb;
        this.data = data;
        stats = data.getLimb(limb);
        switch (limb) {
            case RIGHT_FOOT, LEFT_FOOT, RIGHT_HAND, LEFT_HAND -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/end_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/end_base.png");
                txt_height = 16;
                txt_width = 16;
                this.width = 16;
                this.height = 16;
            }
            case LOWER_LEFT_ARM, UPPER_RIGHT_ARM -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_limb_horizontal_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_limb_horizontal_base.png");
                txt_height = 16;
                txt_width = 24;
                this.width = 24;
                this.height = 16;
            }
            case UPPER_LEFT_ARM, LOWER_RIGHT_ARM -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_limb_horizontal_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_limb_horizontal_base.png");
                txt_height = 16;
                txt_width = 24;
                this.width = 24;
                this.height = 16;
            }
            case UPPER_LEFT_LEG, UPPER_RIGHT_LEG -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_limb_vertical_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_limb_vertical_base.png");
                txt_height = 24;
                txt_width = 16;
                this.width = 16;
                this.height = 24;
            }
            case LOWER_LEFT_LEG, LOWER_RIGHT_LEG -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_limb_vertical_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_limb_vertical_base.png");
                txt_height = 24;
                txt_width = 16;
                this.width = 16;
                this.height = 24;
            }
            case THORAX -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_body_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/upper_body_base.png");
                txt_height = 32;
                txt_width = 32;
                this.width = 32;
                this.height = 32;
            }
            case ABDOMEN -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_body_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/lower_body_base.png");
                txt_height = 32;
                txt_width = 32;
                this.width = 32;
                this.height = 32;
            }
            case HEAD -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/head_border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/head_base.png");
                txt_height = 32;
                txt_width = 32;
                this.width = 32;
                this.height = 32;
            }
            default -> {
                borderTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/border.png");
                baseTxt = CasualtiesCubed.resourceLoc("textures/gui/limbs/base.png");
                txt_height = 64;
                txt_width = 64;
                this.width = 32;
                this.height = 32;
            }
        }
    }

    public Limb getLimb() {
        return limb;
    }

    public void populate_sprites() {
        for (StatusSprites spriteType : StatusSprites.values()) {
            subSprites.put(spriteType, new SubSprite(spriteType, getX(), getY()));
        }
    }

    public void setShake(float shake) {
        this.shake = shake;
    }

    public void setBase_red(float base_red) {
        this.base_red = base_red;
    }

    public void setBorder_red(float border_red) {
        this.border_red = border_red;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        if (amputated) return;
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindForSetup(baseTxt);

        float shakex = getX() + ((random.nextFloat() * 2 - 1) * 2);
        float shakey = getY() + ((random.nextFloat() * 2 - 1) * 2);
        if (random.nextFloat() > Math.pow(shake, 3)) shakex = getX();
        if (random.nextFloat() > Math.pow(shake, 3)) shakey = getY();

        RenderSystem.setShaderColor(
                base_red,
                0,
                0,
                1.0f
        );
        guiGraphics.blit(baseTxt, (int) shakex, (int) shakey, 0, 0, this.width, this.height, this.txt_width, this.txt_height);


        mc.getTextureManager().bindForSetup(borderTxt);

        RenderSystem.setShaderColor(
                1.0f,
                1 - border_red,
                1 - border_red,
                1.0f
        );
        guiGraphics.blit(borderTxt, (int) shakex, (int) shakey, 0, 0, this.width, this.height, txt_width, txt_height);

        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        expanded = isHoveredOrFocused();
        updateSubSpritePositions();
        if (LeftEyeGone) {
            guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/limbs/left_eye.png"), (int) shakex, (int) shakey, 0, 0, this.width, this.height, this.txt_width, this.txt_height);
        }
        if (RightEyeGone) {
            guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/limbs/right_eye.png"), (int) shakex, (int) shakey, 0, 0, this.width, this.height, this.txt_width, this.txt_height);
        }
        if (MouthGone) {
            guiGraphics.blit(CasualtiesCubed.resourceLoc("textures/gui/limbs/mouth.png"), (int) shakex, (int) shakey, 0, 0, this.width, this.height, this.txt_width, this.txt_height);
        }
    }

    public void renderSprites(GuiGraphics guiGraphics) {
        for (SubSprite sprite : subSprites.values()) {
            sprite.render(guiGraphics);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {
    }

    private void updateSubSpritePositions() {
        if (amputated) return;
        List<SubSprite> visibleSprites = subSprites.values().stream()
                .filter(SubSprite::isVisible)
                .toList();

        int spacing = 12;
        if (expanded) {
            int centerIndex = visibleSprites.size() / 2;
            for (int i = 0; i < visibleSprites.size(); i++) {
                SubSprite s = visibleSprites.get(i);
                int offset = (int) ((i - centerIndex) * spacing * s.getScale());

                s.setTargetPosition(getX() + offset + this.width / 2 - 8, getY() - 1 + this.height / 2 - 8);
            }
        } else {
            for (SubSprite s : visibleSprites) {
                s.setTargetPosition(getX() + this.width / 2 - 8, getY() + this.height / 2 - 8);
            }
        }

        // Update all visible sprites (handles bobbing and smooth movement)
        visibleSprites.forEach(SubSprite::update);
    }

    public void setScaleOf(StatusSprites type, float value) {
        subSprites.get(type).setTargetScale(value);
    }

    public void setSubSpriteVisible(StatusSprites type, boolean visible) {
        SubSprite sprite = subSprites.get(type);
        if (sprite != null) {
            sprite.setVisible(visible);
        }
    }

    public boolean isSpritePresent(StatusSprites sprite) {
        return subSprites.get(sprite).isVisible();
    }

    public void update() {
        if (stats.isAmputated()) {
            setAmputated(true);
            return;
        } else setAmputated(false);

        if (limb == Limb.HEAD) {
            setLeftEyeGone(data.isLeftEyeBlind());
            setMouthGone(data.disfigured());
            setRightEyeGone(data.isRightEyeBlind());
        }

        setShake(stats.getPain() / 100f);
        setBorder_red(1 - (stats.getSkinHealth() / 100f));
        setBase_red(1 - (stats.getMuscleHealth() / 100f));

        float bleed = stats.getBleedRate();
        boolean isBleeding = bleed > 0 && !stats.isTourniquet() && !data.isUnderTourniquet(limb);
        if (isBleeding) {
            float scale = Math.max(0.9f, (bleed / data.getMAX_BLEED_RATE()) * 2.5f);
            setScaleOf(StatusSprites.BLEED, scale);
        }

        setSubSpriteVisible(StatusSprites.BLEED, isBleeding);
        setSubSpriteVisible(StatusSprites.DISINFECTION, stats.getDisinfectionTime() > 0);
        setSubSpriteVisible(StatusSprites.FRACTURE, stats.getBoneHealTimer() > 0);
        setSubSpriteVisible(StatusSprites.INFECTION, stats.getInfection() > 25);
        setSubSpriteVisible(StatusSprites.SHRAPNEL, stats.getShrapnel() > 0);
        setSubSpriteVisible(StatusSprites.SPLINT, stats.hasSplint());
        setSubSpriteVisible(StatusSprites.DISLOCATION, stats.getDislocationTimer() > 0);
        setSubSpriteVisible(StatusSprites.TOURNIQUET, stats.isTourniquet());
    }
}
