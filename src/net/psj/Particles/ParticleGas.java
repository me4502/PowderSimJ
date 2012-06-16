package net.psj.Particles;

import net.psj.Interface.MenuData;

public class ParticleGas extends Particle {

	public ParticleGas() {
		super("GAS", new float[] { 0.87f, 1.0f, 0.12f }, 0.01f * CFDS, 0.99f,
				1.0f, 0.30f, 0.75f, 0.0f, 600, 0, 1, MenuData.MENU_PARTS);
	}

}
