package org.day.smartfolders.util;

import java.util.Random;

public class RandomUtils {

	static final Random mRnd = new Random();
	
    public static int getRandom(int max) {
    	if(max <= 0) return 0;
        //Random mRnd = new Random();
        return mRnd.nextInt(max);

    }

    public static int getRandom(int min, int max) {
        //Random mRnd = new Random();
        int n = mRnd.nextInt(max-min);
        return n + min;
    }

}
