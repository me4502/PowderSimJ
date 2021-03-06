package net.psj.Simulation;

import net.Company.Rendering;
import net.psj.PowderSimJ;
import net.psj.Walls.WallBasic;
import net.psj.Walls.WallFan;

public class Air {

	int YRES = PowderSimJ.height;
	int XRES = PowderSimJ.width;
	int CELL = PowderSimJ.cell;

	int airMode = 0;
	int displayAir = 1;

	float AIR_TSTEPP = 0.3f;
	float AIR_TSTEPV = 0.4f;
	float AIR_VADV = 0.3f;
	float AIR_VLOSS = 0.999f;
	float AIR_PLOSS = 0.9999f;

	float[] kernel = new float[9];

	public static float[][] vx = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	float[][] ovx = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	public static float[][] vy = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	public float[][] ovy = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	public static float[][] pv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	float[][] opv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	boolean[][] bmap_blockair = new boolean[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	boolean[][] bmap_blockairh = new boolean[PowderSimJ.height
			/ PowderSimJ.cell][PowderSimJ.width / PowderSimJ.cell];

	float[][] cb_vx = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	float[][] cb_vy = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	float[][] cb_pv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	float[][] cb_hv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];

	public float[][] fvx = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];
	public float[][] fvy = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell];

	public float[][] hv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
			/ PowderSimJ.cell],
			ohv = new float[PowderSimJ.height / PowderSimJ.cell][PowderSimJ.width
					/ PowderSimJ.cell]; // For Ambient Heat

	public void make_kernel() // used for velocity
	{
		int i, j;
		float s = 0.0f;
		for (j = -1; j < 2; j++)
			for (i = -1; i < 2; i++) {
				kernel[(i + 1) + 3 * (j + 1)] = (float) Math.exp(-2.0f
						* (i * i + j * j));
				s += kernel[(i + 1) + 3 * (j + 1)];
			}
		s = 1.0f / s;
		for (j = -1; j < 2; j++)
			for (i = -1; i < 2; i++)
				kernel[(i + 1) + 3 * (j + 1)] *= s;
	}

	public void update_airh() {
		int x, y, i, j;
		float odh, dh, dx, dy, f, tx, ty;
		for (i = 0; i < XRES / CELL; i++) // reduces pressure/velocity on the
											// edges every frame
		{
			hv[0][i] = 295.15f;
			hv[1][i] = 295.15f;
			hv[YRES / CELL - 3][i] = 295.15f;
			hv[YRES / CELL - 2][i] = 295.15f;
			hv[YRES / CELL - 1][i] = 295.15f;
		}
		for (y = 0; y < YRES / CELL; y++) // update velocity and pressure
		{
			// reduce
			hv[y][0] = 295.15f;
			hv[y][1] = 295.15f;
			hv[y][XRES / CELL - 3] = 295.15f;
			hv[y][XRES / CELL - 2] = 295.15f;
			hv[y][XRES / CELL - 1] = 295.15f;

			hv[y][0] = 295.15f;
			hv[y][1] = 295.15f;
			hv[y][XRES / CELL - 3] = 295.15f;
			hv[y][XRES / CELL - 2] = 295.15f;
			hv[y][XRES / CELL - 1] = 295.15f;
			for (x = 0; x < XRES / CELL; x++) {
				bmap_blockairh[y][x] = WallData.bmap[y][x] != null
						&& (WallData.bmap[y][x] instanceof WallBasic);
				dh = 0.0f;
				dx = 0.0f;
				dy = 0.0f;
				for (j = -1; j < 2; j++) {
					for (i = -1; i < 2; i++) {
						if (y + j > 0 && y + j < YRES / CELL - 2 && x + i > 0
								&& x + i < XRES / CELL - 2
								&& !bmap_blockairh[y + j][x + i]) {
							f = kernel[i + 1 + (j + 1) * 3];
							dh += hv[y + j][x + i] * f;
							dx += vx[y + j][x + i] * f;
							dy += vy[y + j][x + i] * f;
						} else {
							f = kernel[i + 1 + (j + 1) * 3];
							dh += hv[y][x] * f;
							dx += vx[y][x] * f;
							dy += vy[y][x] * f;
						}
					}
				}
				tx = x - dx * 0.7f;
				ty = y - dy * 0.7f;
				i = (int) tx;
				j = (int) ty;
				tx -= i;
				ty -= j;
				if (i >= 2 && i < XRES / CELL - 3 && j >= 2
						&& j < YRES / CELL - 3) {
					odh = dh;
					dh *= 1.0f - AIR_VADV;
					dh += AIR_VADV * (1.0f - tx) * (1.0f - ty)
							* (bmap_blockairh[j][i] ? odh : hv[j][i]);
					dh += AIR_VADV * tx * (1.0f - ty)
							* (bmap_blockairh[j][i + 1] ? odh : hv[j][i + 1]);
					dh += AIR_VADV * (1.0f - tx) * ty
							* (bmap_blockairh[j + 1][i] ? odh : hv[j + 1][i]);
					dh += AIR_VADV
							* tx
							* ty
							* (bmap_blockairh[j + 1][i + 1] ? odh
									: hv[j + 1][i + 1]);
				}
				// if(!gravityMode){ //Vertical gravity only for the time being
				// float airdiff = dh-hv[y][x];
				// pv[y][x] += airdiff/5000.0f;
				// if(airdiff>0)
				// vy[y][x] -= airdiff/5000.0f;
				// }
				ohv[y][x] = dh;
			}
		}
		hv = ohv;
		// memcpy(hv, ohv, sizeof(hv));
	}

	public void update_air() {
		int x, y, i, j;
		float dp, dx, dy, f, tx, ty;

		for (y = 0; y < YRES / CELL; y++)
			for (x = 0; x < XRES / CELL; x++) {
				bmap_blockair[y][x] = WallData.bmap[y][x] != null
						&& (WallData.bmap[y][x] instanceof WallBasic);
			}

		if (airMode != 4) { // airMode 4 is no air/pressure update
			for (i = 0; i < XRES / CELL; i++) // reduces pressure/velocity on
												// the edges every frame
			{
				pv[0][i] = pv[0][i] * 0.8f;
				pv[1][i] = pv[1][i] * 0.8f;
				pv[2][i] = pv[2][i] * 0.8f;
				pv[YRES / CELL - 2][i] = pv[YRES / CELL - 2][i] * 0.8f;
				pv[YRES / CELL - 1][i] = pv[YRES / CELL - 1][i] * 0.8f;
				vx[0][i] = vx[1][i] * 0.9f;
				vx[1][i] = vx[2][i] * 0.9f;
				vx[YRES / CELL - 2][i] = vx[YRES / CELL - 3][i] * 0.9f;
				vx[YRES / CELL - 1][i] = vx[YRES / CELL - 2][i] * 0.9f;
				vy[0][i] = vy[1][i] * 0.9f;
				vy[1][i] = vy[2][i] * 0.9f;
				vy[YRES / CELL - 2][i] = vy[YRES / CELL - 3][i] * 0.9f;
				vy[YRES / CELL - 1][i] = vy[YRES / CELL - 2][i] * 0.9f;
			}
			for (j = 1; j < YRES / CELL; j++) // clear some velocities near
												// walls
			{
				pv[j][0] = pv[j][0] * 0.8f;
				pv[j][1] = pv[j][1] * 0.8f;
				pv[j][2] = pv[j][2] * 0.8f;
				pv[j][XRES / CELL - 2] = pv[j][XRES / CELL - 2] * 0.8f;
				pv[j][XRES / CELL - 1] = pv[j][XRES / CELL - 1] * 0.8f;
				vx[j][0] = vx[j][1] * 0.9f;
				vx[j][1] = vx[j][2] * 0.9f;
				vx[j][XRES / CELL - 2] = vx[j][XRES / CELL - 3] * 0.9f;
				vx[j][XRES / CELL - 1] = vx[j][XRES / CELL - 2] * 0.9f;
				vy[j][0] = vy[j][1] * 0.9f;
				vy[j][1] = vy[j][2] * 0.9f;
				vy[j][XRES / CELL - 2] = vy[j][XRES / CELL - 3] * 0.9f;
				vy[j][XRES / CELL - 1] = vy[j][XRES / CELL - 2] * 0.9f;
				for (i = 1; i < XRES / CELL; i++) {
					if (bmap_blockair[j][i]) {
						vx[j][i] = 0.0f;
						vx[j][i - 1] = 0.0f;
						vy[j][i] = 0.0f;
						vy[j - 1][i] = 0.0f;
					}
				}
			}

			for (y = 1; y < YRES / CELL; y++)
				// pressure adjustments from velocity
				for (x = 1; x < XRES / CELL; x++) {
					dp = 0.0f;
					dp += vx[y][x - 1] - vx[y][x];
					dp += vy[y - 1][x] - vy[y][x];
					pv[y][x] *= AIR_PLOSS;
					pv[y][x] += dp * AIR_TSTEPP;
				}

			for (y = 0; y < YRES / CELL - 1; y++)
				// velocity adjustments from pressure
				for (x = 0; x < XRES / CELL - 1; x++) {
					dx = dy = 0.0f;
					dx += pv[y][x] - pv[y][x + 1];
					dy += pv[y][x] - pv[y + 1][x];
					vx[y][x] *= AIR_VLOSS;
					vy[y][x] *= AIR_VLOSS;
					vx[y][x] += dx * AIR_TSTEPV;
					vy[y][x] += dy * AIR_TSTEPV;
					if (bmap_blockair[y][x] || bmap_blockair[y][x + 1])
						vx[y][x] = 0;
					if (bmap_blockair[y][x] || bmap_blockair[y + 1][x])
						vy[y][x] = 0;
				}

			for (y = 0; y < YRES / CELL; y++)
				// update velocity and pressure
				for (x = 0; x < XRES / CELL; x++) {
					dx = 0.0f;
					dy = 0.0f;
					dp = 0.0f;
					for (j = -1; j < 2; j++)
						for (i = -1; i < 2; i++)
							if (y + j > 0 && y + j < YRES / CELL - 1
									&& x + i > 0 && x + i < XRES / CELL - 1
									&& !bmap_blockair[y + j][x + i]) {
								f = kernel[i + 1 + (j + 1) * 3];
								dx += vx[y + j][x + i] * f;
								dy += vy[y + j][x + i] * f;
								dp += pv[y + j][x + i] * f;
							} else {
								f = kernel[i + 1 + (j + 1) * 3];
								dx += vx[y][x] * f;
								dy += vy[y][x] * f;
								dp += pv[y][x] * f;
							}

					tx = x - dx * 0.7f;
					ty = y - dy * 0.7f;
					i = (int) tx;
					j = (int) ty;
					tx -= i;
					ty -= j;
					if (i >= 2 && i < XRES / CELL - 3 && j >= 2
							&& j < YRES / CELL - 3) {
						dx *= 1.0f - AIR_VADV;
						dy *= 1.0f - AIR_VADV;

						dx += AIR_VADV * (1.0f - tx) * (1.0f - ty) * vx[j][i];
						dy += AIR_VADV * (1.0f - tx) * (1.0f - ty) * vy[j][i];

						dx += AIR_VADV * tx * (1.0f - ty) * vx[j][i + 1];
						dy += AIR_VADV * tx * (1.0f - ty) * vy[j][i + 1];

						dx += AIR_VADV * (1.0f - tx) * ty * vx[j + 1][i];
						dy += AIR_VADV * (1.0f - tx) * ty * vy[j + 1][i];

						dx += AIR_VADV * tx * ty * vx[j + 1][i + 1];
						dy += AIR_VADV * tx * ty * vy[j + 1][i + 1];
					}

					if (WallData.bmap[y][x] instanceof WallFan) {
						dx += fvx[y][x];
						dy += fvy[y][x];
					}
					// pressure/velocity caps
					if (dp > 256.0f)
						dp = 256.0f;
					if (dp < -256.0f)
						dp = -256.0f;
					if (dx > 256.0f)
						dx = 256.0f;
					if (dx < -256.0f)
						dx = -256.0f;
					if (dy > 256.0f)
						dy = 256.0f;
					if (dy < -256.0f)
						dy = -256.0f;

					switch (airMode) {
					default:
					case 0: // Default
						break;
					case 1: // 0 Pressure
						dp = 0.0f;
						break;
					case 2: // 0 Velocity
						dx = 0.0f;
						dy = 0.0f;
						break;
					case 3: // 0 Air
						dx = 0.0f;
						dy = 0.0f;
						dp = 0.0f;
						break;
					case 4: // No Update
						break;
					}

					ovx[y][x] = dx;
					ovy[y][x] = dy;
					opv[y][x] = dp;
				}
			vx = ovx;
			vy = ovy;
			pv = opv;
			// memcpy(vx, ovx, sizeof(vx));
			// memcpy(vy, ovy, sizeof(vy));
			// memcpy(pv, opv, sizeof(pv));
		}
	}

	public void drawAirXY(int x, int y) {
		int pixr = 0, pixg = 0, pixb = 0;
		if (displayAir == 1) {
			if (pv[y][x] > 0.0f) {
				pixr = Rendering.clamp_flt(pv[y][x], 0.0f, 8.0f);
				pixg = 0;
				pixb = 0; // positive pressure is red!
			} else {
				pixr = 0;
				pixg = 0;
				pixb = Rendering.clamp_flt(-pv[y][x], 0.0f, 8.0f); // positive
																	// pressure
																	// is
																	// red!
			}
		} else if (displayAir == 2) {
			pixr = Rendering.clamp_flt(Math.abs(vx[y][x]), 0.0f, 8.0f);// vx
																		// adds
																		// red
			pixg = Rendering.clamp_flt(pv[y][x], 0.0f, 8.0f);// pressure adds
																// green
			pixb = Rendering.clamp_flt(Math.abs(vy[y][x]), 0.0f, 8.0f);// vy
																		// adds
																		// blue
		} else if (displayAir == 3) {
			int r;
			int g;
			int b;
			// velocity adds grey
			r = Rendering.clamp_flt(Math.abs(vx[y][x]), 0.0f, 24.0f)
					+ Rendering.clamp_flt(Math.abs(vy[y][x]), 0.0f, 20.0f);
			g = Rendering.clamp_flt(Math.abs(vx[y][x]), 0.0f, 20.0f)
					+ Rendering.clamp_flt(Math.abs(vy[y][x]), 0.0f, 24.0f);
			b = Rendering.clamp_flt(Math.abs(vx[y][x]), 0.0f, 24.0f)
					+ Rendering.clamp_flt(Math.abs(vy[y][x]), 0.0f, 20.0f);
			if (pv[y][x] > 0.0f) {
				r += Rendering.clamp_flt(pv[y][x], 0.0f, 16.0f);// pressure
																// adds red!
				if (r > 255)
					r = 255;
				if (g > 255)
					g = 255;
				if (b > 255)
					b = 255;
				pixr = r;
				pixg = g;
				pixb = b;
			} else {
				b += Rendering.clamp_flt(-pv[y][x], 0.0f, 16.0f);// pressure
																	// adds
																	// blue!
				if (r > 255)
					r = 255;
				if (g > 255)
					g = 255;
				if (b > 255)
					b = 255;
				pixr = r;
				pixg = g;
				pixb = b;
			}
		} else if (displayAir == 4) {
			if (hv[y][x] > 0.0f) {
				// c = RenderUtils.PIXRGB(RenderUtils.clamp_flt(hv[y][x], 0.0f,
				// 8.0f), 0, 0);//positive pressure is red!
			} else {
				// c = RenderUtils.PIXRGB(0, 0, RenderUtils.clamp_flt(-hv[y][x],
				// 0.0f, 8.0f));//negative pressure is blue!
			}
		}
		if (pixr == 0 && pixg == 0 && pixb == 0)
			return;
		Rendering.drawRect(x * CELL, y * CELL, (x * CELL) + CELL, (y * CELL)
				+ CELL, (float) pixr / 255, (float) pixg / 255,
				(float) pixb / 255);
	}

	public void drawAir() {
		if (displayAir == 0)
			return;
		for (int y = 0; y < YRES / CELL; y++)
			for (int x = 0; x < XRES / CELL; x++) {
				drawAirXY(x, y);
			}
	}
}
