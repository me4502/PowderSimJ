package net.psj.Simulation;

import net.psj.shader.Shader;

public class ShaderData {

	public static Shader blurH, blurV, fancy, FXAA, fire;

	public static void init() {
		/*try {
			blurH = new Shader(
					ResourceLoader.loadResource("res","simplevertex.vert"),
					ResourceLoader.loadResource("res","blurHorizontal.frg"));
			blurV = new Shader(
					ResourceLoader.loadResource("res","simplevertex.vert"),
					ResourceLoader.loadResource("res","blurVertical.frg"));
			fancy = new Shader(ResourceLoader.loadResource("res","shock.vert"),
					ResourceLoader.loadResource("res","shock.frag"));
			FXAA = new Shader(ResourceLoader.loadResource("res","FXAA.vert"),
					ResourceLoader.loadResource("res","FXAA.frag"));
			fire = new Shader(ResourceLoader.loadResource("res","fireVertex.vert"),
					ResourceLoader.loadResource("res","fireFrag.frg"));
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
