package Walls;

import net.psj.RenderUtils;
import net.psj.Simulation.Wall;

public class WallFan extends Wall{

	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, (x*CELL)+CELL, (y*CELL)+CELL, 0,0,255);
	}	
}
