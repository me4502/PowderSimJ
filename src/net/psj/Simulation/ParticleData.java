package net.psj.Simulation;

import java.lang.reflect.Constructor;

import net.psj.PowderSimJ;
import net.psj.Particles.Particle;
import net.psj.Particles.ParticleC4;
import net.psj.Particles.ParticleClone;
import net.psj.Particles.ParticleDust;
import net.psj.Particles.ParticleErase;
import net.psj.Particles.ParticleFire;
import net.psj.Particles.ParticleGas;
import net.psj.Particles.ParticleWater;
import net.psj.Particles.ParticleWood;
import net.psj.Particles.Tools.ParticleAir;
import net.psj.Particles.Tools.ParticleVac;
import net.psj.Walls.Wall;
import net.psj.Walls.WallBasic;

public class ParticleData {
	public static int[][] pmap = new int[PowderSimJ.height][PowderSimJ.width];
	public static Particle[] parts = new Particle[PowderSimJ.height
	                                              * PowderSimJ.width * PowderSimJ.cell];

	public static int latPart = 1;
	public static int gravityMode = 0;
	public static int renderMode = 0;

	public static Particle getParticleAt(int x, int y) {
		if(x<0 || y<0) return null;
		if(pmap[y][x]==-1) return null;
		Particle part = parts[pmap[y][x]];
		if(part == null) return null;
		return part;
	}
	
	public Particle change_part(Particle old, int x, int y, int id)
	{
		if(!(getParticleAt(x,y) == null))
			kill(getParticleAt(x,y).id);
		return create_part(x,y,id,false); 
	}

	public Particle create_part(int x, int y, int id, boolean fromBrush) {
		if (x > PowderSimJ.width - PowderSimJ.cell || x < PowderSimJ.cell
				|| y > PowderSimJ.height - PowderSimJ.cell
				|| y < PowderSimJ.cell
				|| wallBlocksParticles(WallData.getWallAt(x, y))) {
			return null;
		}
		Particle newPart = ParticleEnum.fromID(id);
		if (newPart == null)
			return null;
		if (!(newPart instanceof ParticleErase) && getParticleAt(x,y) == null
				&& newPart != null && pmap[y][x] == -1) {
			latPart++;
			newPart.setPos(x, y, latPart);
			newPart.type = id;
			newPart.init();
			parts[latPart] = newPart;
			pmap[y][x] = latPart;
			return parts[latPart];
		} else if (!(newPart instanceof ParticleErase)
				&& getParticleAt(x,y) != null
				&& getParticleAt(x,y) instanceof ParticleClone && id != 2 && newPart != null
				&& fromBrush) {
			ParticleClone p = (ParticleClone) getParticleAt(x,y);
			p.ctype = id;
		} else if (newPart instanceof ParticleErase && getParticleAt(x,y) != null)
			kill(pmap[y][x]);

		return null;
	}

	public void create_parts(int x1, int y1, int id) {
		x1 = x1 - (PowderSimJ.brushSize / 2);
		y1 = y1 - (PowderSimJ.brushSize / 2);
		for (int x = x1; x < x1 + PowderSimJ.brushSize; x++) {
			for (int y = y1; y < y1 + PowderSimJ.brushSize; y++) {
				create_part(x, y, id, true);
			}
		}
	}

	public void update() {
		int part = 0;
		for (int i = 1; i < latPart; i++) {
			if (parts[i] == null)
				continue;
			if (parts[i].update()) {
				kill(i);
			}
		}

		for (int x = 0; x < PowderSimJ.width; x++)
			for (int y = 0; y < PowderSimJ.height; y++) {
				pmap[y][x] = -1;
			}
		for (int i = 1; i < latPart; i++) {
			if (parts[i] != null) {
				if (parts[i] instanceof Particle) {
					if (parts[i].isDead) {
						kill(i);
						continue;
					}
					float x = parts[i].x;
					float y = parts[i].y;
					if (x > PowderSimJ.width - PowderSimJ.cell
							|| x < PowderSimJ.cell
							|| y > PowderSimJ.height - PowderSimJ.cell
							|| y < PowderSimJ.cell
							|| wallBlocksParticles(WallData.getWallAt((int)x, (int)y))) {
						kill(i);
						continue;
					}
					pmap[(int)y][(int)x] = i;
					part++;
				} else
					kill(i);
			} else {
				moveDown(i);
				i--;
			}
		}
		if (part == 0)
			latPart = 1;
	}

	public static void moveDown(int i) {
		parts[i] = parts[latPart];
		parts[latPart] = null;
		// for(int p = i; p < latPart; p++)
		// {
		// if(p!=latPart)
		// parts[p] = parts[p+1];
		// else
		// parts[p] = null;
		// }
		latPart--;
	}

	public static void kill(int i) {
		if(parts[i] == null) return;
		parts[i].isDead = true;
		pmap[(int)parts[i].y][(int)parts[i].x] = -1;
		parts[i] = null;
		if (latPart > 600000) {
			System.out.println(latPart);
			moveDown(i);
			System.out.println(latPart);
		}
	}

	public void render() {
		if (latPart == 0)
			return;
		for (int i = 1; i < latPart; i++) {
			if (parts[i] != null && pmap[(int)parts[i].y][(int)parts[i].x] != -1) {
				if (parts[i] instanceof Particle)
					parts[i].render();
				else
					kill(i);
			}
		}
	}
	public static int PT_NUM = 8;
	public static int PT_TOOLS = 2;

	public static boolean wallBlocksParticles(Wall w) {
		if (w == null)
			return false;
		if (w instanceof WallBasic)
			return true;
		else
			return false;
	}

	public enum ParticleEnum {

		ERASE(0, ParticleErase.class), DUST(1, ParticleDust.class), CLONE(2, ParticleClone.class), 
		WATER(3, ParticleWater.class), FIRE(4, ParticleFire.class), GAS(5, ParticleGas.class),
		C4(6, ParticleC4.class), WOOD(7, ParticleWood.class),

		AIR(100, ParticleAir.class), VAC(101, ParticleVac.class);

		private int id;
		private Class<?> partClass;

		private ParticleEnum(int id, Class<?> partClass) {
			this.id = id;
			this.partClass = partClass;
		}

		public int getId() {
			return id;
		}

		@SuppressWarnings("rawtypes")
		public Particle getPart() {
			Constructor[] ctors = partClass.getDeclaredConstructors();
			Constructor ctor = null;
			for (int i = 0; i < ctors.length; i++) {
			    ctor = ctors[i];
			    if (ctor.getGenericParameterTypes().length == 0)
				break;
			}

			try {
			    ctor.setAccessible(true);
		 	    Particle p = (Particle)ctor.newInstance();
		 	    
		 	    return p;
			} catch (Exception e) {
			    e.printStackTrace();
		 	}
			
			return null;
		}
		
		public static Particle fromID(int id) {
			for(ParticleEnum e : values()) {
				if(e.getId() == id)
					return e.getPart();
			}
			return null;
		}
	}
}