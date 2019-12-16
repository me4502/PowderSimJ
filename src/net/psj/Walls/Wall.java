package net.psj.Walls;

import net.psj.PowderSimJ;
import net.psj.Interface.Menu;
import net.psj.Simulation.Placable;

import com.me4502.MAPL.MAPL;

public class Wall extends Placable {

	protected int CELL = PowderSimJ.cell;

	public Wall(String name, float[] colour, short id, Menu menu) {
		super(name, colour, (short) (id + PowderSimJ.wallStart), menu);
	}

	public void render(int x, int y) {
		MAPL.inst().getRenderer().rectangles().drawSingleRectangle(x * CELL, y * CELL, x * CELL + CELL, y * CELL + CELL, true, colour[0], colour[1], colour[2], 1.0f);
	}

	public String getExtraData() {
		return "";
	}
}