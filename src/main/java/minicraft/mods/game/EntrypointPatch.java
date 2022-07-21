package minicraft.mods.game;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.fabricmc.loader.impl.game.patch.GamePatch;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class EntrypointPatch extends GamePatch {
	@Override
	public void process(FabricLauncher launcher, Function<String, ClassReader> classSource, Consumer<ClassNode> classEmitter) {
		String entrypoint = launcher.getEntrypoint();
		ClassNode mainClass = readClass(classSource.apply(entrypoint));
        ClassNode plusInitializer = readClass(classSource.apply("minicraft.core.Initializer"));
        MethodNode initMethod = findMethod(plusInitializer, (method) -> method.name.equals("run") && method.desc.equals("()V"));

        Log.debug(LogCategory.GAME_PATCH, "Found init method: %s -> %s", entrypoint, plusInitializer == null ? mainClass.name : plusInitializer.name);
        Log.debug(LogCategory.GAME_PATCH, "Patching init method %s%s", initMethod.name, initMethod.desc);

        ListIterator<AbstractInsnNode> it = initMethod.instructions.iterator();
        it.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.INTERNAL_NAME, "init", "()V", false));
		classEmitter.accept(plusInitializer);
	}
}
