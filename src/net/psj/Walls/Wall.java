package net.psj.Walls;

import net.psj.Placable;
import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Wall extends Placable{
	
	protected int CELL = PowderSimJ.cell;
		
	public Wall(String name, float[] colour, int id, int menu)
	{
		super(name,colour,id+PowderSimJ.wallStart, menu);
	}
	
	public void render(int x, int y)
	{
		RenderUtils.drawRect(x*CELL, y*CELL, (x*CELL)+CELL, (y*CELL)+CELL, colour[0],colour[1],colour[2]);
	}
}