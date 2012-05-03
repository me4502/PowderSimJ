package net.psj.Simulation;

import net.psj.PowderSimJ;

public class Walls {

	public static Wall[][] bmap = new Wall[PowderSimJ.height/PowderSimJ.cell][PowderSimJ.width/PowderSimJ.cell];
	
	public Walls()
	{
		
	}
	
	public void renderWalls()
	{
		for(int x = 0; x < PowderSimJ.width/PowderSimJ.cell; x++)
			for(int y = 0; y < PowderSimJ.height/PowderSimJ.cell; y++)
			{
				if(bmap[y][x]==null) continue;
				if(bmap[y][x] instanceof Wall)
					bmap[y][x].render(x,y);
			}
	}
}
