package minicraft.mods.discovery;

@SuppressWarnings("serial")
public class ModResolutionException extends Exception {
	public ModResolutionException(String s) {
		super(s);
	}

	public ModResolutionException(String format, Object... args) {
		super(String.format(format, args));
	}

	public ModResolutionException(String s, Throwable t) {
		super(s, t);
	}
}
