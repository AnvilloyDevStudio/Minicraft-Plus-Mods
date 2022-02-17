package minicraft.core.io;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.util.HashMap;

import minicraft.screen.entry.ArrayEntry;
import minicraft.screen.entry.BooleanEntry;
import minicraft.screen.entry.RangeEntry;

public class Settings {

    @SuppressWarnings("rawtypes")
    private static HashMap<String, ArrayEntry> options = new HashMap<>();

    static {
        options.put("fps", new RangeEntry("Max FPS", 10, 300, getRefreshRate()));
        options.put("diff", new ArrayEntry<>("Difficulty", "Peaceful", "Easy", "Normal", "Hard"));
        options.get("diff").setSelection(2);
        options.put("mode", new ArrayEntry<>("Game Mode", "Survival", "Creative", "Hardcore", "Score"));

        options.put("scoretime", new ArrayEntry<>("Time (Score Mode)", 10, 20, 40, 60, 120));
        options.get("scoretime").setValueVisibility(10, false);
        options.get("scoretime").setValueVisibility(120, false);

        options.put("sound", new BooleanEntry("Sound", true));
        options.put("autosave", new BooleanEntry("Autosave", true));

        options.put("ambient", new ArrayEntry<>("Ambient", "Nice", "Normal", "Scary"));

        options.put("size", new ArrayEntry<>("World Size", 128, 256, 512, 1024));
        options.put("theme", new ArrayEntry<>("World Theme", "Normal", "Forest", "Desert", "Plain", "Hell"));
        options.put("type", new ArrayEntry<>("Terrain Type", "Island", "Box", "Mountain", "Irregular"));

        options.put("unlockedskin", new BooleanEntry("Wear Suit", false));
        options.put("skinon", new BooleanEntry("Wear Suit", false));

        options.put("language", new ArrayEntry<>("Language", true, false, Localization.getLanguages()));
        options.get("language").setValue(Localization.getSelectedLanguage());

        options.get("mode").setChangeAction(value -> options.get("scoretime").setVisible("Score".equals(value)));

        options.get("unlockedskin").setChangeAction(value -> options.get("skinon").setVisible((boolean) value));

        options.put("textures", new ArrayEntry<>("Textures", "Original", "Custom"));
        options.get("textures").setSelection(0);
    }

    public static void init() {
    }

    // Returns the value of the specified option
    public static Object get(String option) {
        return options.get(option.toLowerCase()).getValue();
    }

    // Returns the index of the value in the list of values for the specified option
    public static int getIdx(String option) {
        return options.get(option.toLowerCase()).getSelection();
    }

    // Return the ArrayEntry object associated with the given option name.
    @SuppressWarnings("rawtypes")
    public static ArrayEntry getEntry(String option) {
        return options.get(option.toLowerCase());
    }

    // Sets the value of the given option name, to the given value, provided it is a
    // valid value for that option.
    public static void set(String option, Object value) {
        options.get(option.toLowerCase()).setValue(value);
    }

    // Sets the index of the value of the given option, provided it is a valid index
    public static void setIdx(String option, int idx) {
        options.get(option.toLowerCase()).setSelection(idx);
    }

    private static int getRefreshRate() {
        if (GraphicsEnvironment.isHeadless())
            return 60;

        int hz;
        try {
            hz = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode()
                    .getRefreshRate();
        } catch (HeadlessException e) {
            return 60;
        }

        if (hz == DisplayMode.REFRESH_RATE_UNKNOWN)
            return 60;
        if (hz > 300)
            return 60;
        if (10 > hz)
            return 60;
        return hz;
    }
}