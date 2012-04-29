package net.psj;

import net.psj.Simulation.Air;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class PowderSimJ extends BasicGame{
	
	public static final int width = 800;
	public static final int height = 600;
	public static final int cell = 4;
	
	int mouseX;
	int mouseY;
	
	public Air air = new Air();
	
	public PowderSimJ()
    {
        super("PowderSimJ");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
    }
 
    public static void main(String[] args) throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new PowderSimJ());
         app.setDisplayMode(width, height, false);
         app.start();
    }

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		air.drawAir();
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		
		air.update_air();
		air.make_kernel();
		air.update_airh();
		
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
		
	}
	
	public void onMouseClick()
	{
		air.pv[mouseY/cell][mouseX/cell] -= 1.0f;
	}
	
	public void onMouseRightClick()
	{
		air.pv[mouseY/cell][mouseX/cell] += 1.0f;
	}
}
