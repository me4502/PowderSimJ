package net.psj.Particles.Tools;

import net.psj.PowderSimJ;
import net.psj.Interface.MenuData;
import net.psj.Particles.Particle;
import net.psj.Simulation.Air;

public class ParticleAir extends Particle{

	public ParticleAir() {
		super("Air", new float[]{1.0f,1.0f,1.0f}, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
				0, 0, 5, MenuData.MENU_TOOLS);
	}

	public void init() {
		Air.pv[(int)y / PowderSimJ.cell][(int)x / PowderSimJ.cell] += 1.0f;
		isDead = true;
	}
	
	public boolean render() {
		return true;
	}
}
