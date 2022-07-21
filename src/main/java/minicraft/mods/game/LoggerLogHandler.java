package minicraft.mods.game;

import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.log.LogHandler;
import net.fabricmc.loader.impl.util.log.LogLevel;

public class LoggerLogHandler implements LogHandler {

	@Override
	public void log(long time, LogLevel level, LogCategory category, String msg, Throwable exc, boolean fromReplay,
			boolean wasSuppressed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldLog(LogLevel level, LogCategory category) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

}
