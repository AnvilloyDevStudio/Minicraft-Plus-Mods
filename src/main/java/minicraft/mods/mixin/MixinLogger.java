package minicraft.mods.mixin;

import org.spongepowered.asm.logging.Level;
import org.spongepowered.asm.logging.LoggerAdapterAbstract;
import org.tinylog.Logger;

final class MixinLogger extends LoggerAdapterAbstract {
	MixinLogger(String name) {
		super(name);
	}

	@Override
	public String getType() {
		return "MiniMods Mixin Logger";
	}

	@Override
	public void catching(Level level, Throwable t) {
		log(level, "Catching ".concat(t.toString()), t);
	}

	@Override
	public void log(Level level, String message, Object... params) {
		switch (translateLevel(level)) {
			case DEBUG:
				Logger.debug(message, params);
				break;
			case ERROR:
				Logger.error(message, params);
				break;
			default:
			case INFO:
				Logger.info(message, params);
				break;
			case TRACE:
				Logger.trace(message, params);
				break;
			case WARN:
				Logger.warn(message, params);
				break;
		}
	}

	@Override
	public void log(Level level, String message, Throwable t) {
		log(level, message, new Object[]{t});
	}

	@Override
	public <T extends Throwable> T throwing(T t) {
		log(Level.ERROR, "Throwing ".concat(t.toString()), t);

		return t;
	}

	private static org.tinylog.Level translateLevel(Level level) {
		return level == Level.FATAL ? org.tinylog.Level.ERROR : org.tinylog.Level.valueOf(level.toString());
	}
}
