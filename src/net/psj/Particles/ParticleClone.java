package net.psj.Particles;

import net.psj.PowderSimJ;

public class ParticleClone extends Particle{

	public ParticleClone() {
		super("CLNE", 0xFFD010, 0.0f, 0.90f, 0.0f, 0.0f, 0.0f, 1);
	}

	public boolean update()
	{
		boolean ret = super.update();
		PowderSimJ.ptypes.create_part(x,y,1);
		return ret;
	}
}
