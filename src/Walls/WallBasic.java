package Walls;

import net.psj.RenderUtils;
import net.psj.Simulation.Wall;

public class WallBasic extends Wall{

	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, CELL, CELL, 0x666666);
	}
}
