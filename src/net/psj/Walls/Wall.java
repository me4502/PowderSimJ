package net.psj.Walls;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Wall {
	
	protected int CELL = PowderSimJ.cell;
	
	public Wall()
	{
	}
	
	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, (x*CELL)+CELL, (y*CELL)+CELL, 255,255,255);
	}
}