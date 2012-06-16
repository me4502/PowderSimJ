package net.psj.Particles;

import net.psj.PowderSimJ;
import net.psj.Interface.MenuData;
import net.psj.Simulation.ParticleData;

public class ParticleClone extends Particle {

	public ParticleClone() {
		super("CLNE", new float[] { 1.0f, 0.81f, 0.06f }, 0.0f, 0.90f, 0.0f,
				0.0f, 0.0f, 0.0f, 0, 0, 0, MenuData.MENU_PARTS);
	}

	public String getExtraData() {
		return " C: " + ctype;
	}

	public boolean update() {
		boolean ret = super.update();
		if (ctype != -0) {
			for (int x2 = (int) x - 1; x2 < x + 1; x2++)
				for (int y2 = (int) y - 1; y2 < y + 1; y2++)
					PowderSimJ.ptypes.create_part(x2, y2, ctype, false);
		} else {
			int rx, ry;
			for (rx = -1; rx < 2; rx++)
				for (ry = -1; ry < 2; ry++)
					if (x + rx >= 0 && y + ry > 0 && x + rx < PowderSimJ.width
							&& y + ry < PowderSimJ.height
							&& (rx != 0 || ry != 0)) {
						Particle r = ParticleData.parts[ParticleData.pmap[(int) y
								+ ry][(int) x + rx]];
						if (r == null)
							continue;
						if (r.id == 0)
							continue;
						if (r.id == id)
							continue;
						ctype = r.id;
						break;
					}
		}
		return ret;
	}
}
