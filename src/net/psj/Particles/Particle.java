package net.psj.Particles;

import java.util.Random;

import net.Company.Engine;
import net.Company.Rendering;
import net.psj.PowderSimJ;
import net.psj.Utils;
import net.psj.Interface.Menu;
import net.psj.Simulation.Air;
import net.psj.Simulation.ParticleData;
import net.psj.Simulation.Placable;
import net.psj.Simulation.WallData;

public class Particle extends Placable {
	float airdrag;
	float airloss;
	float advection;
	float loss;
	float diffusion;
	float gravity;
	int flammable;
	int explosive;
	int state = 0;// solid,powder,liquid,gas,special
	public int life = 0;

	public int ctype = 0;

	public float x = 0;
	public float y = 0;

	public float temp;

	static int CELL = PowderSimJ.cell;

	public float vy = 0;
	public float vx = 0;

	float lastY, lastX;

	public static final float CFDS = (4.0f / CELL);
	public static final int RAND_MAX = 0x7FFF;

	public boolean isDead = false;

	Random rand = new Random();
	
	public int type = 0;

	public Particle(String name, float[] colour, float airdrag, float airloss,
			float advection, float loss, float diffusion, float gravity, 
			int flammable, int explosive, int state, Menu menu) {
		super(name, colour, 0, menu);
		this.airdrag = airdrag;
		this.airloss = airloss;
		this.advection = advection;
		this.diffusion = diffusion;
		this.flammable = flammable;
		this.explosive = explosive;
		this.gravity = gravity;
		this.state = state;
	}

	public void init() {

	}

	public Particle setPos(float x, float y, int id) {
		this.x = x;
		this.y = y;
		return setId(id);
	}

	public Particle setId(int id) {
		this.id = id;
		ParticleData.pmap[(int) y][(int) x] = id;
		return this;
	}

	public boolean update() {
		// vx++;
		float pGravX, pGravY, pGravD;
		// Gravity mode by Moach
		switch (ParticleData.gravityMode) {
		default:
		case 0:
			pGravX = 0.0f;
			pGravY = gravity;
			break;
		case 1:
			pGravX = pGravY = 0.0f;
			break;
		case 2:
			pGravD = (float) (0.01f - Math.hypot((x - Engine.cenX),
					(y - Engine.cenY)));
			pGravX = gravity * ((float) (x - Engine.cenX) / pGravD);
			pGravY = gravity * ((float) (y - Engine.cenY) / pGravD);
		}
		Air.vx[(int) y / CELL][(int) x / CELL] = Air.vx[(int)y / CELL][(int)x / CELL]
				* airloss + airdrag * vx;
		Air.vy[(int) y / CELL][(int)x / CELL] = Air.vy[(int)y / CELL][(int)x / CELL] * airloss
				+ airdrag * vy;
		vx *= loss;
		vy *= loss;
		vx += advection * Air.vx[(int)y / CELL][(int)x / CELL];
		vy += advection * Air.vy[(int)y / CELL][(int)x / CELL];
		vy += pGravY * 10;
		vx += pGravX * 10;
		vx += diffusion * (rand.nextInt() / (0.5f * RAND_MAX) - 1.0f) / 10000;
		vy += diffusion * (rand.nextInt() / (0.5f * RAND_MAX) - 1.0f) / 10000;
		if (explosive > 0 && Air.pv[(int) (y/CELL)][(int) (x/CELL)]>2.5f)
		{
			life = rand.nextInt(80)+180;
			temp = Utils.restrict_flt(temp + (flammable/2), PowderSimJ.MIN_TEMP, PowderSimJ.MAX_TEMP);
			Air.pv[(int) (y/CELL)][(int) (x/CELL)] += 0.25f * CFDS;
			PowderSimJ.ptypes.change_part(this, (int)x, (int)y, 4);
			return false;
		}

		motion: {
			try {
				if (vx > 0) {
					for (float xx = vx; xx < 0; xx--) {
						if (ParticleData.parts[ParticleData.pmap[(int)y][(int)x + (int)xx]].id > -1) {
							vx = 0;
							break motion;
						}
						if (ParticleData.wallBlocksParticles(WallData
								.getWallAt((int)x + (int)xx, (int)y))) {
							vx = 0;
							break motion;
						}
					}
				} else if (vx < 0) {
					for (float xx = vx; xx > 0; xx++) {
						if (ParticleData.parts[ParticleData.pmap[(int)y][(int)x + (int)xx]].id > -1) {
							vx = 0;
							break motion;
						}
						if (ParticleData.wallBlocksParticles(WallData
								.getWallAt((int)x + (int)xx, (int)y))) {
							vx = 0;
							break motion;
						}
					}
				}
				if (x + vx > PowderSimJ.width - PowderSimJ.cell
						|| x + vx < PowderSimJ.cell
						|| y > PowderSimJ.height - PowderSimJ.cell
						|| y < PowderSimJ.cell) {
					return true;
				}
				if (!tryMove((int)x + (int)vx, (int)y))
					vx = 0;
			} catch (IndexOutOfBoundsException e) {
				if (vx > 0)
					vx--;
				else
					vx++;
			}
			try {
				if (vy > 0) {
					for (float yy = vy; yy < 0; yy--) {
						if (ParticleData.wallBlocksParticles(WallData
								.getWallAt((int)x, (int)y + (int)yy))) {
							vy = 0;
							break motion;
						}
						if (ParticleData.parts[ParticleData.pmap[(int)y + (int)yy][(int)x]].id > -1) {
							vy = 0;
							break motion;
						}
					}
				} else if (vy < 0) {
					for (float yy = vy; yy > 0; yy++) {
						if (ParticleData.wallBlocksParticles(WallData
								.getWallAt((int)x, (int)y + (int)yy))) {
							vy = 0;
							break motion;
						}

						if (ParticleData.parts[ParticleData.pmap[(int)y + (int)yy][(int)x]].id > -1) {
							vy = 0;
							break motion;
						}
					}
				}
				if (x > PowderSimJ.width - PowderSimJ.cell
						|| x < PowderSimJ.cell
						|| y + vy > PowderSimJ.height - PowderSimJ.cell
						|| y + vy < PowderSimJ.cell) {
					return true;
				}
				if (!tryMove((int)x, (int)y + (int)vy))
					vy = 0;
			} catch (IndexOutOfBoundsException e) {
				if (vy > 0)
					vy--;
				else
					vy++;
			}

			if (state == 1 && lastY == y) // Powder stuff.
			{
				if (!tryMove((int)x, (int)y + 1)) {
					int c = rand.nextInt(5) - 2;
					tryMove((int)x + c, (int)y + 1);
				}
			}
			else if (state == 2 && lastY == y) // Liquid stuff.
			{
				if (!tryMove((int)x, (int)y + 1)) {
					int c = rand.nextInt(9) - 5;
					if (!tryMove((int)x + c, (int)y + 1)) {
						tryMove((int)x + c, (int)y);
					}
				}
			}
		}
		if (life > 0)
			life--;
		lastY = y;
		lastX = x;
		return false;
	}

	public boolean tryMove(int x, int y) {
		boolean ret = false;
		motion: {
			try {
				Particle r = ParticleData.parts[ParticleData.pmap[y][x]];
				if (r != null)
					break motion;
				if (ParticleData.wallBlocksParticles(WallData.getWallAt(x, y)))
					break motion;
				setPos(x, y, id);
				ret = true;
			} catch (Exception e) {
				e.printStackTrace();
				ret = false;
			}
		}
		return ret;
	}

	public boolean render() {
		if(ParticleData.renderMode == 0)
			Rendering.drawPixel((int)x, (int)y, colour[0], colour[1], colour[2]);
		else if(ParticleData.renderMode == 1) //Blob
			Rendering.drawBlob((int)x, (int)y, colour[0], colour[1], colour[2], 255);
		return false;
	}

	public String getExtraData() {
		return "";
	}
}
