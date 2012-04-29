package net.psj;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class PowderSimJ extends BasicGame{
	
	public PowderSimJ()
    {
        super("PowderSimJ");
    }
 
    @Override
    public void init(GameContainer gc) throws SlickException {
 
    }
 
    public static void main(String[] args) 
			throws SlickException
    {
         AppGameContainer app = new AppGameContainer(new PowderSimJ());
         app.setDisplayMode(800, 600, false);
         app.start();
    }

	@Override
	public void render(GameContainer arg0, Graphics arg1) throws SlickException {
		
	}

	@Override
	public void update(GameContainer arg0, int arg1) throws SlickException {
		
	}
}
