package net.psj.Walls;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Interface.Menu;
import net.psj.Simulation.Placable;

public class Wall extends Placable {

	protected int CELL = PowderSimJ.cell;

	public Wall(String name, float[] colour, int id, Menu menu) {
		super(name, colour, id + PowderSimJ.wallStart, menu);
	}

	public void render(int x, int y) {
		RenderUtils.drawRect(x * CELL, y * CELL, (x * CELL) + CELL, (y * CELL)
				+ CELL, colour[0], colour[1], colour[2]);
	}

	public String getExtraData() {
		return "";
	}
}