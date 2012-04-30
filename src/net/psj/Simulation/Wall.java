package net.psj.Simulation;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Wall {

	public int x;
	public int y;
	
	int CELL = PowderSimJ.cell;
	
	public Wall(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void render()
	{
		RenderUtils.drawRect(x*CELL, y*CELL, CELL, CELL, 0xFFFFFF);
	}
}
