package net.psj;

import net.psj.Particles.Particle;
import net.psj.Particles.ParticleClone;
import net.psj.Particles.ParticleDust;
import net.psj.Particles.ParticleErase;
import net.psj.Particles.ParticleWater;

public class ParticleData {
	public static int[][] pmap = new int[PowderSimJ.height][PowderSimJ.width];
	public static Particle[] parts = new Particle[PowderSimJ.height*PowderSimJ.width*PowderSimJ.cell];
	
	public static int latPart = 1;
	public static int gravityMode = 0;
	
	public Particle create_part(int x, int y, int id)
	{
		if(x>PowderSimJ.width - PowderSimJ.cell || x<PowderSimJ.cell || y>PowderSimJ.height - PowderSimJ.cell || y<PowderSimJ.cell)
		{
			return null;
		}
		Particle newPart = newPartFromID(id);
		if(!(newPart instanceof ParticleErase) && pmap[y][x]==0 && newPart!=null)
		{
			newPart.setPos(x,y,id);
			parts[latPart] = newPart;
			return parts[latPart++];
		}
		else if(newPart instanceof ParticleErase && pmap[y][x]!=0)
			kill(pmap[y][x]);
		
		return null;
	}
	
	public void create_parts(int x1, int y1, int id)
	{
		x1 = x1-(PowderSimJ.brushSize/2);
		y1 = y1-(PowderSimJ.brushSize/2);
		for(int x = x1; x < x1 + PowderSimJ.brushSize; x++)
		{
			for(int y = y1; y < y1 + PowderSimJ.brushSize; y++)
			{
				create_part(x,y,id);
			}
		}
	}
	
	public void update()
	{
		int part = 0;
		for(int x = 0; x < PowderSimJ.width; x++)
			for(int y = 0; y < PowderSimJ.height; y++)
			{
				pmap[y][x] = 0;
			}
		for(int i = 1; i < latPart; i++)
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
					pmap[y][x] = i;
					part++;
				}
				else
					kill(i);
			}
			else
			{
				moveDown(i);
				i--;
			}
		}
		if(part==0) latPart = 1;
	}
	
	public static void moveDown(int i)
	{
		for(int p = i; p < latPart; p++)
		{
			if(p!=latPart)
				parts[p] = parts[p+1];
			else
				parts[p] = null;
		}
		latPart--;
	}
	
	public static void kill(int i)
	{
		parts[i] = null;
		if(latPart>300000)
		{
			System.out.println(latPart);
			moveDown(i);
			System.out.println(latPart);
		}
	}
	
	public void render()
	{
		for(int i = 1; i < latPart; i++)
		{
			if(parts[i]!=null)
			{
				if(parts[i] instanceof Particle)
					parts[i].render();
			}
		}
	}
	
	public static Particle newPartFromID(int id)
	{
		if(id==0) return new ParticleErase();
		if(id==1) return new ParticleDust();
		if(id==2) return new ParticleClone();
		if(id==3) return new ParticleWater();
		return null;
	}
	
	public static int PT_NUM = 4;
}