package net.psj.Particles;

import net.psj.RenderUtils;

public class ParticleDust extends Particle{

	public ParticleDust()
	{
		super("Dust",RenderUtils.PIXPACK(0xFFE0A0),0.02f * CFDS, 0.96f,0.7f, 0.80f, 0.00f, 0.1f, 0);
	}
}
