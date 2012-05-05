package net.psj;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.font.GlyphPage;

public class RenderUtils {

	public static void drawPixel(int x, int y, int r, int g, int b)
	{
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glBlendFunc(770, 771);
		GL11.glColor3d(r,g,b);
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}
	
	public static void drawRect(int x1, int y1, int x2, int y2, int r,int g,int b)
	{
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glBlendFunc(770, 771);
		GL11.glColor3d(r,g,b);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x2, y1);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x1, y2);
		GL11.glEnd();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}
	
	public static void drawRectLine(int x1, int y1, int x2, int y2, int r,int g,int b)
	{
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glBlendFunc(770, 771);
		GL11.glColor3d(r,g,b);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glVertex2d(x1, y1);
		GL11.glVertex2d(x2, y1);
		GL11.glVertex2d(x2, y2);
		GL11.glVertex2d(x1, y2);
		GL11.glEnd();
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

	
	public static void setAntiAliasing(boolean antiAlias)
	{
        java.awt.Graphics g = GlyphPage.getScratchGraphics();
        if (g!=null && g instanceof Graphics2D) {
           Graphics2D g2d = (Graphics2D)g;
           g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                 antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
           g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                 antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
	}
		
	public static int clamp_flt(float f, float min, float max)
	{
		if (f<min)
			return 0;
		if (f>max)
			return 255;
		return (int)(255.0f*(f-min)/(max-min));
	}
	
	public static int PIXRGB(int r, int g, int b) 
	{
		return (0xFF000000|((r)<<16)|((g)<<8)|((b)));
	}
	
	public static int PIXRGBA(int r, int g, int b, int a) 
	{
		return (((a)<<24)|((r)<<16)|((g)<<8)|((b)));
	}
	
	public static int PIXA(int x)
	{
		return (((x)>>24)&0xFF);
	}
		
	public static int PIXR(int x)
	{
		return (((x)>>16)&0xFF);
	}
	public static int PIXG(int x)
	{
		return (((x)>>8)&0xFF);
	}

	public static int PIXB(int x)
	{
		return ((x)&0xFF);
	}
	
	public static int PIXPACK(int x)
	{
		return (0xFF000000|((x)&0xFFFFFF));
	}

	
    public static void drawLine(int i, int j, int k, int l, int i1, int r, int g, int b)
    {
    	GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glBlendFunc(770, 771);
        GL11.glColor3d(r,g,b);
        GL11.glLineWidth(i1);
        GL11.glBegin(1);
        GL11.glVertex2i(i, j);
        GL11.glVertex2i(k, l);
        GL11.glEnd();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
    }
}
