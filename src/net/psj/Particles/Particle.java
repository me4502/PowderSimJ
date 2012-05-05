package net.psj.Particles;

import java.util.Random;

import net.psj.ParticleData;
import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Simulation.Air;

public class Particle 
{
	public String name;
	public float[] colour;
	float airdrag;
	float airloss;
	float advection;
	float loss;
	float diffusion;
	float gravity;
	int state = 0;//solid,powder,liquid,gas,special
	public int menu = 0;
	
	public int x = 0;
	public int y = 0;
	public int id = 0;
	
	public int temp;
	
	static int CELL = PowderSimJ.cell;
	
	public int vy = 0;
	public int vx = 0;
	
	public static final float CFDS = (4.0f/CELL);
	public static final int RAND_MAX = 0x7FFF;
	
	Random rand = new Random();
	
	public Particle(String name, float[] colour, float airdrag, float airloss, float advection, float loss, float diffusion, float gravity, int state)
	{
		this.name = name;
		this.colour = colour;
		this.airdrag = airdrag;
		this.airloss = airloss;
		this.advection = advection;
		this.diffusion = diffusion;
		this.gravity = gravity;
		this.state = state;
	}
	
	public Particle setPos(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
		return this;
	}
	
	public boolean update()
	{
		//vx++;
		boolean ret = false;
		float pGravX,pGravY,pGravD;
		//Gravity mode by Moach
		switch (ParticleData.gravityMode)
		{
		default:
		case 0:
			pGravX = 0.0f;
			pGravY = gravity;
			break;
		case 1:
			pGravX = pGravY = 0.0f;
			break;
		case 2:
			pGravD = (float) (0.01f - Math.hypot((x - PowderSimJ.cenX), (y - PowderSimJ.cenY)));
			pGravX = gravity * ((float)(x - PowderSimJ.cenX) / pGravD);
			pGravY = gravity * ((float)(y - PowderSimJ.cenY) / pGravD);
		}
		Air.vx[y/CELL][x/CELL] = Air.vx[y/CELL][x/CELL]*airloss + airdrag*vx;
		Air.vy[y/CELL][x/CELL] = Air.vy[y/CELL][x/CELL]*airloss + airdrag*vy;
		vx *= loss;
		vy *= loss;
		vx += advection*Air.vx[y/CELL][x/CELL];
		vy += advection*Air.vy[y/CELL][x/CELL];
		vy += pGravY*10;
		vx += pGravX*10;
		vx += diffusion*(rand.nextInt()/(0.5f*RAND_MAX)-1.0f)*10;
		vy += diffusion*(rand.nextInt()/(0.5f*RAND_MAX)-1.0f)*10;
		try{
			if(ParticleData.pmap[y][x+vx]==0)
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
			if(ParticleData.pmap[y+vy][x]==0)
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
		RenderUtils.drawPixel(x, y, colour[0],colour[1],colour[2]);
		return false;
	}
}
