package net.psj.Interface;

import java.util.ArrayList;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Simulation.ParticleData;
import net.psj.Simulation.Placable;
import net.psj.Simulation.WallData;

public class MenuData {

	public static int selected = 0;
	static int menus = 2;

	public static Menu MENU_WALLS = new Menu("W", 1);
	public static Menu MENU_PARTS = new Menu("P", 0);

	public static Menu getMenuFromID(int id) {
		if (id == 1)
			return MENU_WALLS;
		if (id == 0)
			return MENU_PARTS;
		else
			return null;
	}

	public static void draw() {
		// RenderUtils.drawRect(0, h+m, w, h, 1.0f,1.0f,1.0f);
		for (int i = 0; i < menus; i++)// draw all the menu sections
		{
			draw_menu(i);
		}
		draw_items();
	}

	static void draw_items() {
		// int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		ArrayList<Placable> menuItems = new ArrayList<Placable>();
		for (int i = PowderSimJ.wallStart; i < PowderSimJ.wallStart
				+ WallData.WL_NUM; i++) {
			if (WallData.newWallfromID(i).menu.id == selected) {
				menuItems.add(WallData.newWallfromID(i));
			}
		}
		for (int i = 0; i < ParticleData.PT_NUM; i++) {
			if (ParticleData.newPartFromID(i).menu.id == selected) {
				menuItems.add(ParticleData.newPartFromID(i));
			}
		}

		for (int i = 0; i < menuItems.size(); i++) {
			float c = menuItems.get(i).colour[0] * menuItems.get(i).colour[1]
					* menuItems.get(i).colour[1];
			if (c < 0.5f) {
				c = 1.0f;
			} else {
				c = 0f;
			}
			RenderUtils.drawRect((i * 64) + 5, h + 6, (i * 64) + 63,
					(h + m) - 6, menuItems.get(i).colour[0],
					menuItems.get(i).colour[1], menuItems.get(i).colour[2]);
			RenderUtils.drawFont((i * 64) + 23, h + (m / 2) - 5,
					menuItems.get(i).name, c, c, c);
		}
	}

	public static void click(int button, int x, int y) {
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		// int b = PowderSimJ.barSize;
		if (y > h && x < w) {
			ArrayList<Placable> menuItems = new ArrayList<Placable>();
			for (int i = PowderSimJ.wallStart; i < PowderSimJ.wallStart
					+ WallData.WL_NUM; i++) {
				if (WallData.newWallfromID(i).menu.id == selected) {
					menuItems.add(WallData.newWallfromID(i));
				}
			}
			for (int i = 0; i < ParticleData.PT_NUM; i++) {
				if (ParticleData.newPartFromID(i).menu.id == selected) {
					menuItems.add(ParticleData.newPartFromID(i).setId(i));
				}
			}
			for (int i = 0; i < menuItems.size(); i++) {
				if (x > (i * 64) + 5 && x < (i * 64) + 63)
					if (y > h + 6 && y < (h + m) - 6) {
						if (button == 0)
							PowderSimJ.selectedl = menuItems.get(i).id;
						else if (button == 1)
							PowderSimJ.selectedr = menuItems.get(i).id;
					}
			}
		} else {
			for (int i = 0; i < menus; i++) {
				if (x > (PowderSimJ.width + PowderSimJ.barSize) - 16
						&& x < (PowderSimJ.width + PowderSimJ.barSize) - 2
						&& y > (i * 16) + PowderSimJ.height
								+ PowderSimJ.menuSize - 16 - (menus * 16)
						&& y < ((i * 16) + PowderSimJ.height
								+ PowderSimJ.menuSize - 16 - (menus * 16)) + 14) {
					selected = i;
				}
			}

		}
	}

	static void draw_menu(int i) {
		RenderUtils
				.drawRectLine((PowderSimJ.width + PowderSimJ.barSize) - 16,
						(i * 16) + PowderSimJ.height + PowderSimJ.menuSize - 16
								- (menus * 16),
						(PowderSimJ.width + PowderSimJ.barSize) - 2,
						((i * 16) + PowderSimJ.height + PowderSimJ.menuSize
								- 16 - (menus * 16)) + 14, 255, 255, 255);
		if (selected == i)// selected menu
		{
			RenderUtils
					.drawRect((PowderSimJ.width + PowderSimJ.barSize) - 16,
							(i * 16) + PowderSimJ.height + PowderSimJ.menuSize
									- 16 - (menus * 16),
							(PowderSimJ.width + PowderSimJ.barSize) - 2,
							((i * 16) + PowderSimJ.height + PowderSimJ.menuSize
									- 16 - (menus * 16)) + 14, 255, 255, 255);
			RenderUtils.drawFont((PowderSimJ.width + PowderSimJ.barSize) - 13,
					(i * 16) + PowderSimJ.height + PowderSimJ.menuSize - 14
							- (menus * 16), getMenuFromID(i).name, 0, 0, 0);
		} else // unselected menu
		{
			RenderUtils.drawFont((PowderSimJ.width + PowderSimJ.barSize) - 13,
					(i * 16) + PowderSimJ.height + PowderSimJ.menuSize - 14
							- (menus * 16), getMenuFromID(i).name, 255, 255,
					255);
		}
	}
}
