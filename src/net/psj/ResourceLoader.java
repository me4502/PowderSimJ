package net.psj;

import java.io.File;

public class ResourceLoader {

	public static String loadResource(String resource)
	{
		File f = new File("res/" + resource);
		if(f.exists())
			return "res/" + resource;
		else
			return resource;
	}
	
}
