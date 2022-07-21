package minicraft.mods.discovery;

import net.fabricmc.loader.api.metadata.ModDependency;

class Explanation implements Comparable<Explanation> {
	private static int nextCmpId;

	final ErrorKind error;
	final ModCandidate mod;
	final ModDependency dep;
	final String data;
	private final int cmpId;

	Explanation(ErrorKind error, ModCandidate mod) {
		this(error, mod, null, null);
	}

	Explanation(ErrorKind error, ModCandidate mod, ModDependency dep) {
		this(error, mod, dep, null);
	}

	Explanation(ErrorKind error, String data) {
		this(error, null, data);
	}

	Explanation(ErrorKind error, ModCandidate mod, String data) {
		this(error, mod, null, data);
	}

	private Explanation(ErrorKind error, ModCandidate mod, ModDependency dep, String data) {
		this.error = error;
		this.mod = mod;
		this.dep = dep;
		this.data = data;
		this.cmpId = nextCmpId++;
	}

	@Override
	public int compareTo(Explanation o) {
		return Integer.compare(cmpId, o.cmpId);
	}

	@Override
	public String toString() {
		if (mod == null) {
			return String.format("%s %s", error, data);
		} else if (dep == null) {
			return String.format("%s %s", error, mod);
		} else {
			return String.format("%s %s %s", error, mod, dep);
		}
	}

	enum ErrorKind {
		/**
		 * Positive hard dependency (depends) from a preselected mod.
		 */
		PRESELECT_HARD_DEP(true),
		/**
		 * Positive soft dependency (recommends) from a preselected mod.
		 */
		PRESELECT_SOFT_DEP(true),
		/**
		 * Negative hard dependency (breaks) from a preselected mod.
		 */
		PRESELECT_NEG_HARD_DEP(true),
		/**
		 * Force loaded due to being preselected.
		 */
		PRESELECT_FORCELOAD(false),
		/**
		 * Positive hard dependency (depends) from a mod with incompatible preselected candidate.
		 */
		HARD_DEP_INCOMPATIBLE_PRESELECTED(true),
		/**
		 * Positive hard dependency (depends) from a mod with no matching candidate.
		 */
		HARD_DEP_NO_CANDIDATE(true),
		/**
		 * Positive hard dependency (depends) from a mod.
		 */
		HARD_DEP(true),
		/**
		 * Positive soft dependency (recommends) from a mod.
		 */
		SOFT_DEP(true),
		/**
		 * Negative hard dependency (breaks) from a mod.
		 */
		NEG_HARD_DEP(true),
		/**
		 * Force loaded if the parent is loaded due to LoadType ALWAYS.
		 */
		NESTED_FORCELOAD(false),
		/**
		 * Dependency of a nested mod on its parent mods.
		 */
		NESTED_REQ_PARENT(false),
		/**
		 * Force loaded due to LoadType ALWAYS as a singular root mod.
		 */
		ROOT_FORCELOAD_SINGLE(false),
		/**
		 * Force loaded due to LoadType ALWAYS and containing root mods.
		 */
		ROOT_FORCELOAD(false),
		/**
		 * Requirement to load at most one mod per id (including provides).
		 */
		UNIQUE_ID(false);

		final boolean isDependencyError;

		ErrorKind(boolean isDependencyError) {
			this.isDependencyError = isDependencyError;
		}
	}
}
