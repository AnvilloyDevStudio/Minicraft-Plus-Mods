package minicraft.mods.fabric.discovery;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;

public class DirectoryModCandidateFinder implements ModCandidateFinder {
	private final Path path;
	private final boolean requiresRemap;

	public DirectoryModCandidateFinder(Path path, boolean requiresRemap) {
		this.path = path;
		this.requiresRemap = requiresRemap;
	}

	@Override
	public void findCandidates(ModCandidateConsumer out) {
		if (!Files.exists(path)) {
			try {
				Files.createDirectory(path);
			} catch (IOException e) {
				throw new RuntimeException("Could not create directory " + path, e);
			}
		}

		if (!Files.isDirectory(path)) {
			throw new RuntimeException(path + " is not a directory!");
		}

		try {
			Files.walkFileTree(this.path, EnumSet.of(FileVisitOption.FOLLOW_LINKS), 1, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (isValidFile(file)) {
						out.accept(file, requiresRemap);
					}

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException("Exception while searching for mods in '" + path + "'!", e);
		}
	}

	static boolean isValidFile(Path path) {
		/*
		 * We only propose a file as a possible mod in the following scenarios:
		 * General: Must be a jar file
		 *
		 * Some OSes Generate metadata so consider the following because of OSes:
		 * UNIX: Exclude if file is hidden; this occurs when starting a file name with `.`
		 * MacOS: Exclude hidden + startsWith "." since Mac OS names their metadata files in the form of `.mod.jar`
		 */

		if (!Files.isRegularFile(path)) return false;

		try {
			if (Files.isHidden(path)) return false;
		} catch (IOException e) {
			Log.warn(LogCategory.DISCOVERY, "Error checking if file %s is hidden", path, e);
			return false;
		}

		String fileName = path.getFileName().toString();

		return fileName.endsWith(".jar") && !fileName.startsWith(".");
	}
}
