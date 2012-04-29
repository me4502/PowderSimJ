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
		Input input = arg0.getInput();
		onKeypress(input);
		air.update_air();
	}
	
	public void onKeypress(Input keys)
	{
		
	}
}
