package net.psj;

public class Utils {

	public static float restrict_flt(float f, float min, float max)
	{
		if (f<min)
			return min;
		if (f>max)
			return max;
		return f;
	}
}
