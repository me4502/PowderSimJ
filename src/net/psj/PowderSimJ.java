package net.psj;

import net.psj.Simulation.Air;
import net.psj.Simulation.Walls;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import Walls.*;

public class PowderSimJ extends BasicGame{
	
	public static final int width = 400;
	public static final int height = 400;
	public static final int cell = 4;
	
	public static final int MAX_TEMP = 9000;
	public static final int MIN_TEMP = 0;
	
	int mouseX;
	int mouseY;
	
	int keyTick = 5;
	
	public boolean isPaused = false;
	
	public boolean airHeat = false;
	
	public Air air = new Air();
	public Walls wall = new Walls();
	
	public PowderSimJ()
    {
        super("Cabbage Game");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
    }
 
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new PowderSimJ());
         app.setDisplayMode(width, height, false);
         app.setVSync(true);
         app.start();
    }

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		air.drawAir();
		wall.renderWalls();
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
		
		Input input = arg0.getInput();
		onKeypress(input);
		mouseX = input.getMouseX();
		mouseY = input.getMouseY();
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			onMouseClick();
		if(input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			onMouseRightClick();
	}
	
	public void onKeypress(Input keys)
	{
		if(keyTick>0)
		{
			keyTick--;
			return;
		}
		keyTick = 2;
		if(keys.isKeyDown(Input.KEY_EQUALS))
			for (int y=0; y<height/cell; y++)
					for (int x=0; x<width/cell; x++)
					{
						air.pv[y][x] = 0f;
						air.vy[y][x] = 0f;
						air.vx[y][x] = 0f;
					}
		if(keys.isKeyDown(Input.KEY_SPACE))
			isPaused = !isPaused;
	}
	
	public void onMouseClick()
	{
		while(!(mouseY%cell==0))
			mouseY--;
		while(!(mouseX%cell==0))
			mouseX--;
		if(Walls.bmap[mouseY/cell][mouseX/cell] instanceof WallFan)
		{
			
		}
		air.pv[mouseY/cell][mouseX/cell] -= 500.0f;
	}
	
	public void onMouseRightClick()
	{
		while(!(mouseY%cell==0))
			mouseY--;
		while(!(mouseX%cell==0))
			mouseX--;
		Walls.bmap[mouseY/cell][mouseX/cell] = new WallFan();
		//air.pv[mouseY/cell][mouseX/cell] += 500.0f;
	}
}
