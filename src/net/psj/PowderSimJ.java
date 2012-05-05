package net.psj;

import net.psj.Interface.Menu;
import net.psj.Simulation.Air;
import net.psj.Simulation.Walls;
import net.psj.Walls.*;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;


public class PowderSimJ extends BasicGame implements MouseListener,KeyListener{
	
	public static final int width = 612;
	public static final int height = 384;
	public static final int menuSize = 40;
	public static final int barSize = 17;
	public static final int cell = 4;
	
	public static final int MAX_TEMP = 9000;
	public static final int MIN_TEMP = 0;
	
	static int AA = 0;
	
	int mouseX;
	int mouseY;
	
	public static int selectedl = 1;/*0x00 - 0xFF are particles */
	public static int selectedr = 0;
	
	int fanX,fanY;
	
	boolean isSettingFan = false;
		
	int keyTick = 5;
		
	public boolean isPaused = false;
	
	public boolean airHeat = false;
	
	public Air air = new Air();
	public Walls wall = new Walls();
	public static ParticleData ptypes = new ParticleData();
	
	public static int brushSize = 10;
	
	public PowderSimJ()
    {
        super("Cabbage Game");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
    	RenderUtils.setAntiAliasing(true);
    	GL11.glDisable(GL11.GL_LIGHTING);
    	GL11.glEnable(GL11.GL_SMOOTH);
    	GL11.glEnable(GL11.GL_LINE_SMOOTH);
    	RenderUtils.init();
    }
 
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new PowderSimJ());
         app.setDisplayMode(width+barSize, height+menuSize, false);
         app.setVSync(true);
         app.setMultiSample(AA);
         app.start();
    }

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		if(!isSettingFan)
			air.drawAir();
		wall.renderWalls();
		ptypes.render();
		
		Menu.draw();
		
		if(isSettingFan)
			RenderUtils.drawLine(fanX,fanY,mouseX,mouseY, 1,255,255,255);
		else
		{
			int x1 = mouseX, y1 = mouseY;
			x1 = x1-(PowderSimJ.brushSize/2);
			y1 = y1-(PowderSimJ.brushSize/2);
			RenderUtils.drawRectLine(x1, y1, x1+brushSize, y1+brushSize, 255, 255, 255);
		}
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException 
	{
		if(!isPaused)
		{
			air.update_air();
			air.make_kernel();
			if(airHeat)
				air.update_airh();
		}
		
		ptypes.update();
		
		Input input = arg0.getInput();
		mouseX = input.getMouseX();
		mouseY = input.getMouseY();
		input.setMouseClickTolerance(0);
		input.setDoubleClickInterval(0);
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			onMouseClick(arg0,0);
		if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			onMouseClick(arg0,4);
	}
	
	@Override
	public void keyPressed(int key, char c)
	{
		if(key==Input.KEY_EQUALS)
			for (int y=0; y<height/cell; y++)
					for (int x=0; x<width/cell; x++)
					{
						air.pv[y][x] = 0f;
						Air.vy[y][x] = 0f;
						Air.vx[y][x] = 0f;
						air.hv[y][x] = 0f;
					}
		if(key==Input.KEY_SPACE)
			isPaused = !isPaused;
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		brushSize += change/100;
		if(brushSize<1) brushSize = 1;
	}
	
	public void onMouseClick(GameContainer arg0, int button)
	{
		if(mouseY>0 && mouseY<height)
		{
			if(mouseX>0 && mouseX<width)
			{
				if(button==0)
					ptypes.create_parts(mouseX, mouseY, selectedl);
				else if(button==4)
					ptypes.create_parts(mouseX, mouseY, selectedr);
				while(!(mouseY%cell==0))
					mouseY--;
				while(!(mouseX%cell==0))
					mouseX--;
				if(Walls.bmap[mouseY/cell][mouseX/cell] instanceof WallFan && arg0.getInput().isKeyDown(Input.KEY_LSHIFT))
				{
					isSettingFan = !isSettingFan;
					fanX = mouseX;
					fanY = mouseY;
					return;
				}
				else if(isSettingFan)
				{
					float nfvx = (mouseX-fanX)*0.055f;
					float nfvy = (mouseY-fanY)*0.055f;
					air.fvx[fanY/cell][fanX/cell] = nfvx;
					air.fvy[fanY/cell][fanX/cell] = nfvy;
					isSettingFan = false;
					return;
				}
			}
		}
	}
}