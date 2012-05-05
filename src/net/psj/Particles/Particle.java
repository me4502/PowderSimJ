package net.psj.Particles;

import java.util.Random;

import net.psj.ParticleData;
import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Simulation.Air;

public class Particle 
{
	public String name;
	public int colour;
	float airdrag;
	float airloss;
	float advection;
	float loss;
	float diffusion;
	int state = 0;//powder,solid,liquid,gas,special
	
	public int x = 0;
	public int y = 0;
	public int id = 0;
	
	static int CELL = PowderSimJ.cell;
	
	public int vy = 0;
	int vx = 0;
	
	public static final float CFDS = (4.0f/CELL);
	public static final int RAND_MAX = 0x7FFF;
	
	Random rand = new Random();
	
	public Particle(String name, int colour, float airdrag, float airloss, float advection, float loss, float diffusion, int state)
	{
		this.name = name;
		this.colour = colour;
		this.airdrag = airdrag;
		this.airloss = airloss;
		this.advection = advection;
		this.diffusion = diffusion;
		this.state = state;
	}
	
	public void setPos(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public boolean update()
	{
		//vx++;
		boolean ret = false;
		Air.vx[y/CELL][x/CELL] = Air.vx[y/CELL][x/CELL]*airloss + airdrag*vx;
		Air.vy[y/CELL][x/CELL] = Air.vy[y/CELL][x/CELL]*airloss + airdrag*vy;
		vx *= loss;
		vy *= loss;
		vx += advection*Air.vx[y/CELL][x/CELL];
		vy += advection*Air.vy[y/CELL][x/CELL];
		vx += diffusion*(rand.nextInt()/(0.5f*RAND_MAX)-1.0f);
		vy += diffusion*(rand.nextInt()/(0.5f*RAND_MAX)-1.0f);
		try{
			if(ParticleData.pmap[y][x-vx]==0)
			{
				setPos(x+vx, y, id);
				ret =  true;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			if(vx>0)vx--;
			else vx++;
		}

		try{
			if(ParticleData.pmap[y-vy][x]==0)
			{
				setPos(x, y+vy, id);
				ret =  true;
			}
		}
		catch(IndexOutOfBoundsException e)
		{
			if(vy>0)vy--;
			else vy++;
		}
		return ret;
	}
	
	public boolean render()
	{
		RenderUtils.drawPixel(x, y, RenderUtils.PIXR(colour),RenderUtils.PIXG(colour),RenderUtils.PIXB(colour));
		return false;
	}
}
