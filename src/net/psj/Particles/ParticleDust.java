package net.psj.Particles;

public class ParticleDust extends Particle{

	public ParticleDust()
	{
		super("Dust",0xE6CFA5,0.1f,0.3f, 1.0f);
	}
	
	public ParticleDust(String name, int colour, float airdrag, float advection, float loss) {
		super(name, colour, airdrag, advection, loss);
	}

}
