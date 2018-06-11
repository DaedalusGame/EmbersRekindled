#version 120

varying vec3 normal;
varying vec4 color;
varying float shift;
varying vec3 position;
varying float intens;
varying vec4 lcolor;
varying vec4 uv;

uniform int time;
uniform int chunkX;
uniform int chunkZ;
uniform sampler2D sampler;
uniform sampler2D lightmap;
uniform vec3 playerPos;

void main()
{
	vec3 lmapDarkness = texture2D(lightmap,gl_TexCoord[1].st*vec2(1/256.0f,1/256.0f)).xyz;
	vec4 baseColor = gl_Color * texture2D(sampler,gl_TexCoord[0].st);
	vec4 color = vec4((baseColor.xyz * (lcolor.xyz * 1.0f)),baseColor.w);
	color.x = min(1.0f,color.x);
	color.y = min(1.0f,color.y);
	color.z = min(1.0f,color.z);
	color.w = min(1.0f,color.w);
	color = (1.0f-intens)*baseColor*vec4(lmapDarkness,1) + intens*color;
	gl_FragColor = vec4(color.xyz * (intens + (1.0f-intens)*lmapDarkness),color.w);
}