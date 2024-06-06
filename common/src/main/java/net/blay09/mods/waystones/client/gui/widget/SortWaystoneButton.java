package net.blay09.mods.waystones.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class SortWaystoneButton extends Button {

    private static final ResourceLocation MOVE_UP_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("server_list/move_up_highlighted");
    private static final ResourceLocation MOVE_UP_SPRITE = ResourceLocation.withDefaultNamespace("server_list/move_up");
    private static final ResourceLocation MOVE_DOWN_HIGHLIGHTED_SPRITE = ResourceLocation.withDefaultNamespace("server_list/move_down_highlighted");
    private static final ResourceLocation MOVE_DOWN_SPRITE = ResourceLocation.withDefaultNamespace("server_list/move_down");

    private final int sortDir;
    private final int visibleRegionStart;
    private final int visibleRegionHeight;

    public SortWaystoneButton(int x, int y, int sortDir, int visibleRegionStart, int visibleRegionHeight, OnPress pressable) {
        super(x, y, 11, 7, Component.empty(), pressable, Button.DEFAULT_NARRATION);
        this.sortDir = sortDir;
        this.visibleRegionStart = visibleRegionStart;
        this.visibleRegionHeight = visibleRegionHeight;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
        if (mouseY >= visibleRegionStart && mouseY < visibleRegionStart + visibleRegionHeight) {
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            int renderY = getY() - (sortDir == 1 ? 20 : 5);
            RenderSystem.enableBlend();
            if (active && isHovered) {
                guiGraphics.setColor(1f, 1f, 1f, 1f);
            } else if (active) {
                guiGraphics.setColor(1f, 1f, 1f, 0.75f);
            } else {
                guiGraphics.setColor(1f, 1f, 1f, 0.25f);
            }

            if (isHovered && active) {
                guiGraphics.blitSprite(sortDir == 1 ? MOVE_DOWN_HIGHLIGHTED_SPRITE : MOVE_UP_HIGHLIGHTED_SPRITE, getX() - 5, renderY, 32, 32);
            } else {
                guiGraphics.blitSprite(sortDir == 1 ? MOVE_DOWN_SPRITE : MOVE_UP_SPRITE, getX() - 5, renderY, 32, 32);
            }

            RenderSystem.disableBlend();
        }
    }

}
