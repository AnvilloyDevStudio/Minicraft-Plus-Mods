package minicraft.mods.fabric.game;

import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.fabricmc.loader.impl.game.patch.GamePatch;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.fabricmc.loader.impl.util.version.StringVersion;

public class EntrypointPatch extends GamePatch {
	@Override
	public void process(FabricLauncher launcher, Function<String, ClassReader> classSource, Consumer<ClassNode> classEmitter) {
		String entrypoint = launcher.getEntrypoint();
		ClassNode mainClass = readClass(classSource.apply(entrypoint));
        ClassNode plusInitializer = readClass(classSource.apply("minicraft.core.Initializer"));
        MethodNode initMethod = findMethod(plusInitializer, (method) -> method.name.equals("run") && method.desc.equals("()V"));

		for (MethodNode method : mainClass.methods) {
            if (method.name.equals("<clinit>")) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction instanceof LdcInsnNode) {
						LdcInsnNode ldcInsnNode = (LdcInsnNode) instruction;
                        Object value = ldcInsnNode.cst;
                        if (value instanceof String) {
                            if (((String) value).contains(".")) {
                                MiniModsGameProvider.setGameVersion(new StringVersion((String)value));
                            }
                        }
                    }
                }
            }
        }

		for (MethodNode method : readClass(classSource.apply("minicraft.mods.Mods")).methods) {
            if (method.name.equals("<clinit>")) {
                for (AbstractInsnNode instruction : method.instructions) {
                    if (instruction instanceof LdcInsnNode) {
						LdcInsnNode ldcInsnNode = (LdcInsnNode) instruction;
                        Object value = ldcInsnNode.cst;
                        if (value instanceof String) {
                            if (((String) value).contains(".")) {
                                MiniModsGameProvider.setModsVersion(new StringVersion((String)value));
                            }
                        }
                    }
                }
            }
        }

        Log.debug(LogCategory.GAME_PATCH, "Found init method: %s -> %s", entrypoint, plusInitializer == null ? mainClass.name : plusInitializer.name);
        Log.debug(LogCategory.GAME_PATCH, "Patching init method %s%s", initMethod.name, initMethod.desc);

        ListIterator<AbstractInsnNode> it = initMethod.instructions.iterator();
        it.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Hooks.INTERNAL_NAME, "init", "()V", false));
		classEmitter.accept(plusInitializer);
	}
}
