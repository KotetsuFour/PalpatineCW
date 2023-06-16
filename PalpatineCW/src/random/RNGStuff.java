package random;

import java.util.Random;

public class RNGStuff {

	public static final Random rng = new Random();
	
	public static int randomNumber0To99() {
		return rng.nextInt(100);
	}
	
	public static String randomNameGenerator() {
		int firstSyls = rng.nextInt(3) + 1;
		int lastSyls = rng.nextInt(2) + 1;
		StringBuilder first = new StringBuilder();
		StringBuilder last = new StringBuilder();
		boolean addedConsonant = true;
		for (int q = 0; q < firstSyls; q++) {
			String c = consonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			first.append(c);
			first.append(vowel());
			c = consonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			first.append(c);
		}
		first.append(" ");
		for (int q = 0; q < lastSyls; q++) {
			String c = consonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			last.append(c);
			last.append(vowel());
			c = consonant(addedConsonant);
			if (c.contentEquals("")) {
				addedConsonant = false;
			} else {
				addedConsonant = true;
			}
			last.append(c);
		}
		first.setCharAt(0, Character.toUpperCase(first.charAt(0)));
		last.setCharAt(0, Character.toUpperCase(last.charAt(0)));
		return first.toString() + last.toString();
	}
	
	private static String consonant(boolean allowedToSkip) {
		if (!allowedToSkip || rng.nextBoolean()) {
			String[] cs = {"b", "c", "ch", "d", "f", "g", "h", "j", "k", "l", "ll", "m", "n", "p",
					"q", "r", "rr", "s", "sh", "t", "th", "v", "w", "wh", "x", "y", "z"};
			return cs[rng.nextInt(cs.length)];
		}
		return "";
	}
	
	private static String vowel() {
		String[] vs = {"a", "e", "i", "o", "u", "ai", "ao", "au", "ea", "ee", "ei", "ia", "ie",
				"io", "oa", "oi", "oo", "ou", "ua", "ue", "ui", "uo"};
		return vs[rng.nextInt(vs.length)];
	}
	
	public int nextInt(int bound) {
		return rng.nextInt(bound);
	}
	
	public static void main(String[] args) {
		for (int q = 0; q < 10; q++) {
			System.out.println(randomNameGenerator());
		}
	}
}
