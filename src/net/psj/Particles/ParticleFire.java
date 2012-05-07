package net.psj.Particles;
import net.psj.RenderUtils;
import net.psj.Utils;
import net.psj.Interface.MenuData;
import net.psj.Simulation.ShaderData;

public class ParticleFire extends Particle{

	public ParticleFire() {
		super("FIRE", new float[]{1.0f,0.1f,0.0f}, 0.04f * CFDS, 0.97f, 0.9f, 0.20f, 0.00f, -0.1f,1, MenuData.MENU_PARTS);
	}

	public boolean render()
	{
		ShaderData.fancy.activate();
		int i = 0,p=0;
		float caddress = Utils.restrict_flt(Utils.restrict_flt((float)life, 0.0f, 200.0f)*3, 0.0f, (200.0f*3)-3)/510;
		//for(int i = -1; i < 2; i++)
		//	for(int p = -1; p < 2; p++)
		//	{
				RenderUtils.drawPixelBlend(x+i, y+p, colour[0],colour[1],colour[2],0.5f);
			//}
		//System.out.println(caddress);
		ShaderData.fancy.deactivate();
		return false;
	}
	
	public void init()
	{
		life = rand.nextInt()%50+120;
	}
}
