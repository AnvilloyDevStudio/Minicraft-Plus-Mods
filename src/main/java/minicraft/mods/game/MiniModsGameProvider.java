package minicraft.mods.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import minicraft.core.Game;
import minicraft.mods.Mods;
import net.fabricmc.loader.impl.FormattedException;
import net.fabricmc.loader.impl.game.GameProvider;
import net.fabricmc.loader.impl.game.patch.GameTransformer;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.metadata.BuiltinModMetadata;
import net.fabricmc.loader.impl.util.Arguments;
import net.fabricmc.loader.impl.util.log.ConsoleLogHandler;
import net.fabricmc.loader.impl.util.log.Log;

public class MiniModsGameProvider implements GameProvider {
	private static final String entryPoint = "minicraft.core.Game";
	private static final Set<String> SENSITIVE_ARGS = new HashSet<>(Arrays.asList(
			// all lowercase without --
			"debug",
			"savedir",
			"fullscreen"));

	private Path gameJar;
	private Arguments arguments;

	private static final GameTransformer TRANSFORMER = new GameTransformer(new EntrypointPatch());

	@Override
	public String getGameId() {
		return "minimods";
	}

	@Override
	public String getGameName() {
		return "MiniMods";
	}

	@Override
	public String getRawGameVersion() {
		return Game.VERSION.toString();
	}

	@Override
	public String getNormalizedGameVersion() {
		return getRawGameVersion();
	}

	@Override
	public Collection<BuiltinMod> getBuiltinMods() {
		Collection<BuiltinMod> metadatas = new ArrayList<>();

		metadatas.add(new BuiltinMod(Collections.singletonList(gameJar), new BuiltinModMetadata.Builder("minicraft", getNormalizedGameVersion())
			.setName("Minicraft+")
			.addAuthor("Minicraft+ Contributors", new HashMap<>(Map.of("homepage", "https://playminicraft.com/",
				"wiki", "https://minicraft.fandom.com/",
				"discord", "https://discord.me/minicraft")))
			.build()));

		metadatas.add(new BuiltinMod(Collections.singletonList(gameJar), new BuiltinModMetadata.Builder(getGameId(), Mods.VERSION.toString())
			.setName("MiniMods")
			.addAuthor("Ben Forge", new HashMap<>(Map.of("github", "https://github.com/BenCheung0422/Minicraft-Plus-Mods",
				"wiki", "https://github.com/BenCheung0422/Minicraft-Plus-Mods/wiki",
				"discord", "https://discord.com/invite/87DF72RqHu")))
			.build()));

		return metadatas;
	}

	@Override
	public String getEntrypoint() {
		return entryPoint;
	}

	@Override
	public Path getLaunchDirectory() {
		return Paths.get(".");
	}

	@Override
	public boolean isObfuscated() {
		return false;
	}

	@Override
	public boolean requiresUrlClassLoader() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean locateGame(FabricLauncher launcher, String[] args) {
		this.arguments = new Arguments();
		arguments.parse(args);

		try {
			gameJar = Path.of(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		return true;
	}

	@Override
	public void initialize(FabricLauncher launcher) {
		TRANSFORMER.locateEntrypoints(launcher, List.of(gameJar));
		Log.init(new ConsoleLogHandler());
	}

	@Override
	public GameTransformer getEntrypointTransformer() {
		return TRANSFORMER;
	}

	@Override
	public void unlockClassPath(FabricLauncher launcher) {
		launcher.addToClassPath(gameJar);
	}

	@Override
	public void launch(ClassLoader loader) {
		try {
			Class<?> c = loader.loadClass(entryPoint);
			Method m = c.getMethod("main", String[].class);
			m.invoke(null, (Object) arguments.toArray());
		} catch(InvocationTargetException e) {
			throw new FormattedException("Minicraft has crashed!", e.getCause());
		} catch(ReflectiveOperationException e) {
			throw new FormattedException("Failed to start Minicraft", e);
		}
	}

	@Override
	public Arguments getArguments() {
		return arguments;
	}

	@Override
	public String[] getLaunchArguments(boolean sanitize) {
		if (arguments == null) return new String[0];

		String[] ret = arguments.toArray();
		if (!sanitize) return ret;

		int writeIdx = 0;

		for (int i = 0; i < ret.length; i++) {
			String arg = ret[i];

			if (i + 1 < ret.length
					&& arg.startsWith("--")
					&& SENSITIVE_ARGS.contains(arg.substring(2).toLowerCase(Locale.ENGLISH))) {
				i++; // skip value
			} else {
				ret[writeIdx++] = arg;
			}
		}

		if (writeIdx < ret.length) ret = Arrays.copyOf(ret, writeIdx);

		return ret;
	}
}
