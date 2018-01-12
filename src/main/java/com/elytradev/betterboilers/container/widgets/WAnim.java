package com.elytradev.betterboilers.container.widgets;

import com.elytradev.concrete.inventory.gui.client.GuiDrawing;

import com.elytradev.concrete.inventory.gui.widget.WWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WAnim extends WWidget {

    private int currentFrame= 0;
    private long currentFrameTime = 0;
    private ResourceLocation[] animLocs;
    private int frameTime;
    private long lastFrame;

    public WAnim(int frameTime, ResourceLocation... animLocs) {
        //an array of ResourceLocations for each frame. Should be put in in order, of course.
        this.animLocs = animLocs;
        //number of milliseconds each animation frame should be. Remember, 1 tick = 50 ms.
        this.frameTime = frameTime;
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void paintBackground(int x, int y) {
        //grab the system time at the very start of the frame.
        long now = System.nanoTime() / 1_000_000L;

        //check bounds so the ResourceLocation isn't passed a bad number
        boolean inBounds = (currentFrame >= 0) && (currentFrame < animLocs.length);
        if (!inBounds) currentFrame = 0;
        //assemble and draw the frame calculated last iteration.
        ResourceLocation currentFrameTex = animLocs[currentFrame];
        GuiDrawing.rect(currentFrameTex, x, y, getWidth(), getHeight(), 0xFFFFFFFF);

        //calculate how much time has elapsed since the last animation change, and change the frame if necessary.
        long elapsed = now - lastFrame;
        currentFrameTime += elapsed;
        if (currentFrameTime >= frameTime) {
            currentFrame++;
            //if we've hit the end of the animation, go back to the beginning
            if (currentFrame >= animLocs.length - 1) {
                currentFrame = 0;
            }
            currentFrameTime = 0;
        }

        //frame is over; this frame is becoming the last frame so write the time to lastFrame
        this.lastFrame = now;
    }
}