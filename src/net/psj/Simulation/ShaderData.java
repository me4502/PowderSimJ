package net.psj.Simulation;

import net.psj.shader.Shader;

public class ShaderData {

	public static Shader blurH,blurV,fancy;
	
	public static void init()
	{
		try{
			blurH = new Shader( "res/simplevertex.vert", "res/blurHorizontal.frg" );
			blurV = new Shader( "res/simplevertex.vert", "res/blurVertical.frg" );
			fancy = new Shader("res/simplevertex.vert","res/bloom.frag");
		}
		catch(Exception e){e.printStackTrace();}
	}
}
