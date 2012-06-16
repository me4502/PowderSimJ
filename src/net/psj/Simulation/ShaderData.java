package net.psj.Simulation;

import net.psj.ResourceLoader;
import net.psj.shader.Shader;

public class ShaderData {

	public static Shader blurH, blurV, fancy, FXAA, fire;

	public static void init() {
		try {
			blurH = new Shader(ResourceLoader.loadResource("simplevertex.vert"),
					ResourceLoader.loadResource("blurHorizontal.frg"));
			blurV = new Shader(ResourceLoader.loadResource("simplevertex.vert"), ResourceLoader.loadResource("blurVertical.frg"));
			fancy = new Shader(ResourceLoader.loadResource("shock.vert"), ResourceLoader.loadResource("shock.frag"));
			FXAA = new Shader(ResourceLoader.loadResource("FXAA.vert"), ResourceLoader.loadResource("FXAA.frag"));
			fire = new Shader(ResourceLoader.loadResource("fireVertex.vert"), ResourceLoader.loadResource("fireFrag.frg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
