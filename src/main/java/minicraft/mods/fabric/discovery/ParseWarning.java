package minicraft.mods.fabric.discovery;

final class ParseWarning {
	private final int line;
	private final int column;
	private final String key;
	private final String reason;

	ParseWarning(int line, int column, String key) {
		this(line, column, key, null);
	}

	ParseWarning(int line, int column, String key, /* @Nullable */ String reason) {
		this.line = line;
		this.column = column;
		this.key = key;
		this.reason = reason;
	}

	public int getLine() {
		return this.line;
	}

	public int getColumn() {
		return this.column;
	}

	public String getKey() {
		return this.key;
	}

	public String getReason() {
		return this.reason;
	}
}
