package net.psj;

import net.psj.Particles.Particle;
import net.psj.Particles.ParticleDust;

public class ParticleData {
	public static int[][] pmap = new int[PowderSimJ.height][PowderSimJ.width];
	public static Particle[] parts = new Particle[PowderSimJ.height*PowderSimJ.width];
	public static Particle dust;
	
	public static int latPart = 0;
	
	public void create_parts(int x1, int y1, int id)
	{
		x1 = x1-(PowderSimJ.brushSize/2);
		y1 = y1-(PowderSimJ.brushSize/2);
		for(int x = x1; x < x1 + PowderSimJ.brushSize; x++)
		{
			for(int y = y1; y < y1 + PowderSimJ.brushSize; y++)
			{
				if(x>PowderSimJ.width - PowderSimJ.cell || x<PowderSimJ.cell || y>PowderSimJ.height - PowderSimJ.cell || y<PowderSimJ.cell)
				{
					continue;
				}
				Particle newPart = newPartFromID(id);
				if(newPart!=null)
				{
					parts[latPart] = newPart;
					parts[latPart++].setPos(x, y);
					System.out.println(latPart);
				}
				else
					parts[pmap[y][x]] = null;
			}
		}
	}
	
	public void update()
	{
		for(int x = 0; x < PowderSimJ.width; x++)
			for(int y = 0; y < PowderSimJ.height; y++)
			{
				pmap[y][x] = 0;
			}
		//int usedParts = 0;
		for(int i = 0; i < latPart; i++)
		{
			if(parts[i]!=null)
			{
				if(parts[i] instanceof Particle)
				{
					parts[i].update();
					int x = parts[i].x;
					int y = parts[i].y;
					if(x>PowderSimJ.width - PowderSimJ.cell || x<PowderSimJ.cell || y>PowderSimJ.height - PowderSimJ.cell || y<PowderSimJ.cell || pmap[y][x]!=0)
					{
						kill(i);

						continue;
					}
					//usedParts = i;
					pmap[y][x] = i;
				}
			}
			else
			{
				//if(parts[i+1]!=null)
				//{
				//	parts[i] = parts[i+1];
				//	parts[i+1]=null;
				//}
			}
		}
		//latPart = usedParts;
	}
	
	public void kill(int i)
	{
		parts[i] = null;
	}
	
	public void render()
	{
		for(int i = 0; i < latPart; i++)
		{
			if(parts[i]!=null)
			{
				if(parts[i] instanceof Particle)
					parts[i].render();
			}
		}
	}
	
	public Particle newPartFromID(int id)
	{
		if(id==1) return new ParticleDust();
		return null;
	}
	
	static{
		dust = new Particle("Dust",0xEDD69F,0.1f,0.3f, 1.0f);
	}
}