package net.psj.Simulation;

import net.psj.PowderSimJ;
import net.psj.Walls.*;

public class WallData {

	public static Wall[][] bmap = new Wall[PowderSimJ.height/PowderSimJ.cell][PowderSimJ.width/PowderSimJ.cell];
	
	public Wall create_wall(int x, int y, int id)
	{
		if(x>PowderSimJ.width/PowderSimJ.cell - 1 || x<1 || y>PowderSimJ.height/PowderSimJ.cell - 1 || y<1)
		{
			return null;
		}
		Wall newWall = newWallfromID(id);
		if(!(newWall instanceof WallErase) && bmap[y][x]==null && newWall!=null)
		{
			bmap[y][x] = newWall;
		}
		else if(newWall instanceof WallErase && bmap[y][x]!=null)
			bmap[y][x] = null;
		
		return null;
	}
	
	public void create_walls(int x1, int y1, int id)
	{
		x1 = x1-((PowderSimJ.brushSize/PowderSimJ.cell)/2);
		y1 = y1-((PowderSimJ.brushSize/PowderSimJ.cell)/2);
		for(int x = x1; x < x1 + PowderSimJ.brushSize/PowderSimJ.cell; x++)
		{
			for(int y = y1; y < y1 + PowderSimJ.brushSize/PowderSimJ.cell; y++)
			{
				create_wall(x,y,id);
			}
		}
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
	
	public static Wall getWallAt(int x, int y)
	{
		return WallData.bmap[y/PowderSimJ.cell][x/PowderSimJ.cell];
	}
	
	public static Wall newWallfromID(int id)
	{
		id -= PowderSimJ.wallStart;
		if(id==0) return new WallErase();
		if(id==1) return new WallBasic();
		if(id==2) return new WallFan();
		return null;
	}
	
	public static int WL_NUM = 3;
}
