package net.psj.Interface;

import net.Company.Rendering;
import net.psj.PowderSimJ;
import net.psj.Particles.Particle;
import net.psj.Simulation.Air;
import net.psj.Simulation.ParticleData;
import net.psj.Simulation.WallData;
import net.psj.Walls.Wall;

public class Overlay {

	public static void drawInfoBar() {
		// int w = PowderSimJ.width;
		// int h = PowderSimJ.height;

		Rendering.drawFont(4, 6, "PowderSimJ version " + PowderSimJ.version
				+ " FPS: " + PowderSimJ.getGame().getFPS() * 2, 1.0f, 1.0f,
				1.0f);
	}

	public static void drawPixInfo() {
		int w = PowderSimJ.width;
		// int h = PowderSimJ.height;

		int x = PowderSimJ.engine.mouseX;
		int y = PowderSimJ.engine.mouseY;
		int c = PowderSimJ.cell;

		String text = "X: " + x + " Y: " + y;
		Rendering.drawFont(w - Rendering.getFontWidth(text), 6, text, 1.0f,
				1.0f, 1.0f);

		if (PowderSimJ.isInPlayField(x, y)) {
			String text2 = "P: " + Math.round(Air.pv[y / c][x / c] * 100)
					+ " X: " + Math.round(Air.vx[y / c][x / c] * 100) + " Y: "
					+ Math.round(Air.vy[y / c][x / c] * 100);
			Rendering.drawFont(w - Rendering.getFontWidth(text2), 14, text2,
					1.0f, 1.0f, 1.0f);

			if (ParticleData.getParticleAt(x, y) != null) {
				Particle p = ParticleData.parts[ParticleData.pmap[y][x]];
				String text3 = "Name: " + p.name + " Temp: " + p.temp
						+ " Life: " + p.life + p.getExtraData();
				Rendering.drawFont(w - Rendering.getFontWidth(text3), 22,
						text3, 1.0f, 1.0f, 1.0f);
			} else if (WallData.bmap[y / c][x / c] != null) {
				Wall ww = WallData.bmap[y / c][x / c];
				String text3 = "Name: " + ww.name + ww.getExtraData();
				Rendering.drawFont(w - Rendering.getFontWidth(text3), 22,
						text3, 1.0f, 1.0f, 1.0f);
			}
		}

		if (PowderSimJ.engine.Debug) {
			int height = 14;
			if (PowderSimJ.isInPlayField(x, y)) {
				if (ParticleData.parts[ParticleData.pmap[y][x]] != null
						|| WallData.bmap[y / c][x / c] != null)
					height = 30;
				else
					height = 22;
			}
			String text4 = "L: " + PowderSimJ.selectedl + " R: "
					+ PowderSimJ.selectedr;
			Rendering.drawFont(w - Rendering.getFontWidth(text4), height,
					text4, 1.0f, 1.0f, 1.0f);
		}
	}
}
