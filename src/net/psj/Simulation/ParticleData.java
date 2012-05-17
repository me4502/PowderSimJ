package net.psj.Simulation;

import net.psj.PowderSimJ;
import net.psj.Particles.Particle;
import net.psj.Particles.ParticleClone;
import net.psj.Particles.ParticleDust;
import net.psj.Particles.ParticleErase;
import net.psj.Particles.ParticleFire;
import net.psj.Particles.ParticleWater;
import net.psj.Walls.Wall;
import net.psj.Walls.WallBasic;

public class ParticleData {
	public static int[][] pmap = new int[PowderSimJ.height][PowderSimJ.width];
	public static Particle[] parts = new Particle[PowderSimJ.height
			* PowderSimJ.width * PowderSimJ.cell];

	public static int latPart = 1;
	public static int gravityMode = 0;

	public Particle create_part(int x, int y, int id, boolean fromBrush) {
		if (x > PowderSimJ.width - PowderSimJ.cell || x < PowderSimJ.cell
				|| y > PowderSimJ.height - PowderSimJ.cell
				|| y < PowderSimJ.cell
				|| wallBlocksParticles(WallData.getWallAt(x, y))) {
			return null;
		}
		Particle newPart = newPartFromID(id);
		if (newPart == null)
			return null;
		if (!(newPart instanceof ParticleErase) && parts[pmap[y][x]] == null
				&& newPart != null && pmap[y][x] == 0) {
			newPart.setPos(x, y, id);
			newPart.init();
			parts[latPart] = newPart;
			pmap[y][x] = id;
			return parts[latPart++];
		} else if (!(newPart instanceof ParticleErase)
				&& parts[pmap[y][x]] != null && pmap[y][x] != 0
				&& parts[pmap[y][x]].id == 2 && id != 2 && newPart != null
				&& fromBrush) {
			ParticleClone p = (ParticleClone) parts[pmap[y][x]];
			p.ctype = id;
		} else if (newPart instanceof ParticleErase
				&& parts[pmap[y][x]] != null)
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
		for (int x = 0; x < PowderSimJ.width; x++)
			for (int y = 0; y < PowderSimJ.height; y++) {
				pmap[y][x] = 0;
			}
		for (int i = 1; i < latPart; i++) {
			if (parts[i] != null) {
				if (parts[i] instanceof Particle) {
					if (parts[i].isDead) {
						kill(i);
						continue;
					}
					int x = parts[i].x;
					int y = parts[i].y;
					if (x > PowderSimJ.width - PowderSimJ.cell
							|| x < PowderSimJ.cell
							|| y > PowderSimJ.height - PowderSimJ.cell
							|| y < PowderSimJ.cell
							|| wallBlocksParticles(WallData.getWallAt(x, y))) {
						kill(i);
						continue;
					}
					pmap[y][x] = i;
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
		for (int i = 1; i < latPart; i++) {
			if (parts[i] == null)
				continue;
			if (parts[i].update()) {
				kill(i);
			}
		}
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
		parts[i].isDead = true;
		pmap[parts[i].y][parts[i].x] = 0;
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
			if (parts[i] != null && pmap[parts[i].y][parts[i].x] != 0) {
				if (parts[i] instanceof Particle)
					parts[i].render();
			}
		}
	}

	public static Particle newPartFromID(int id) {
		if (id == 0)
			return new ParticleErase();
		if (id == 1)
			return new ParticleDust();
		if (id == 2)
			return new ParticleClone();
		if (id == 3)
			return new ParticleWater();
		if (id == 4)
			return new ParticleFire();
		return null;
	}

	public static int PT_NUM = 5;

	public static boolean wallBlocksParticles(Wall w) {
		if (w == null)
			return false;
		if (w instanceof WallBasic)
			return true;
		else
			return false;
	}
}