package net.psj.Particles;

import java.util.Random;

import net.psj.ParticleData;
import net.psj.Placable;
import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Interface.Menu;
import net.psj.Simulation.Air;
import net.psj.Simulation.WallData;

public class Particle extends Placable
{
	float airdrag;
	float airloss;
	float advection;
	float loss;
	float diffusion;
	float gravity;
	int state = 0;//solid,powder,liquid,gas,special
	
	public int x = 0;
	public int y = 0;
	
	public int temp;
	
	static int CELL = PowderSimJ.cell;
	
	public int vy = 0;
	public int vx = 0;
	
	public static final float CFDS = (4.0f/CELL);
	public static final int RAND_MAX = 0x7FFF;
	
	Random rand = new Random();
	
	public Particle(String name, float[] colour, float airdrag, float airloss, float advection, float loss, float diffusion, float gravity, int state, Menu menu)
	{
		super(name,colour,0,menu);
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
		motion:
		{
			try{
				if(vx>0)
				{
					for(int xx = vx; xx < 0; xx--)
					{
						if(ParticleData.parts[ParticleData.pmap[y][x+xx]].id!=0)
						{
							vx = 0;
							break motion;
						}
						if(ParticleData.wallBlocksParticles(WallData.getWallAt(x+xx,y)))
						{
							System.out.println("IT ONE A DEM WALLS");
							vx = 0;
							break motion;
						}
					}
				}
				else if(vx<0)
				{
					for(int xx = vx; xx > 0; xx++)
					{
						if(ParticleData.parts[ParticleData.pmap[y][x+xx]].id!=0)
						{
							vx = 0;
							break motion;
						}
						if(ParticleData.wallBlocksParticles(WallData.getWallAt(x+xx,y)))
						{
							vx = 0;
							break motion;
						}
					}
				}
				if(x+vx>PowderSimJ.width - PowderSimJ.cell || x+vx<PowderSimJ.cell || y>PowderSimJ.height - PowderSimJ.cell || y<PowderSimJ.cell)
				{
					return true;
				}
				if(ParticleData.pmap[y][x+vx]==0 && !ParticleData.wallBlocksParticles(WallData.getWallAt(x+vx,y)))
				{
					setPos(x+vx, y, id);
				}
				else
					vx = 0;
			}
			catch(IndexOutOfBoundsException e)
			{
				if(vx>0)vx--;
				else vx++;
			}
			try{
				if(vy>0)
				{
					for(int yy = vy; yy < 0; yy--)
					{
						if(ParticleData.wallBlocksParticles(WallData.getWallAt(x,y+yy)))
						{
							vy = 0;
							break motion;
						}
						if(ParticleData.parts[ParticleData.pmap[y+yy][x]].id!=0)
						{
							vy = 0;
							break motion;
						}
					}
				}
				else if(vy<0)
				{
					for(int yy = vy; yy > 0; yy++)
					{
						if(ParticleData.wallBlocksParticles(WallData.getWallAt(x,y+yy)))
						{
							System.out.println("IT ONE A DEM WALLS");
							vy = 0;
							break motion;
						}

						if(ParticleData.parts[ParticleData.pmap[y+yy][x]].id!=0)
						{
							vy = 0;
							break motion;
						}
					}
				}
				if(x>PowderSimJ.width - PowderSimJ.cell || x<PowderSimJ.cell || y+vy>PowderSimJ.height - PowderSimJ.cell || y+vy<PowderSimJ.cell)
				{
					return true;
				}
				if(ParticleData.pmap[y+vy][x]==0 && !ParticleData.wallBlocksParticles(WallData.getWallAt(x,y+vy)))
				{
					setPos(x, y+vy, id);
				}
				else
					vy = 0;
			}
			catch(IndexOutOfBoundsException e)
			{
				if(vy>0)vy--;
				else vy++;
			}
		}
		return false;
	}
	
	public boolean render()
	{
		RenderUtils.drawPixel(x, y, colour[0],colour[1],colour[2]);
		return false;
	}
}
