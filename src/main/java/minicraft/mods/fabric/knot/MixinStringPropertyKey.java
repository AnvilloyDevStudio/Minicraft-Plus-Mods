package minicraft.mods.fabric.knot;

import java.util.Objects;

import org.spongepowered.asm.service.IPropertyKey;

public class MixinStringPropertyKey implements IPropertyKey {
	public final String key;

	public MixinStringPropertyKey(String key) {
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MixinStringPropertyKey)) {
			return false;
		} else {
			return Objects.equals(this.key, ((MixinStringPropertyKey) obj).key);
		}
	}

	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public String toString() {
		return this.key;
	}
}
