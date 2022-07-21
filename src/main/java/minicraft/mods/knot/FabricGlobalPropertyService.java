package minicraft.mods.knot;

import org.spongepowered.asm.service.IGlobalPropertyService;
import org.spongepowered.asm.service.IPropertyKey;

import minicraft.mods.FabricLauncherBase;

public class FabricGlobalPropertyService implements IGlobalPropertyService {
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
		return (T) FabricLauncherBase.getProperties().get(keyString(key));
	}

	@Override
	public void setProperty(IPropertyKey key, Object value) {
		FabricLauncherBase.getProperties().put(keyString(key), value);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getProperty(IPropertyKey key, T defaultValue) {
		return (T) FabricLauncherBase.getProperties().getOrDefault(keyString(key), defaultValue);
	}

	@Override
	public String getPropertyString(IPropertyKey key, String defaultValue) {
		Object o = FabricLauncherBase.getProperties().get(keyString(key));
		return o != null ? o.toString() : defaultValue;
	}
}
