package net.psj.Particles;

import net.Company.Rendering;
import net.psj.PowderSimJ;
import net.psj.Utils;
import net.psj.Interface.MenuData;
import net.psj.Simulation.Air;
import net.psj.Simulation.ParticleData;

public class ParticleFire extends Particle {

	public ParticleFire() {
		super("FIRE", new float[] { 1.0f, 0.2f, 0.0f }, 0.04f * CFDS, 0.97f,
				0.9f, 0.20f, 0.00f, -0.1f, 0, 0, 1, MenuData.MENU_PARTS);
	}

	public boolean update() {
		boolean ret = super.update();
		if(life<=0) {
			this.isDead = true;
			return ret;
		}
		for (int i = -3; i < 2; i++)
			for (int p = -3; p < 2; p++) {
				Particle part = ParticleData.parts[ParticleData.pmap[(int) y + i][(int) x + p]];
				if (part == null)
					continue;
				if (part instanceof ParticleWater) {
					isDead = true;
					part.isDead = true;
				}
				if((part.explosive>0 || part.flammable > 0) && (part.temp < 2273.15f && Air.pv[(int)y/CELL][(int)x/CELL] < 50.0f) && part.flammable > 0 && (part.flammable + (int)(Air.pv[(int)(y+i)/CELL][(int)(x+p)/CELL]*10.0f))>(rand.nextInt(10000)))
				{
					PowderSimJ.ptypes.change_part(part, (int)part.x, (int)part.y, 4);
					part = ParticleData.parts[ParticleData.pmap[(int) y + i][(int) x + p]];
					if(part == null) continue;
					part.temp = Utils.restrict_flt(part.temp + (part.flammable/2), PowderSimJ.MIN_TEMP, PowderSimJ.MAX_TEMP);
					part.life = rand.nextInt(80)+180;
					part.ctype = 0;
					if(part.explosive > 0)
						Air.pv[(int) (y/CELL)][(int) (x/CELL)] += 0.25f * CFDS;
				}
			}
		return ret;
	}

	public boolean render() {
		///ShaderData.fire.activate();
		//super.render();
		// for(int i = -2; i < 1; i++)
		// for(int p = -2; p < 1; p++)
		// {
		// if(p==0 && i == 0)
		//RenderUtils.drawPixelBlend(x+i, y+p,
		// colour[0],colour[1],colour[2],(float)Utils.restrict_flt((float)life,
		// 0f, 100f)/255);
		// else
		// RenderUtils.drawPixelBlend(x+i, y+p,
		// colour[0],colour[1],colour[2],(float)Utils.restrict_flt((float)life,
		// 0f, 100f)/510);
		// }
		Rendering.drawFire((int)x,(int)y,colour[0],colour[1],colour[2],(float)Utils.restrict_flt((float)life,0f, 100f)/255);
		//ShaderData.fire.deactivate();
		return true;
	}

	public void init() {
		life = rand.nextInt() % 50 + 120;
	}
}
