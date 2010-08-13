package com.appsec.intellipass.util;

import java.util.Random;

/**
 * 
 * @author Chander Singh [chander (dot) singh (at) gmail (dot) com]
 * May 31, 2009
 */
public class IntellipassUtil {

	/**
	 * 
	 * @param minNbr
	 * @param maxNbr
	 * @return
	 */
	public static int getRandom (int minNbr, int maxNbr) {
		if (minNbr == maxNbr) {
			return maxNbr;
		}
		Random random = new Random ();
		int randomNbr = random.nextInt(maxNbr + 1);
		if (randomNbr < minNbr) {
			return getRandom(minNbr, maxNbr);
		}
		return randomNbr;
	}
}
