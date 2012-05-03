package net.psj.Interface;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Menu {

	public static void draw()
	{
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		RenderUtils.drawRect(0, h+m, w, h, 255,255,255);
	}
}
