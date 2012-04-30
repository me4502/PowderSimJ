package net.psj;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class RenderUtils {

	public static void drawRect(int x, int y, int width, int height, int colour)
	{
		Graphics g = new Graphics();
		g.setColor(new Color(colour));
		for(int w = width; w > 0; w--)
			g.drawRect(x, y, w, height);
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
}
