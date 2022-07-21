package minicraft.mods.game;

import java.nio.file.Path;
import java.nio.file.Paths;

import minicraft.mods.FabricLoaderImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.impl.entrypoint.EntrypointUtils;

public class Hooks {
	public static final String INTERNAL_NAME = Hooks.class.getName().replace('.', '/');
    public static void init() {
        Path runDir = Paths.get(".");
		FabricLoaderImpl.INSTANCE.freeze();
        FabricLoaderImpl.INSTANCE.prepareModInit(runDir, FabricLoaderImpl.INSTANCE.getGameInstance());
        EntrypointUtils.invoke("main", ModInitializer.class, ModInitializer::onInitialize);
        EntrypointUtils.invoke("client", ClientModInitializer.class, ClientModInitializer::onInitializeClient);
        EntrypointUtils.invoke("server", DedicatedServerModInitializer.class, DedicatedServerModInitializer::onInitializeServer);
    }
}
