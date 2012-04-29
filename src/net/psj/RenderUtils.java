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
}
