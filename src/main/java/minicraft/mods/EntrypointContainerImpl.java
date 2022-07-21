package minicraft.mods;

import net.fabricmc.loader.api.EntrypointException;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;

public final class EntrypointContainerImpl<T> implements EntrypointContainer<T> {
	private final String key;
	private final Class<T> type;
	private final EntrypointStorage.Entry entry;
	private T instance;

	/**
	 * Create EntrypointContainer with lazy init.
	 */
	public EntrypointContainerImpl(String key, Class<T> type, EntrypointStorage.Entry entry) {
		this.key = key;
		this.type = type;
		this.entry = entry;
	}

	/**
	 * Create EntrypointContainer without lazy init.
	 */
	public EntrypointContainerImpl(EntrypointStorage.Entry entry, T instance) {
		this.key = null;
		this.type = null;
		this.entry = entry;
		this.instance = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public synchronized T getEntrypoint() {
		if (instance == null) {
			try {
				instance = entry.getOrCreate(type);
				assert instance != null;
			} catch (Exception ex) {
				throw new EntrypointException(key, getProvider().getMetadata().getId(), ex);
			}
		}

		return instance;
	}

	@Override
	public ModContainer getProvider() {
		return entry.getModContainer();
	}
}
