package minicraft.mods.mixin;

import java.util.HashMap;

import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

public class GlobalPropertyService implements IGlobalPropertyService {
	public static final HashMap<String, Object> properties = new HashMap<>();

	@Override
	public IPropertyKey resolveKey(String name) {
		return new MixinStringPropertyKey(name);
	}

	private String keyString(IPropertyKey key) {
		return ((MixinStringPropertyKey) key).key;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(IPropertyKey key) {
		return (T) properties.get(keyString(key));
	}

	@Override
	public void setProperty(IPropertyKey key, Object value) {
		properties.put(keyString(key), value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(IPropertyKey key, T defaultValue) {
		return (T) properties.getOrDefault(keyString(key), defaultValue);
	}

	@Override
	public String getPropertyString(IPropertyKey key, String defaultValue) {
		Object o = properties.get(keyString(key));
		return o != null ? o.toString() : defaultValue;
	}
}
