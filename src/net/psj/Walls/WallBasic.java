package net.psj.Walls;

import net.psj.RenderUtils;
import net.psj.Simulation.Wall;

public class WallBasic extends Wall{

	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, (x*CELL)+CELL, (y*CELL)+CELL, 99,99,99);
	}
}
