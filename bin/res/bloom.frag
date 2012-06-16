uniform sampler2D tex0;

void main(void)
{
	vec4 blurSample = 0.0;
	vec4 tmpPix;
	vec4 offPix;
	for(int tx =-2;tx<3;tx++)
        {
		for(int ty =-2;ty<3;ty++)
		{
			vec2 uv = gl_TexCoord[0].st;
			uv.x = uv.x + tx*0.01;
			uv.y = uv.y + ty*0.01;
			tmpPix = texture2D(tex0,uv);
			offPix = -0.3+tmpPix;
			offPix = offPix *32;
			blurSample = blurSample + offPix;
			
		}	
        }
	blurSample = blurSample / 256;
	gl_FragColor = texture2D(tex0,gl_TexCoord[0].st)*2+blurSample*1.2;
}