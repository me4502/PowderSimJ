package net.psj.Interface;

import java.util.ArrayList;

import net.psj.ParticleData;
import net.psj.PowderSimJ;
import net.psj.RenderUtils;
import net.psj.Particles.Particle;

public class Menu {

	public static int selected = 0;
	static int menus = 2;
	
	public static void draw()
	{
		//RenderUtils.drawRect(0, h+m, w, h, 1.0f,1.0f,1.0f);
        for (int i=0; i<menus; i++)//draw all the menu sections
        {
            draw_menu(i);
        }
        draw_items();
	}
	
	static void draw_items()
	{
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		ArrayList<Particle> menuItems = new ArrayList<Particle>();
		for(int i = 1; i < ParticleData.PT_NUM; i++)
		{
			if(ParticleData.newPartFromID(i).menu == selected)
			{
				menuItems.add(ParticleData.newPartFromID(i));
			}
		}
		for(int i = 0; i < menuItems.size(); i++)
		{
			RenderUtils.drawRect((i*64)+5, h+6, (i*64)+63, (h+m)-6, menuItems.get(i).colour[0], menuItems.get(i).colour[1], menuItems.get(i).colour[2]);
			RenderUtils.drawFont((i*64)+23, h+(m/2)-5, menuItems.get(i).name, menuItems.get(i).colour[2], menuItems.get(i).colour[1], menuItems.get(i).colour[0]);
		}
	}
	
	public static void click(int button, int x, int y)
	{
		int w = PowderSimJ.width;
		int h = PowderSimJ.height;
		int m = PowderSimJ.menuSize;
		ArrayList<Particle> menuItems = new ArrayList<Particle>();
		for(int i = 1; i < ParticleData.PT_NUM; i++)
		{
			if(ParticleData.newPartFromID(i).menu == selected)
			{
				menuItems.add(ParticleData.newPartFromID(i).setPos(x, y, i));
			}
		}
		for(int i = 0; i < menuItems.size(); i++)
		{
			if(x>(i*64)+5 && x < (i*64)+63)
				if(y>h+6 && y < (h+m)-6)
				{
					if(button==0) PowderSimJ.selectedl = menuItems.get(i).id;
					else if(button==4) PowderSimJ.selectedr = menuItems.get(i).id;
				}
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
