package minicraft.mods;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.tinylog.Logger;

import minicraft.mods.mixin.ModClassDelegate;

@SuppressWarnings("unused")
public class GameTransformer {
	private static HashMap<String, byte[]> patchedClasses = new HashMap<>();

	private static ClassNode readClass(ClassReader reader) {
		if (reader == null) return null;

		ClassNode node = new ClassNode();
		reader.accept(node, 0);
		return node;
	}

	private static FieldNode findField(ClassNode node, Predicate<FieldNode> predicate) {
		return node.fields.stream().filter(predicate).findAny().orElse(null);
	}

	private static List<FieldNode> findFields(ClassNode node, Predicate<FieldNode> predicate) {
		return node.fields.stream().filter(predicate).collect(Collectors.toList());
	}

	private static MethodNode findMethod(ClassNode node, Predicate<MethodNode> predicate) {
		return node.methods.stream().filter(predicate).findAny().orElse(null);
	}

	private static AbstractInsnNode findInsn(MethodNode node, Predicate<AbstractInsnNode> predicate, boolean last) {
		if (last) {
			for (int i = node.instructions.size() - 1; i >= 0; i--) {
				AbstractInsnNode insn = node.instructions.get(i);

				if (predicate.test(insn)) {
					return insn;
				}
			}
		} else {
			for (int i = 0; i < node.instructions.size(); i++) {
				AbstractInsnNode insn = node.instructions.get(i);

				if (predicate.test(insn)) {
					return insn;
				}
			}
		}

		return null;
	}

	private static void moveAfter(ListIterator<AbstractInsnNode> it, int opcode) {
		while (it.hasNext()) {
			AbstractInsnNode node = it.next();

			if (node.getOpcode() == opcode) {
				break;
			}
		}
	}

	private static void moveBefore(ListIterator<AbstractInsnNode> it, int opcode) {
		moveAfter(it, opcode);
		it.previous();
	}

	private static void moveAfter(ListIterator<AbstractInsnNode> it, AbstractInsnNode targetNode) {
		while (it.hasNext()) {
			AbstractInsnNode node = it.next();

			if (node == targetNode) {
				break;
			}
		}
	}

	private static void moveBefore(ListIterator<AbstractInsnNode> it, AbstractInsnNode targetNode) {
		moveAfter(it, targetNode);
		it.previous();
	}

	private static void moveBeforeType(ListIterator<AbstractInsnNode> it, int nodeType) {
		while (it.hasPrevious()) {
			AbstractInsnNode node = it.previous();

			if (node.getType() == nodeType) {
				break;
			}
		}
	}

	private static boolean isStatic(int access) {
		return ((access & Opcodes.ACC_STATIC) != 0);
	}

	private static boolean isPublicStatic(int access) {
		return ((access & 0x0F) == (Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC));
	}

	private static boolean isPublicInstance(int access) {
		//noinspection PointlessBitwiseExpression
		return ((access & 0x0F) == (Opcodes.ACC_PUBLIC | 0 /* non-static */));
	}

	private static ClassReader getClassSource(String name) {
		byte[] data = patchedClasses.get(name);

		if (data != null) {
			return new ClassReader(data);
		}

		try (JarFile jar = new JarFile(GameTransformer.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())) {
			JarEntry entry = jar.getJarEntry(name.replace(".", "/").concat(".class"));
			if (entry == null) return null;

			try (InputStream is = jar.getInputStream(entry)) {
				return new ClassReader(is);
			} catch (IOException e) {
				throw new RuntimeException(String.format("error reading %s in %s: %s", name, entry.getName(), e), e);
			}
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e);
		}
	};

	public static void transform() {

		// Transform mod initialization invoker

		ClassNode mainClass = readClass(getClassSource(Mods.entrypoint));
        ClassNode plusInitializer = readClass(getClassSource("minicraft.core.Initializer"));
        MethodNode initMethod = findMethod(plusInitializer, (method) -> method.name.equals("run") && method.desc.equals("()V"));

        Logger.debug("Found init method: %s -> %s", Mods.entrypoint, plusInitializer == null ? mainClass.name : plusInitializer.name);
        Logger.debug("Patching init method %s%s", initMethod.name, initMethod.desc);

        ListIterator<AbstractInsnNode> it = initMethod.instructions.iterator();
        it.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ModHandler.class.getName().replace('.', '/'), "initMods", "()V", false));
		addPatchedClass(plusInitializer);
	}

	private static void addPatchedClass(ClassNode node) {
		String key = node.name.replace('/', '.');
		ClassWriter writer = new ClassWriter(0);
		node.accept(writer);
		patchedClasses.put(key, writer.toByteArray());
	}

	public static byte[] getTransformed(String key) {
		return patchedClasses.get(key);
	}
}
