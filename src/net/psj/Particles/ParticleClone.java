package net.psj.Particles;

import net.psj.ParticleData;
import net.psj.PowderSimJ;
import net.psj.Interface.MenuData;

public class ParticleClone extends Particle{

	public int cloneID = -1;
	
	public ParticleClone() {
		super("CLNE", new float[]{1.0f,0.81f,0.06f}, 0.0f, 0.90f, 0.0f, 0.0f, 0.0f, 0.0f, 0,MenuData.MENU_PARTS);
		cloneID = -1;
	}

	public boolean update()
	{
		boolean ret = super.update();
		if(cloneID!=-1)
		{
			for(int x2 = x-1; x2 < x+1; x2++)
				for(int y2 = y-1; y2 < y+1; y2++)
					PowderSimJ.ptypes.create_part(x2,y2,cloneID);
		}
		else
		{
			int rx, ry;
			for (rx=-3; rx<4; rx++)
				for (ry=-3; ry<4; ry++)
					if (x+rx>=0 && y+ry>0 && x+rx<PowderSimJ.width && y+ry<PowderSimJ.height && (rx!=0 || ry!=0))
					{
						Particle r = ParticleData.parts[ParticleData.pmap[y+ry][x+rx]];
						if (r==null)
							continue;
						if(r.id==0)
							continue;
						if(r.id==id)
							continue;
						cloneID = r.id;
						break;
					}
		}
		return ret;
	}
}
