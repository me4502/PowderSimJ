package net.psj.Simulation;

import net.psj.Interface.Menu;

public class Placable {

	public String name;
	public float[] colour;
	public short id;
	public Menu menu;

	public Placable(String name, float[] colour, short id, Menu menu) {
		this.name = name;
		this.colour = colour;
		this.id = id;
		this.menu = menu;
	}
}
