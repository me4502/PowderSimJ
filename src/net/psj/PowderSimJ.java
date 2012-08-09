package net.psj;

import net.Company.CompanyGame;
import net.Company.DebugProfiler;
import net.Company.Engine;
import net.Company.Rendering;
import net.psj.Interface.MenuData;
import net.psj.Interface.Overlay;
import net.psj.Simulation.Air;
import net.psj.Simulation.ParticleData;
import net.psj.Simulation.ShaderData;
import net.psj.Simulation.WallData;
import net.psj.Walls.WallFan;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class PowderSimJ extends CompanyGame {

	static Engine engine;

	public static final int width = 612;
	public static final int height = 384;
	public static final int menuSize = 40;
	public static final int barSize = 17;
	public static final int cell = 4;

	public static final int MAX_TEMP = 9000;
	public static final int MIN_TEMP = 0;

	public static int selectedl = 1;/* 0x00 - 0xFF are particles */
	public static int selectedr = 0;

	public static int wallStart = 4096; // Basically the element limit. Can be
	// changed, but once saving is done, it
	// will break saves.

	int fanX, fanY;

	boolean isSettingFan = false;

	public boolean isPaused = false;

	public boolean airHeat = false;

	public static GameContainer gc;

	public Air air = new Air();
	public WallData wall = new WallData();
	public static ParticleData ptypes = new ParticleData();

	public static int brushSize = 10;

	public static final String version = "0.1 Test";

	boolean shouldUpdateAir = true;

	// Debug Data
	private DebugProfiler updateProfiler = new DebugProfiler(5, 20, "Tick");
	private DebugProfiler renderProfiler = new DebugProfiler(5, 80, "Render");

	public PowderSimJ(Engine engine) {
		super(engine);
	}

	public PowderSimJ() {
		super();
	}

	public static GameContainer getGame() {
		return gc;
	}

	@Override
	public void init(GameContainer gc) {
		try {
			gc.setIcon(ResourceLoader.loadResource("powder.png"));
			gc.setAlwaysRender(true);
			PowderSimJ.gc = gc;
			Rendering.setAntiAliasing(true);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			Rendering.init();
			ShaderData.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SlickException {
		// Initializes the engine.
		engine = new Engine("PowderSimJ");
		Engine.setup("PowderSimJ", new PowderSimJ(engine), width + barSize,
				height + menuSize);
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
			Rendering.drawLine(fanX, fanY, Engine.mouseX, Engine.mouseY, 1,
					1.0f, 1.0f, 1.0f);
		else {
			int x1 = Engine.mouseX, y1 = Engine.mouseY;
			x1 = x1 - PowderSimJ.brushSize / 2;
			y1 = y1 - PowderSimJ.brushSize / 2;
			Rendering.drawRectLine(x1, y1, x1 + brushSize, y1 + brushSize,
					1.0f, 1.0f, 1.0f);
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
			while (!(Engine.mouseY % cell == 0))
				Engine.mouseY--;
			while (!(Engine.mouseX % cell == 0))
				Engine.mouseX--;
			if (WallData.bmap[Engine.mouseY / cell][Engine.mouseX / cell] instanceof WallFan
					&& gc.getInput().isKeyDown(Input.KEY_LSHIFT)) {
				isSettingFan = !isSettingFan;
				fanX = Engine.mouseX;
				fanY = Engine.mouseY;
				return;
			} else if (isSettingFan) {
				float nfvx = (Engine.mouseX - fanX) * 0.055f;
				float nfvy = (Engine.mouseY - fanY) * 0.055f;
				air.fvx[fanY / cell][fanX / cell] = nfvx;
				air.fvy[fanY / cell][fanX / cell] = nfvy;
				isSettingFan = false;
				return;
			}
		}
	}

	public static boolean isInPlayField(int x, int y) {
		if (Engine.mouseY > 0 && Engine.mouseY < PowderSimJ.height)
			if (Engine.mouseX > 0 && Engine.mouseX < PowderSimJ.width)
				return true;

		return false;
	}

	public void onMouseClick(GameContainer arg0, int button) {
		if (isInPlayField(Engine.mouseX, Engine.mouseY)) {
			if (button == 0) {
				if (selectedl < wallStart)
					ptypes.create_parts(Engine.mouseX, Engine.mouseY, selectedl);
				else
					wall.create_walls(Engine.mouseX / 4, Engine.mouseY / 4,
							selectedl);
			} else if (button == 4)
				ptypes.create_parts(Engine.mouseX, Engine.mouseY, selectedr);
		}
	}

	@Override
	public void configLoad(String[] args) {

	}

	@Override
	public String[] configSave() {
		return null;
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}
}