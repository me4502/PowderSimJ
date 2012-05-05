package net.psj.Interface;

import net.psj.PowderSimJ;
import net.psj.RenderUtils;

public class Menu {

	public static int selected = 0;
	static int menus = 2;
	
	public static void draw()
	{
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		RenderUtils.drawRect(0, h+m, w, h, 1.0f,1.0f,1.0f);
        for (int i=0; i<menus; i++)//draw all the menu sections
        {
            draw_menu(i);
        }
	}
	
	static void draw_menu(int i)
	{
		RenderUtils.drawRectLine((PowderSimJ.width+PowderSimJ.barSize)-16, (i*16)+PowderSimJ.height+PowderSimJ.menuSize-16-(menus*16), (PowderSimJ.width+PowderSimJ.barSize)-2, ((i*16)+PowderSimJ.height+PowderSimJ.menuSize-16-(menus*16))+14, 255, 255, 255);
		if (selected==i)//selected menu
		{
			RenderUtils.drawRect((PowderSimJ.width+PowderSimJ.barSize)-16, (i*16)+PowderSimJ.height+PowderSimJ.menuSize-16-(menus*16), (PowderSimJ.width+PowderSimJ.barSize)-2, ((i*16)+PowderSimJ.height+PowderSimJ.menuSize-16-(menus*16))+14, 255, 255, 255);
			RenderUtils.drawFont((PowderSimJ.width+PowderSimJ.barSize)-13, (i*16)+PowderSimJ.height+PowderSimJ.menuSize-14-(menus*16), "C", 0, 0, 0);
		}
		else //unselected menu
		{
			RenderUtils.drawFont((PowderSimJ.width+PowderSimJ.barSize)-13, (i*16)+PowderSimJ.height+PowderSimJ.menuSize-14-(menus*16), "C", 255, 255, 255);
		}
	}
}
