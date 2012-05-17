package net.psj.Particles;

import net.psj.Interface.MenuData;
import net.psj.Simulation.ParticleData;

public class ParticleFire extends Particle {

	public ParticleFire() {
		super("FIRE", new float[] { 1.0f, 0.2f, 0.0f }, 0.04f * CFDS, 0.97f,
				0.9f, 0.20f, 0.00f, -0.1f, 1, MenuData.MENU_PARTS);
	}

	public boolean update() {
		boolean ret = super.update();
		for (int i = -3; i < 2; i++)
			for (int p = -3; p < 2; p++) {
				Particle part = ParticleData.parts[ParticleData.pmap[(int) y
						+ i][(int) x + p]];
				if (part == null)
					continue;
				if (part.id == 3) {
					isDead = true;
					part.isDead = true;
				}
			}
		return ret;
	}

	public boolean render() {
		super.render();
		// for(int i = -2; i < 1; i++)
		// for(int p = -2; p < 1; p++)
		// {
		// if(p==0 && i == 0)
		// RenderUtils.drawPixelBlend(x+i, y+p,
		// colour[0],colour[1],colour[2],(float)Utils.restrict_flt((float)life,
		// 0f, 100f)/255);
		// else
		// RenderUtils.drawPixelBlend(x+i, y+p,
		// colour[0],colour[1],colour[2],(float)Utils.restrict_flt((float)life,
		// 0f, 100f)/510);
		// }
		return false;
	}

	public void init() {
		life = rand.nextInt() % 50 + 120;
	}
}
