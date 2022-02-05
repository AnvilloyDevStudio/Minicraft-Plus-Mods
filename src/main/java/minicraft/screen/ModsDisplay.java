package minicraft.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;

import minicraft.core.Game;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Sound;
import minicraft.gfx.*;

public class ModsDisplay extends Display {
	private static final List<JSONObject> mods = new ArrayList<>();
	private static int selectedIndex = 0;

	static {
		// These are all the generic skins. To add one, just add an entry in this list.
		for (JSONObject mod : minicraft.core.Mods.Mods) {
            mods.add(mod);
        }

		// Read and add the .png file to the skins list.
	}

	public ModsDisplay() {
		if (selectedIndex >= mods.size()) selectedIndex = 0;
	}

	@Override
	public void tick(InputHandler input) {
		if (input.getKey("menu").clicked || input.getKey("attack").clicked || input.getKey("exit").clicked) {
			Game.exitMenu();
			return;
		}
		if (input.getKey("cursor-down").clicked && selectedIndex < mods.size() - 1) {
			selectedIndex++;
			Sound.select.play();
		}
		if (input.getKey("cursor-up").clicked && selectedIndex > 0) {
			selectedIndex--;
			Sound.select.play();
		}
	}

	@Override
	public void render(Screen screen) {
		screen.clear(0);

        // Get skin above and below.
		String selectedUUUUUU = selectedIndex + 6 > mods.size() - 6 ? "" : mods.get(selectedIndex + 6).getString("name");
		String selectedUUUUU = selectedIndex + 5 > mods.size() - 5 ? "" : mods.get(selectedIndex + 5).getString("name");
		String selectedUUUU = selectedIndex + 4 > mods.size() - 4 ? "" : mods.get(selectedIndex + 4).getString("name");
		String selectedUUU = selectedIndex + 3 > mods.size() - 3 ? "" : mods.get(selectedIndex + 3).getString("name");
		String selectedUU = selectedIndex + 2 > mods.size() - 2 ? "" : mods.get(selectedIndex + 2).getString("name");
		String selectedU = selectedIndex + 1 > mods.size() - 1 ? "" : mods.get(selectedIndex + 1).getString("name");
		String selectedD = selectedIndex - 1 < 0 ? "" : mods.get(selectedIndex - 1).getString("name");
		String selectedDD = selectedIndex - 2 < 0 ? "" : mods.get(selectedIndex - 2).getString("name");
		String selectedDDD = selectedIndex - 3 < 0 ? "" : mods.get(selectedIndex - 3).getString("name");
		String selectedDDDD = selectedIndex - 4 < 0 ? "" : mods.get(selectedIndex - 4).getString("name");
		String selectedDDDDD = selectedIndex - 5 < 0 ? "" : mods.get(selectedIndex - 5).getString("name");
		String selectedDDDDDD = selectedIndex - 6 < 0 ? "" : mods.get(selectedIndex - 6).getString("name");

		// Title.
		Font.drawCentered("Mods", screen, Screen.h - 185, Color.YELLOW);
        
		// Render the menu.
		Font.draw(ModsDisplay.shortNameIfLong(selectedUUUUUU), screen, 0, Screen.h - 50, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(selectedUUUUU), screen, 0, Screen.h - 60, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(selectedUUUU), screen, 0, Screen.h - 70, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(selectedUUU), screen, 0, Screen.h - 80, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(selectedUU), screen, 0, Screen.h - 90, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(selectedU), screen, 0, Screen.h - 100, Color.GRAY); // First unselected space
		Font.draw(ModsDisplay.shortNameIfLong(mods.get(selectedIndex).getString("name")), screen, 0, Screen.h - 110, Color.WHITE); // Selection
		Font.draw(ModsDisplay.shortNameIfLong(selectedD), screen, 0, Screen.h - 120, Color.GRAY); // Fourth space
		Font.draw(ModsDisplay.shortNameIfLong(selectedDD), screen, 0, Screen.h - 130, Color.GRAY); // Fourth space
		Font.draw(ModsDisplay.shortNameIfLong(selectedDDD), screen, 0, Screen.h - 140, Color.GRAY); // Fourth space
		Font.draw(ModsDisplay.shortNameIfLong(selectedDDDD), screen, 0, Screen.h - 150, Color.GRAY); // Fourth space
		Font.draw(ModsDisplay.shortNameIfLong(selectedDDDDD), screen, 0, Screen.h - 160, Color.GRAY); // Fourth space
		Font.draw(ModsDisplay.shortNameIfLong(selectedDDDDDD), screen, 0, Screen.h - 170, Color.GRAY); // Fourth space
		FontStyle fs = new FontStyle();
		fs.setXPos(screen.w/2);
		fs.setYPos(screen.h-170);
		List<String> des = new ArrayList<>();
		for (String line : Arrays.asList(mods.get(selectedIndex).getString("description").split("\n"))) {
			int br = (int)Math.ceil(line.length()/18)+1;
			for (int a = 0; a<br; a++) {
				int b = (a+1)*18;
				des.add(line.substring(a*18, b>line.length()? line.length(): b));
			}
		}
        Font.drawParagraph(des, screen, fs, 0);
		// Help text.
		Font.drawCentered("Use "+ Game.input.getMapping("cursor-down") + " and " + Game.input.getMapping("cursor-up") + " to move.", screen, Screen.h - 9, Color.WHITE);
	}


	// In case the name is too big ...
	private static String shortNameIfLong(String name) {
		return name.length() > 22 ? name.substring(0, 16) + "..." : name;
	}

	public static int getSelectedIndex() {
		return selectedIndex;
	}

	public static void setSelectedIndex(int selectedIndex) {
		ModsDisplay.selectedIndex = selectedIndex;
	}
}
