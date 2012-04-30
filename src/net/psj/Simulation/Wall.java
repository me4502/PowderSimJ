package net.psj.Simulation;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Wall {
	
	protected int CELL = PowderSimJ.cell;
	
	public Wall()
	{
	}
	
	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, CELL, CELL, 0xFFFFFF);
	}
}
