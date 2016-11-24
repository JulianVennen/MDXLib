package com.arisux.mdxlib.lib.client.render;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.arisux.mdxlib.lib.client.gui.GuiCustomScreen;
import com.arisux.mdxlib.lib.game.Game;
import com.arisux.mdxlib.lib.game.GameResources;
import com.arisux.mdxlib.lib.util.Math;
import com.arisux.mdxlib.lib.util.Remote;
import com.arisux.mdxlib.lib.world.block.Blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Draw
{
    public static interface ITooltipLineHandler
    {
        public Dimension getSize();
    
        public void draw(int x, int y);
    }
    
    /**
     * Draws a rectangle at the specified coordinates, with the 
     * specified width, height and color.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this rectangle
     * @param h - Height of this rectangle
     * @param color - Color of this rectangle
     */
    public static void drawRect(int x, int y, int w, int h, int color)
    {
        Draw.drawGradientRect(x, y, w, h, color, color);
    }

    /**
     * Draws a rectangle at the specified coordinates, with the 
     * specified width, height and linear gradient color.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this rectangle
     * @param h - Height of this rectangle
     * @param color1 - First color of the linear gradient
     * @param color2 - Second color of the linear gradient
     */
    public static void drawGradientRect(int x, int y, int w, int h, int color1, int color2)
    {
        Draw.drawGradientRect(x, y, x + w, y + h, 0, color1, color2);
    }

    /**
     * Draws a rectangle at the specified coordinates, with the 
     * specified width, height and linear gradient color.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this rectangle
     * @param h - Height of this rectangle
     * @param zLevel - z level of which to draw the rectangle on.
     * @param color1 - First color of the linear gradient
     * @param color2 - Second color of the linear gradient
     */
    public static void drawGradientRect(int x, int y, int w, int h, int zLevel, int color1, int color2)
    {
        OpenGL.disableTexture2d();
        OpenGL.shadeSmooth();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F((color1 >> 16 & 255) / 255.0F, (color1 >> 8 & 255) / 255.0F, (color1 & 255) / 255.0F, (color1 >> 24 & 255) / 255.0F);
        tessellator.addVertex(w, y, zLevel);
        tessellator.addVertex(x, y, zLevel);
        tessellator.setColorRGBA_F((color2 >> 16 & 255) / 255.0F, (color2 >> 8 & 255) / 255.0F, (color2 & 255) / 255.0F, (color2 >> 24 & 255) / 255.0F);
        tessellator.addVertex(x, h, zLevel);
        tessellator.addVertex(w, h, zLevel);
        tessellator.draw();
        OpenGL.shadeFlat();
        OpenGL.enableTexture2d();
    }

    /**
     * Draws a quad at the specified coordinates, with the
     * specified width and height
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     */
    public static void drawQuad(int x, int y, int w, int h)
    {
        Draw.drawQuad(x, y, w, h, -90);
    }

    /**
     * Draws a quad at the specified coordinates, with the 
     * specified width and height on the specified z level.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     * @param z - z level to render this quad on
     */
    public static void drawQuad(int x, int y, int w, int h, int z)
    {
        Draw.drawQuad(x, y, w, h, z, 0, 1, 0, 1);
    }

    /**
     * Draws a quad at the specified coordinates, with the 
     * specified width and height and specified texture uv coords.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     * @param u - x coordinate of the texture to draw on the quad.
     * @param v - y coordinate of the texture to draw on the quad.
     */
    public static void drawQuad(int x, int y, int w, int h, int u, int v)
    {
        Draw.drawQuad(x, y, w, h, -90, u, v);
    }

    /**
     * Draws a quad at the specified coordinates, with the 
     * specified width and height and specified texture uv coords.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     * @param z - z level to render this quad on
     * @param u - x coordinate of the texture to draw on the quad.
     * @param v - y coordinate of the texture to draw on the quad.
     */
    public static void drawQuad(int x, int y, int w, int h, int z, int u, int v)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + h, z, (u + 0) * f, (v + h) * f1);
        tessellator.addVertexWithUV(x + w, y + h, z, (u + w) * f, (v + h) * f1);
        tessellator.addVertexWithUV(x + w, y + 0, z, (u + w) * f, (v + 0) * f1);
        tessellator.addVertexWithUV(x + 0, y + 0, z, (u + 0) * f, (v + 0) * f1);
        tessellator.draw();
    }

    /**
     * Draws a quad at the specified coordinates, with the 
     * specified width and height and specified texture uv coords.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     * @param minU - x coordinate of the texture to draw on the quad.
     * @param maxU - width of the texture being draw on this quad.
     * @param minV - y coordinate of the texture to draw on the quad.
     * @param maxV - height of the texture being draw on this quad.
     */
    public static void drawQuad(int x, int y, int w, int h, float minU, float maxU, float minV, float maxV)
    {
        Draw.drawQuad(x, y, w, h, -90, minU, maxU, minV, maxV);
    }

    /**
     * Draws a quad at the specified coordinates, with the 
     * specified width and height and specified texture uv coords.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this quad
     * @param h - Height of this quad
     * @param z - z level to render this quad on
     * @param minU - x coordinate of the texture to draw on the quad.
     * @param maxU - width of the texture being draw on this quad.
     * @param minV - y coordinate of the texture to draw on the quad.
     * @param maxV - height of the texture being draw on this quad.
     */
    public static void drawQuad(int x, int y, int w, int h, int z, float minU, float maxU, float minV, float maxV)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + h, z, minU, maxV);
        tessellator.addVertexWithUV(x + w, y + h, z, maxU, maxV);
        tessellator.addVertexWithUV(x + w, y + 0, z, maxU, minV);
        tessellator.addVertexWithUV(x + 0, y + 0, z, minU, minV);
        tessellator.draw();
    }

    /**
     * Draw the specified String at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     * @param shadow - Set to true to draw a shadow beneath the rendered string.
     */
    public static void drawString(String text, int x, int y, int color, boolean shadow)
    {
        text = I18n.format(text);
    
        if (shadow)
        {
            Game.fontRenderer().drawStringWithShadow(text, x, y, color);
        }
    
        if (!shadow)
        {
            Game.fontRenderer().drawString(text, x, y, color);
        }
    
        OpenGL.color3i(0xFFFFFF);
    }

    /**
     * Draw the specified String at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     */
    public static void drawString(String text, int x, int y, int color)
    {
        drawString(text, x, y, color, true);
    }

    /**
     * Draw the specified String centered at the specified coordinates using the specified color.
     *
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param w - width of the string
     * @param h - height of the string
     * @param color - Color to draw using
     * @param shadow - Set to true to draw a shadow beneath the rendered string.
     */
    public static void drawStringAlignCenter(String text, int x, int y, int w, int h, int color, boolean shadow)
    {
        drawString(text, x + (w - Draw.getStringRenderWidth(StatCollector.translateToLocal(text))) / 2, y + (h - 8) / 2, color, shadow);
    }

    /**
     * Draw the specified String centered at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     */
    public static void drawStringAlignCenter(String text, int x, int y, int w, int h, int color)
    {
        drawStringAlignCenter(text, x, y, w, h, color, true);
    }

    /**
     * Draw the specified String centered at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     * @param shadow - Set to true to draw a shadow beneath the rendered string.
     */
    public static void drawStringAlignCenter(String text, int x, int y, int color, boolean shadow)
    {
        drawString(text, x - Draw.getStringRenderWidth(StatCollector.translateToLocal(text)) / 2, y, color, shadow);
    }

    /**
     * Draw the specified String centered at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     */
    public static void drawStringAlignCenter(String text, int x, int y, int color)
    {
        drawStringAlignCenter(text, x, y, color, true);
    }

    /**
     * Draw the specified String aligned to the right at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     * @param shadow - Set to true to draw a shadow beneath the rendered string.
     */
    public static void drawStringAlignRight(String text, int x, int y, int color, boolean shadow)
    {
        drawString(text, x - Draw.getStringRenderWidth(StatCollector.translateToLocal(text)), y, color, shadow);
    }

    /**
     * Draw the specified String aligned to the right at the specified coordinates using the specified color.
     * 
     * @param text - String to draw
     * @param x - x coordinate to draw at
     * @param y - y coordinate to draw at
     * @param color - Color to draw using
     */
    public static void drawStringAlignRight(String text, int x, int y, int color)
    {
        drawStringAlignRight(text, x, y, color, true);
    }

    /**
     * @param s - String to get the render width for.
     * @return The render width of the specified String.
     */
    public static int getStringRenderWidth(String s)
    {
        return Game.fontRenderer().getStringWidth(EnumChatFormatting.getTextWithoutFormattingCodes(s));
    }

    /**
     * Draws a tooltip at the specified cordinates.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param text - Text to show in the tooltip.
     */
    public static void drawToolTip(int x, int y, String text)
    {
        Draw.drawMultilineToolTip(x, y, Arrays.asList(text));
    }

    public static final String TOOLTIP_LINESPACE = "\u00A7h";
    public static final String TOOLTIP_HANDLER = "\u00A7x";
    public static List<Draw.ITooltipLineHandler> tipLineHandlers = new ArrayList<Draw.ITooltipLineHandler>();
    public static int getTipLineId(ITooltipLineHandler handler)
    {
        tipLineHandlers.add(handler);
        return tipLineHandlers.size() - 1;
    }

    public static ITooltipLineHandler getTipLine(String line)
    {
        return !line.startsWith(TOOLTIP_HANDLER) ? null : tipLineHandlers.get(Integer.parseInt(line.substring(2)));
    }

    /**
     * Draws a multi-line tooltip at the specified cordinates.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param list - List of Strings to show in the tooltip.
     */
    public static void drawMultilineToolTip(int x, int y, List<String> list)
    {
        if (list.isEmpty())
        {
            return;
        }
    
        OpenGL.disableRescaleNormal();
        OpenGL.disableDepthTest();
        OpenGL.disableStandardItemLighting();
    
        int w = 0;
        int h = -2;
        for (int i = 0; i < list.size(); i++)
        {
            String s = list.get(i);
            ITooltipLineHandler line = getTipLine(s);
            Dimension d = line != null ? line.getSize() : new Dimension(getStringRenderWidth(s), list.get(i).endsWith(TOOLTIP_LINESPACE) && i + 1 < list.size() ? 12 : 10);
            w = java.lang.Math.max(w, d.width);
            h += d.height;
        }
    
        if (x < 8)
        {
            x = 8;
        }
        else if (x > Screen.scaledDisplayResolution().getScaledWidth() - w - 8)
        {
            x -= 24 + w;
        }
        y = (int) Math.clip(y, 8, Screen.scaledDisplayResolution().getScaledHeight() - 8 - h);
    
        Draw.guiHook.incZLevel(300);
        Draw.drawTooltipBox(x - 4, y - 4, w + 7, h + 7);
    
        for (String s : list)
        {
            ITooltipLineHandler line = getTipLine(s);
            if (line != null)
            {
                line.draw(x, y);
                y += line.getSize().height;
            }
            else
            {
                Game.fontRenderer().drawStringWithShadow(s, x, y, -1);
                y += s.endsWith(TOOLTIP_LINESPACE) ? 12 : 10;
            }
        }
    
        tipLineHandlers.clear();
        Draw.guiHook.incZLevel(-300);
    
        OpenGL.enableDepthTest();
        OpenGL.enableRescaleNormal();
    }

    /**
     * Draws a tooltip box at the specified cordinates, with the specified width and height.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of the box
     * @param h - Height of the box
     */
    public static void drawTooltipBox(int x, int y, int w, int h)
    {
        int bg = 0xf0100010;
        drawGradientRect(x + 1, y, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + h, w - 1, 1, bg, bg);
        drawGradientRect(x + 1, y + 1, w - 1, h - 1, bg, bg);
        drawGradientRect(x, y + 1, 1, h - 1, bg, bg);
        drawGradientRect(x + w, y + 1, 1, h - 1, bg, bg);
        int grad1 = 0x505000ff;
        int grad2 = 0x5028007F;
        drawGradientRect(x + 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect(x + w - 1, y + 2, 1, h - 3, grad1, grad2);
        drawGradientRect(x + 1, y + 1, w - 1, 1, grad1, grad1);
        drawGradientRect(x + 1, y + h - 1, w - 1, 1, grad2, grad2);
    }

    /**
     * Draws a progress bar.
     * 
     * @param label - Label to draw on top of the progress bar.
     * @param maxProgress - Maximum progress
     * @param curProgress - Current progress
     * @param posX - x coordinate to draw the bar at
     * @param posY - y coordinate to draw the bar at
     * @param barWidth - The width of the progress bar
     * @param barHeight - The height of the progress bar
     * @param stringPosY - The offset height of the label text (0 is default)
     * @param color - The color of the progress bar
     * @param barStyle - Set to false for a solid style progress bar. Set to true 
     * for a box-style progress bar.
     */
    public static void drawProgressBar(String label, int maxProgress, int curProgress, int posX, int posY, int barWidth, int barHeight, int stringPosY, int color, boolean barStyle)
    {
        OpenGL.pushMatrix();
        {
            Gui.drawRect(posX + 0, posY + 0, posX + barWidth, posY + 5 + barHeight, 0x77000000);
    
            if (!barStyle && curProgress > maxProgress / barWidth)
            {
                Gui.drawRect(posX + 1, posY + 1, posX + ((((curProgress * maxProgress) / maxProgress) * barWidth) / maxProgress) - 1, posY + 4 + barHeight, color);
                Gui.drawRect(posX + 1, posY + 2 + (barHeight / 2), posX + ((((curProgress * maxProgress) / maxProgress) * barWidth) / maxProgress) - 1, posY + 4 + barHeight, 0x55000000);
            }
            else if (curProgress > maxProgress / barWidth)
            {
                int spaceBetweenBars = 1;
                int amountOfBars = 70;
                int widthOfBar = (barWidth / amountOfBars - spaceBetweenBars);
    
                for (int x = 1; x <= amountOfBars - ((curProgress * amountOfBars) / maxProgress); x++)
                {
                    int barStartX = (posX + widthOfBar) * (x) - widthOfBar;
    
                    Gui.drawRect(barStartX + spaceBetweenBars * x, posY + 1, barStartX + widthOfBar + spaceBetweenBars * x, posY + 4 + barHeight, color);
                    Gui.drawRect(barStartX + spaceBetweenBars * x, posY + 2 + (barHeight / 2), barStartX + widthOfBar + spaceBetweenBars * x, posY + 4 + barHeight, 0x55000000);
                }
            }
    
            Game.fontRenderer().drawStringWithShadow(label, posX + (barWidth / 2) - Game.fontRenderer().getStringWidth(label) + (Game.fontRenderer().getStringWidth(label) / 2), (posY - 1) + stringPosY, 0xFFFFFFFF);
        }
        OpenGL.popMatrix();
    }

    /**
     * Draws a centered rectangle with an outline at the specified 
     * coordinates and the specified width, height, and color.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this rectangle
     * @param h - Height of this rectangle
     * @param borderWidth - Width of the rectangle's border
     * @param fillColor - Color of the inner portion of this rectangle
     * @param borderColor - Color of the border of this rectangle
     */
    public static void drawCenteredRectWithOutline(int x, int y, int w, int h, int borderWidth, int fillColor, int borderColor)
    {
        drawRect(x - w / 2 + borderWidth, y - h / 2, w, h, fillColor);
        drawRect(x - w / 2 + borderWidth, y - h / 2, w, borderWidth, borderColor);
        drawRect(x - w / 2, y + h / 2, w, borderWidth, borderColor);
        drawRect(x - w / 2, y - h / 2, borderWidth, h, borderColor);
        drawRect(x + w / 2, y - h / 2 + borderWidth, borderWidth, h, borderColor);
    }

    /**
     * Draws a rectangle with an outline at the specified 
     * coordinates and the specified width, height, and color.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - Width of this rectangle
     * @param h - Height of this rectangle
     * @param borderWidth - Width of the rectangle's border
     * @param fillColor - Color of the inner portion of this rectangle
     * @param borderColor - Color of the border of this rectangle
     */
    public static void drawRectWithOutline(int x, int y, int w, int h, int borderWidth, int fillColor, int borderColor)
    {
        int x1 = x;
        int y1 = y;
        int x2 = x + w;
        int y2 = y + h;
    
        Gui.drawRect(x1, y1, x2, y2, fillColor);
        Gui.drawRect(x1, y1, x2, y2 - h + borderWidth, borderColor);
        Gui.drawRect(x1, y1 + h - borderWidth, x2, y2, borderColor);
        Gui.drawRect(x1, y1 + borderWidth, x2 - w + borderWidth, y2 - borderWidth, borderColor);
        Gui.drawRect(x1 + w - borderWidth, y1 + borderWidth, x2, y2 - borderWidth, borderColor);
    }

    /**
     * Draws an overlay across the entire screen using the specified ResourceLocation
     * @param resource - The ResourceLocation to draw
     */
    public static void drawOverlay(ResourceLocation resource)
    {
        Draw.drawOverlay(resource, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Draws an overlay across the entire screen using the specified ResourceLocation 
     * and an alpha value.
     * 
     * @param resource - The ResourceLocation to draw
     * @param a - Alpha value to render the overlay at. For transparency.
     */
    public static void drawOverlay(ResourceLocation resource, float a)
    {
        Draw.drawOverlay(resource, 1.0F, 1.0F, 1.0F, a);
    }

    /**
     * Draws an overlay across the entire screen using the specified ResourceLocation 
     * and 3 RGB color values.
     * 
     * @param resource - The ResourceLocation to draw
     * @param r - Red value to render the overlay at.
     * @param g - Green value to render the overlay at.
     * @param b - Blue value to render the overlay at.
     */
    public static void drawOverlay(ResourceLocation resource, float r, float g, float b)
    {
        Draw.drawOverlay(resource, r, g, b, 1.0F);
    }

    /**
     * Draws an overlay across the entire screen using the specified ResourceLocation 
     * and 4 RGBA color values.
     * 
     * @param resource - The ResourceLocation to draw
     * @param r - Red value to render the overlay at.
     * @param g - Green value to render the overlay at.
     * @param b - Blue value to render the overlay at.
     * @param a - Alpha value to render the overlay at. For transparency.
     */
    public static void drawOverlay(ResourceLocation resource, float r, float g, float b, float a)
    {
        OpenGL.enableBlend();
        OpenGL.disableDepthTest();
        OpenGL.depthMask(false);
        OpenGL.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        OpenGL.color(r, g, b, a);
        OpenGL.disableAlphaTest();
        Draw.bindTexture(resource);
        drawQuad(0, 0, Screen.scaledDisplayResolution().getScaledWidth(), Screen.scaledDisplayResolution().getScaledHeight());
        OpenGL.depthMask(true);
        OpenGL.enableDepthTest();
        OpenGL.enableAlphaTest();
        OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGL.disableBlend();
    }

    /**
     * Draw the specified ModelBase instance at 0,0,0 with the specified ResourceLocation.
     * 
     * @param model - ModelBase instance to draw.
     * @param resource - ResourceLocation to draw on the ModelBase instance.
     */
    public static void drawModel(ModelBase model, ResourceLocation resource)
    {
        Draw.drawModel(null, model, resource, 0, 0, 0);
    }

    /**
     * Draw the specified ModelBase instance at the specified coordinates with the
     * specified ResourceLocation.
     * 
     * @param model - ModelBase instance to draw.
     * @param resource - ResourceLocation to draw on the ModelBase instance.
     * @param posX - x coordinate to draw this model at.
     * @param posY - y coordinate to draw this model at.
     * @param posZ - z coordinate to draw this model at.
     */
    public static void drawModel(ModelBase model, ResourceLocation resource, double posX, double posY, double posZ)
    {
        Draw.drawModel(null, model, resource, posX, posY, posZ);
    }

    /**
     * Draw the specified ModelBase instance at the specified coordinates with the
     * specified ResourceLocation.
     * 
     * @param entity - The entity class to provide the ModelBase instance with.
     * @param model - ModelBase instance to draw.
     * @param resource - ResourceLocation to draw on the ModelBase instance.
     */
    public static void drawModel(Entity entity, ModelBase model, ResourceLocation resource)
    {
        Draw.drawModel(entity, model, resource, 0, 0, 0);
    }

    /**
     * Draw the specified ModelBase instance at the specified coordinates with the
     * specified ResourceLocation.
     * 
     * @param entity - The entity class to provide the ModelBase instance with.
     * @param model - ModelBase instance to draw.
     * @param resource - ResourceLocation to draw on the ModelBase instance.
     * @param posX - x coordinate to draw this model at.
     * @param posY - y coordinate to draw this model at.
     * @param posZ - z coordinate to draw this model at.
     */
    public static void drawModel(Entity entity, ModelBase model, ResourceLocation resource, double posX, double posY, double posZ)
    {
        OpenGL.disableCullFace();
        Draw.bindTexture(resource);
        OpenGL.translate(posX, posY, posZ);
        model.render(entity, 0, 0, 0, 0, 0, 0.625F);
    }

    /**
     * Draw the specified ModelBase instance at the specified coordinates with the
     * specified ResourceLocation.
     * 
     * @param model - ModelBase instance to draw.
     * @param resource - ResourceLocation to draw on the ModelBase instance.
     * @param x - x coordinate to draw this model at.
     * @param y - y coordinate to draw this model at.
     * @param scale - The scale this model should be rendered at.
     */
    public static void drawShowcaseModel(ModelBase model, ResourceLocation resource, int x, int y, float scale)
    {
        OpenGL.color(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGL.pushMatrix();
        OpenGL.translate(x, y - (scale * 0.43f), 10);
        OpenGL.scale(0.06f * scale, 0.06f * scale, 1);
        OpenGL.rotate(-20, 1, 0, 0);
        OpenGL.rotate(205, 0, 1, 0);
        OpenGL.disableCullFace();
        OpenGL.enableDepthTest();
        Draw.bindTexture(resource);
        model.render(null, 0F, 0F, 0F, 0F, 0F, 0.0625F);
        OpenGL.enableCullFace();
        OpenGL.disableDepthTest();
        OpenGL.popMatrix();
    }

    /**
     * Draw the specified entity at the specified coordinates using 
     * the specified scale, yaw, and pitch.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param scale - The scale this model should be rendered at.
     * @param yaw - The rotation yaw.
     * @param pitch - The rotation pitch.
     * @param entity - The Entity instance that is being rendered.
     */
    public static void drawEntity(int x, int y, int scale, float yaw, float pitch, Entity entity)
    {
        OpenGL.enable(GL11.GL_COLOR_MATERIAL);
        OpenGL.pushMatrix();
        {
            OpenGL.translate(x, y, 100.0F);
            OpenGL.scale(-scale, scale, scale);
            OpenGL.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            OpenGL.rotate(yaw, 0.0F, 1.0F, 0.0F);
            OpenGL.rotate(pitch, 1.0F, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
            OpenGL.enableLightMapping();
        }
        OpenGL.popMatrix();
    }

    /**
     * Draw the player's face with the specified username. Requires a network
     * connection. Will default to a Steve face if one is not present.
     * 
     * @param username - Username of the player's face to draw.
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - Width to render the face at.
     * @param height - Height to render the face at.
     */
    public static void drawPlayerFace(String username, int x, int y, int width, int height)
    {
        ResourceLocation resource = Remote.downloadResource(String.format("http://s3.amazonaws.com/MinecraftSkins/%s.png", username), AbstractClientPlayer.locationStevePng, false);
    
        Draw.bindTexture(resource);
        drawQuad(x, y, width, height, 90, 0.125F, 0.25F, 0.25F, 0.5F);
        drawQuad(x, y, width, height, 90, 0.75F, 0.625F, 0.25F, 0.5F);
    }

    /**
     * Draw the specified ResourceLocation at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     */
    public static void drawResource(ResourceLocation resource, int posX, int posY, int width, int height)
    {
        Draw.drawResource(resource, posX, posY, width, height, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draw the specified ResourceLocation at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     * @param r - Red value
     * @param g - Green value
     * @param b - Blue value
     * @param a - Alpha value (Transparency)
     */
    public static void drawResource(ResourceLocation resource, int posX, int posY, int width, int height, float r, float g, float b, float a)
    {
        Draw.drawResource(resource, posX, posY, width, height, r, g, b, a, 1.0f, 1.0f);
    }

    /**
     * Draw the specified ResourceLocation at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     * @param r - Red value
     * @param g - Green value
     * @param b - Blue value
     * @param a - Alpha value (Transparency)
     * @param u - x coordinate of the texture offset
     * @param v - y coordinate of the texture offset
     */
    public static void drawResource(ResourceLocation resource, int posX, int posY, int width, int height, float r, float g, float b, float a, float u, float v)
    {
        OpenGL.disableLighting();
        OpenGL.disableFog();
        Draw.bindTexture(resource);
        OpenGL.color(r, g, b, a);
        drawQuad(posX, posY, width, height, 0, 0, u, 0, v);
    }

    /**
     * Draw the specified ResourceLocation centered at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     */
    public static void drawResourceCentered(ResourceLocation resource, int posX, int posY, int width, int height)
    {
        Draw.drawResourceCentered(resource, posX, posY, width, height, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Draw the specified ResourceLocation centered at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     * @param r - Red value
     * @param g - Green value
     * @param b - Blue value
     * @param a - Alpha value (Transparency)
     */
    public static void drawResourceCentered(ResourceLocation resource, int posX, int posY, int width, int height, float r, float g, float b, float a)
    {
        Draw.drawResourceCentered(resource, posX, posY, width, height, r, g, b, a, 1.0f, 1.0f);
    }

    /**
     * Draw the specified ResourceLocation centered at the specified coordinates and dimensions.
     * 
     * @param resource - ResourceLocation to render
     * @param posX - x coordinate
     * @param posY - y coordinate
     * @param width - Width to render this resource at.
     * @param height - Height to render this resource at.
     * @param r - Red value
     * @param g - Green value
     * @param b - Blue value
     * @param a - Alpha value (Transparency)
     * @param u - x coordinate of the texture offset
     * @param v - y coordinate of the texture offset
     */
    public static void drawResourceCentered(ResourceLocation resource, int posX, int posY, int width, int height, float r, float g, float b, float a, float u, float v)
    {
        OpenGL.disableLighting();
        OpenGL.disableFog();
        Draw.bindTexture(resource);
        OpenGL.color(r, g, b, a);
        drawQuad(posX - (width / 2), posY, width, height, 0, 0, u, 0, v);
    }

    /**
     * Draw the specified particle at the specified coordinates and dimensions.
     * 
     * @param particleId - ID of the particle to draw
     * @param x - x coordinate
     * @param y - y coordinate
     * @param width - Width to render the particle at
     * @param height - Height to render the particle at
     */
    public static void drawParticle(int index, int x, int y, int width, int height)
    {
        float tS = 0.0624375F;
        float u = (float) (index % 16) / 16.0F;
        float mU = u + tS;
        float v = (float) (index / 16) / 16.0F;
        float mV = v + tS;
        
        Draw.bindTexture(GameResources.particleTexture);
        drawQuad(x, y, width, height, 0, u, mU, v, mV);
    }

    /**
     * Draw the IIcon of the specified Item at the specified coordinates and dimensions
     * 
     * @param item - Item to draw the iicon of.
     * @param x - x coordinate
     * @param y - y corodinate
     * @param width - Width to render the icon at
     * @param height - Height to render the icon at
     */
    public static void drawItemIcon(Item item, int x, int y, int width, int height)
    {
        IIcon icon = item.getIcon(new ItemStack(item), 1);
    
        if (icon != null)
        {
            Draw.bindTexture(Game.minecraft().getTextureManager().getResourceLocation(item.getSpriteNumber()));
            drawQuad(x, y, width, height, 0, icon.getMinU(), icon.getMaxU(), icon.getMinV(), icon.getMaxV());
        }
    }

    /**
     * Draw the IIcon of the specified Block side at the specified coordinates and dimensions
     * 
     * @param block - Block to draw the iicon of.
     * @param side - ID of the side of the Block to draw.
     * @param x - x coordinate
     * @param y - y corodinate
     * @param width - Width to render the icon at
     * @param height - Height to render the icon at
     */
    public static void drawBlockSide(Block block, int side, int x, int y, int width, int height)
    {
        Draw.drawBlockSide(block, side, x, y, width, height, 1, 1);
    }

    /**
     * Draw the IIcon of the specified Block side at the specified coordinates and dimensions
     * 
     * @param block - Block to draw the iicon of.
     * @param side - ID of the side of the Block to draw.
     * @param x - x coordinate
     * @param y - y corodinate
     * @param width - Width to render the icon at
     * @param height - Height to render the icon at
     * @param u - x coordinate of the texture offset
     * @param v - y coordinate of the texture offset
     */
    public static void drawBlockSide(Block block, int side, int x, int y, int width, int height, float u, float v)
    {
        IIcon icon = block.getBlockTextureFromSide(side);
    
        if (icon != null)
        {
            Draw.bindTexture(Blocks.getBlockTexture(block, side));
            drawQuad(x, y, width, height, 0, 0, u, 0, v);
        }
    }

    /**
     * Draw the IRecipe on screen of the specified Item or Block
     * 
     * @param obj - Item or Block instance
     * @param x - x coordinate
     * @param y - y coordinate
     * @param size - Scale of the recipe
     * @param slotPadding - Padding between each slot drawn
     * @param backgroundColor - Background color of each slot drawn.
     */
    public static void drawRecipe(Object obj, int x, int y, int size, int slotPadding, int backgroundColor)
    {
        IRecipe irecipe = obj instanceof Item ? (Game.getRecipe(obj)) : obj instanceof Block ? (Game.getRecipe(obj)) : null;
    
        if (irecipe == null)
        {
            return;
        }
    
        for (int gX = 0; gX < 3; ++gX)
        {
            for (int gY = 0; gY < 3; ++gY)
            {
                drawRect(x + slotPadding + gX * (size + slotPadding), y + slotPadding + gY * (size + slotPadding), size, size, backgroundColor);
    
                if (irecipe instanceof ShapedRecipes)
                {
                    ItemStack slotStack = ((ShapedRecipes) irecipe).recipeItems[gX + gY * 3];
    
                    if (slotStack != null)
                    {
                        drawItemIcon(slotStack.getItem(), x + slotPadding + gX * (size + slotPadding), y + slotPadding + gY * (size + slotPadding), size, size);
                    }
                }
    
                if (irecipe instanceof ShapedOreRecipe)
                {
                    ShapedOreRecipe recipe = (ShapedOreRecipe) irecipe;
    
                    for (Object o : recipe.getInput())
                    {
                        try
                        {
                            Class<?> unmodifiableArrayList = Class.forName("net.minecraftforge.oredict.OreDictionary$UnmodifiableArrayList");
    
                            if (unmodifiableArrayList.isInstance(o))
                            {
                                String domain = o.toString().contains("item.") ? o.toString().substring(o.toString().indexOf("x") + 1, o.toString().indexOf("item.")).equalsIgnoreCase("") ? "minecraft" : o.toString().substring(o.toString().indexOf("x") + 1, o.toString().indexOf("item.")) : "null";
                                Item item = GameRegistry.findItem(domain, o.toString().substring(o.toString().indexOf(".") + 1, o.toString().indexOf("@")));
    
                                if (item != null)
                                {
                                    drawItemIcon(item, x + slotPadding + gX * (size + slotPadding), y + slotPadding + gY * (size + slotPadding), size, size);
                                }
                            }
                            else if ((gX + gY * 3) < recipe.getInput().length)
                            {
                                if (recipe.getInput()[gX + gY * 3] instanceof ItemStack)
                                {
                                    ItemStack slotStack = (ItemStack) recipe.getInput()[gX + gY * 3];
    
                                    if (slotStack != null)
                                    {
                                        drawItemIcon(slotStack.getItem(), x + slotPadding + gX * (size + slotPadding), y + slotPadding + gY * (size + slotPadding), size, size);
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Binds a texture to OpenGL using Minecraft's render engine.
     * @param resource - The ResourceLocation of the resource to bind.
     */
    public static void bindTexture(ResourceLocation resource)
    {
        Game.minecraft().renderEngine.bindTexture(resource);
    }

    /**
     * Get the full path of the specified ResourceLocation. Format: domain:path/to/resource.png
     * @param resource - The ResourceLocation to retrieve a path of.
     * @return The full path of the resource, including the domain.
     */
    public static String getResourcePath(ResourceLocation resource)
    {
        return String.format("%s:%s", resource.getResourceDomain(), resource.getResourcePath());
    }

    public static final GuiCustomScreen guiHook = new GuiCustomScreen();
    public static void lightingHelper(Entity entity, float offset)
    {
        int brightness = entity.worldObj.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY + offset / 16.0F), MathHelper.floor_double(entity.posZ), 0);
        OpenGL.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536);
        OpenGL.color(1.0F, 1.0F, 1.0F);
    }

    public static ArrayList<String> wrapString(String string, int width)
    {
        ArrayList<String> strings = new ArrayList<String>();
        int stringWidth = getStringRenderWidth(string);
    
        if (stringWidth > width)
        {
            String currentLine = "";
    
            for (String word : string.split(" "))
            {
                int wordWidth = getStringRenderWidth(word);
                int currentLineWidth = getStringRenderWidth(currentLine);
    
                if ((currentLineWidth + wordWidth) <= width)
                {
                    currentLine = currentLine.isEmpty() ? word : currentLine + " " + word;
                }
                else
                {
                    strings.add(currentLine);
                    currentLine = word;
                }
            }
    
            if (!currentLine.isEmpty())
            {
                strings.add(currentLine);
            }
        }
        else
        {
            strings.add(string);
        }
    
        return strings;
    }

}