package net.psj;

import net.psj.Interface.MenuData;
import net.psj.Interface.Overlay;
import net.psj.Simulation.Air;
import net.psj.Simulation.ParticleData;
import net.psj.Simulation.ShaderData;
import net.psj.Simulation.WallData;
import net.psj.Walls.WallFan;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.me4502.MAPL.DebugProfiler;
import com.me4502.MAPL.MAPL;
import com.me4502.MAPL.slick.MAPLSlickProgram;
import com.me4502.MAPL.slick.SlickMAPL;
import com.me4502.MAPL.util.config.YAMLConfiguration;

public class PowderSimJ implements MAPLSlickProgram {

	public static final int width = 612;
	public static final int height = 384;
	public static final int menuSize = 60;
	public static final int barSize = 17;
	public static final int cell = 4;

	public static final int MAX_TEMP = 9000;
	public static final int MIN_TEMP = 0;

	public static short selectedl = 1;/* 0x00 - 0xFF are particles */
	public static short selectedr = 0;

	public static short wallStart = 4096; // Basically the element limit. Can be
	// changed, but once saving is done, it
	// will break saves.

	int fanX, fanY;

	boolean isSettingFan = false;

	public boolean isPaused = false;

	public boolean airHeat = false;

	public Air air = new Air();
	public WallData wall = new WallData();
	public static ParticleData ptypes = new ParticleData();

	public static int brushSize = 10;

	public static final String version = "0.1 Test";

	boolean shouldUpdateAir = true;

	// Debug Data
	private DebugProfiler updateProfiler = new DebugProfiler(5, 20, "Tick") {

		@Override
		public void drawTimes() {

		}
	};
	private DebugProfiler renderProfiler = new DebugProfiler(5, 80, "Render") {

		@Override
		public void drawTimes() {

		}
	};

	public PowderSimJ() {
		super();
	}

	@Override
	public void init(GameContainer gc) {
		try {
			//gc.setIcon(ResourceLoader.loadResource("res", "powder.png"));
			gc.setAlwaysRender(true);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			ShaderData.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SlickException {

		new SlickMAPL().setup(new PowderSimJ(), "PowderSimJ", width + barSize, height + menuSize, false);
	}

	@Override
	public void render(GameContainer arg0, Graphics arg1) {
		renderProfiler.reset();
		renderProfiler.start();
		if (!isSettingFan) {
			renderProfiler.startSection("Air");
			air.drawAir();
			renderProfiler.endSection();
		}
		renderProfiler.startSection("Walls");
		wall.renderWalls();

		renderProfiler.startEndSection("Parts");
		ptypes.render();

		renderProfiler.startEndSection("Menu");
		MenuData.draw();

		renderProfiler.startEndSection("Cursor");
		if (isSettingFan)
			MAPL.inst().getRenderer().lines().drawSingleLine(fanX, fanY, ((SlickMAPL)MAPL.inst()).mouseX, ((SlickMAPL)MAPL.inst()).mouseY, 1, 1.0f, 1.0f, 1.0f, 1.0f);
		else {
			int x1 = ((SlickMAPL)MAPL.inst()).mouseX, y1 = ((SlickMAPL)MAPL.inst()).mouseY;
			x1 = x1 - PowderSimJ.brushSize / 2;
			y1 = y1 - PowderSimJ.brushSize / 2;
			MAPL.inst().getRenderer().lines().drawSingleLine(x1, y1, x1 + brushSize, y1 + brushSize, 1, 1.0f, 1.0f, 1.0f, 1.0f);
		}

		renderProfiler.startSection("Info");
		Overlay.drawInfoBar();
		Overlay.drawPixInfo();
		renderProfiler.endSection();
		renderProfiler.stop();
		renderProfiler.drawTimes();
		updateProfiler.drawTimes();
	}

	@Override
	public void update(GameContainer arg0, int arg1) {
		try {
		}
		catch(Exception e){
			e.printStackTrace();
		}
		updateProfiler.reset();
		updateProfiler.start();
		if (!isPaused) {
			if (shouldUpdateAir) {
				shouldUpdateAir = false;
				updateProfiler.startSection("Air");
				air.update_air();
				air.make_kernel();
				if (airHeat)
					air.update_airh();
				updateProfiler.endSection();
			} else
				shouldUpdateAir = true;

			updateProfiler.startSection("Parts");
			ptypes.update();
			updateProfiler.endSection();
		}

		Input input = arg0.getInput();
		if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON))
			onMouseClick(arg0, 0);
		if (input.isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON))
			onMouseClick(arg0, 4);
		updateProfiler.stop();
	}

	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_EQUALS)
			for (int y = 0; y < PowderSimJ.height / cell; y++)
				for (int x = 0; x < PowderSimJ.width / cell; x++) {
					Air.pv[y][x] = 0f;
					Air.vy[y][x] = 0f;
					Air.vx[y][x] = 0f;
					air.hv[y][x] = 0f;
				}
		if (key == Input.KEY_SPACE)
			isPaused = !isPaused;

		if (key == Input.KEY_LBRACKET)
			brushSize -= 20;

		if (key == Input.KEY_RBRACKET)
			brushSize += 20;

		if (brushSize < 1)
			brushSize = 1;
	}

	@Override
	public void mouseWheelMoved(int change) {
		brushSize += change / 100;
		if (brushSize < 1)
			brushSize = 1;
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		// TODO make more brush types;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		MenuData.click(button, x, y);
		if (isInPlayField(x, y)) {
			while (!(y % cell == 0))
				y--;
			while (!(x % cell == 0))
				x--;
			if (WallData.bmap[y / cell][x / cell] instanceof WallFan && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				isSettingFan = !isSettingFan;
				fanX = x;
				fanY = y;
				return;
			} else if (isSettingFan) {
				float nfvx = (x - fanX) * 0.055f;
				float nfvy = (y - fanY) * 0.055f;
				air.fvx[fanY / cell][fanX / cell] = nfvx;
				air.fvy[fanY / cell][fanX / cell] = nfvy;
				isSettingFan = false;
				return;
			}
		}
	}

	public static boolean isInPlayField(int x, int y) {
		if (y > 0 && y < PowderSimJ.height)
			if (x > 0 && x < PowderSimJ.width)
				return true;

		return false;
	}

	public void onMouseClick(GameContainer arg0, int button) {
		if (isInPlayField(arg0.getInput().getMouseX(), arg0.getInput().getMouseY())) {
			if (button == 0) {
				if (selectedl < wallStart)
					ptypes.create_parts(((SlickMAPL)MAPL.inst()).mouseX, ((SlickMAPL)MAPL.inst()).mouseY, selectedl);
				else
					wall.create_walls(((SlickMAPL)MAPL.inst()).mouseX / 4, ((SlickMAPL)MAPL.inst()).mouseY / 4,
							selectedl);
			} else if (button == 4)
				ptypes.create_parts(((SlickMAPL)MAPL.inst()).mouseX, ((SlickMAPL)MAPL.inst()).mouseY, selectedr);
		}
	}

	@Override
	public String getProgramName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWindowWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getWindowHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWindowScaleX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getWindowScaleY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public YAMLConfiguration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean close() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int arg0, char arg1) {
		// TODO Auto-generated method stub

	}
}