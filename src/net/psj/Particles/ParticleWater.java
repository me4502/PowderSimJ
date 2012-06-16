package net.psj.Particles;

import net.psj.Interface.MenuData;

public class ParticleWater extends Particle {

	public ParticleWater() {
		super("WATR", new float[] { 0.12f, 0.18f, 0.81f }, 0.01f * CFDS, 0.98f,
				3.0f, 0.95f, 0.00f, 0.1f, 0, 0, 2, MenuData.MENU_PARTS);
	}
}
