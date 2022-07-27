package minicraft.mods;

public class LoaderConfig<T> {
	public final static LoaderConfig<String> cfg = new LoaderConfig<>("");

	private T value;
	private final T defaultValue;
	private LoaderConfig(T defaultVal) {
		value = defaultVal;
		defaultValue = defaultVal;
	}

	public void setValue(T val) {
		value = val;
	}
	public T getValue() {
		return value;
	}
	public T resetDefaultValue() {
		return value = defaultValue;
	}
}
