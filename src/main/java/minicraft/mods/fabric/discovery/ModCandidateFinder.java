package minicraft.mods.fabric.discovery;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@FunctionalInterface
interface ModCandidateFinder {
	void findCandidates(ModCandidateConsumer out);

	interface ModCandidateConsumer {
		default void accept(Path path, boolean requiresRemap) {
			accept(Collections.singletonList(path), requiresRemap);
		}

		void accept(List<Path> paths, boolean requiresRemap);
	}
}
