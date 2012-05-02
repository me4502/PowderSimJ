package Interface;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Menu {

	public static void draw()
	{
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		RenderUtils.drawRect(0, h-10, w, h, 255,255,255);
	}
}
