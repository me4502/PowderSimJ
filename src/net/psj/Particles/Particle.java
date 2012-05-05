package net.psj.Particles;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Simulation.Air;

public class Particle 
{
	public String name;
	public int colour;
	float airdrag;
	float advection;
	float loss;
	
	public int x = 0;
	public int y = 0;
	
	int CELL = PowderSimJ.cell;
	
	int vy = 0, vx = 0;
	
	public Particle(String name, int colour, float airdrag, float advection, float loss)
	{
		this.name = name;
		this.colour = colour;
		this.airdrag = airdrag;
		this.advection = advection;
	}
	
	public void setPos(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean update()
	{
		boolean ret = false;
		y++;
		Air.vx[y/CELL][x/CELL] = Air.vx[y/CELL][x/CELL] + (airdrag*vx);
		Air.vy[y/CELL][x/CELL] = Air.vy[y/CELL][x/CELL] + (airdrag*vy);
		//vx *= loss;
		//vy *= loss;
		vx += advection*Air.vx[y/CELL][x/CELL];
		vy += advection*Air.vy[y/CELL][x/CELL];
		//try{
		//	if(ParticleData.pmap[y][x-vx]==null)
		//	{
		//		ParticleData.pmap[y][x-vx] = this;
		//		ParticleData.pmap[y][x] = null;
		//		ret =  true;
		//	}
		//}
		//catch(Exception e)
		//{
		//	if(vx>0)vx--;
		//	else vx++;
		//}

		//try{
		//	if(ParticleData.pmap[y-vy][x]==null)
		//	{
		//		ParticleData.pmap[y-vy][x] = this;
		//		ParticleData.pmap[y][x] = null;
		//		ret =  true;
		//	}
		//}
		//catch(Exception e)
		//{
		//	if(vy>0)vy--;
		//	else vy++;
		//}
		return ret;
	}
	
	public boolean render()
	{
		RenderUtils.drawPixel(x, y, RenderUtils.PIXR(colour),RenderUtils.PIXG(colour),RenderUtils.PIXB(colour));
		return false;
	}
}
