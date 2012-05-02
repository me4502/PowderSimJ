package net.psj;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.font.GlyphPage;

public class RenderUtils {

	public static void drawRect(int x, int y, int width, int height, int colour)
	{
		Graphics g = new Graphics();
		g.setColor(new Color(colour));
		for(int w = width; w > 0; w--)
			g.drawRect(x, y, w, height);
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
	
	public static void drawLine(int x, int y, int x2, int y2, int colour)
	{
		Graphics g = new Graphics();
		g.setColor(new Color(colour));
		g.drawLine(x, y, x2, y2);
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
		return ((((r)<<8)&0xF800)|(((g)<<3)&0x07E0)|(((b)>>3)&0x001F));
	}
	
	public static int PIXRGBA(int r,int g,int b,int a) 
	{
		return (((a)<<24)|((r)<<16)|((g)<<8)|((b)));
	}
}
