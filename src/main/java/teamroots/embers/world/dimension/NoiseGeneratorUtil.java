package teamroots.embers.world.dimension;

public class NoiseGeneratorUtil {
	public static float getNoise(int x, int y){
		x = 36969*(x & 65535) + (x >> 16);
		y = 18000*(y & 65535) + (y >> 16);
		return ((float)(x << 16) + y)/65536.0f;
	}
	
	public static float fastSin(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + .405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}
	
	public static float fastCos(float x){
	    if (x < -3.14159265){
	        x += 6.28318531;
	    }
	    else {
	        if (x >  3.14159265){
	            x -= 6.28318531;
	        }
	    }
	    x += 1.57079632;
	    if (x >  3.14159265){
	        x -= 6.28318531;
	    }

	    if (x < 0){
	        return (float) (1.27323954 * x + 0.405284735 * x * x);
	    }
	    else {
	        return (float) (1.27323954 * x - 0.405284735 * x * x);
	    }
	}

	public static float interpolate(float s, float e, float t){
	    float t2 = (1.0f-fastCos(t*3.14159265358979323f))/2.0f;
	    return(s*(1.0f-t2)+(e)*t2);
	}
	
	public static float bilinear(float ul, float ur, float dr, float dl, float t1, float t2){
		return interpolate(interpolate(ul,ur,t1),interpolate(dl,dr,t1),t2);
	}
	
	public static float getOctave(int x, int y, int dimen){
		return bilinear(getNoise((x/dimen)*dimen,(y/dimen)*dimen)
				,getNoise((x/dimen)*dimen+dimen,(y/dimen)*dimen)
				,getNoise((x/dimen)*dimen+dimen,(y/dimen)*dimen+dimen)
				,getNoise((x/dimen)*dimen,(y/dimen)*dimen+dimen),
				((float)(x-(x/dimen)*dimen))/((float)dimen),
				((float)(y-(y/dimen)*dimen))/((float)dimen));
	}
}
