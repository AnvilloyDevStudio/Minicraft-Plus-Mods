package minicraft.mods;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LoaderUtils {
	/**
	 * Reading the string from the input stream.
	 * @param in The input stream to be read.
	 * @return The returned string.
	 */
	public static String readStringFromInputStream(InputStream in) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		return String.join("\n", reader.lines().toArray(String[]::new));
	}
}
