package net.psj.Particles;

import net.psj.PowderSimJ;
import net.psj.Interface.Menu;

public class ParticleClone extends Particle{

	public ParticleClone() {
		super("CLNE", new float[]{1.0f,0.81f,0.06f}, 0.0f, 0.90f, 0.0f, 0.0f, 0.0f, 0.0f, 0,Menu.MENU_PARTS);
	}

	public boolean update()
	{
		boolean ret = super.update();
		for(int x2 = x-1; x2 < x+1; x2++)
			for(int y2 = y-1; y2 < y+1; y2++)
				PowderSimJ.ptypes.create_part(x2,y2,1);
		return ret;
	}
}
