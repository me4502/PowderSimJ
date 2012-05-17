package net.psj.Particles;

import net.psj.Interface.MenuData;

public class ParticleDust extends Particle {

	public ParticleDust() {
		super("Dust", new float[] { 1.0f, 0.87f, 0.62f }, 0.02f * CFDS, 0.96f,
				2.7f, 0.80f, 0.00f, 0.1f, 1, MenuData.MENU_PARTS);
	}
}
