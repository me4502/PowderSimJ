package net.psj.Walls;

import net.psj.RenderUtils;

public class WallFan extends Wall{

	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, (x*CELL)+CELL, (y*CELL)+CELL, 0,0,255);
	}	
}
