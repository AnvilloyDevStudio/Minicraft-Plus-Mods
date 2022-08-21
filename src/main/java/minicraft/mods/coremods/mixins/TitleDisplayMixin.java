package minicraft.mods.coremods.mixins;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import minicraft.core.Game;
import minicraft.gfx.Color;
import minicraft.gfx.Point;
import minicraft.gfx.Screen;
import minicraft.mods.coremods.ModsDisplay;
import minicraft.screen.AchievementsDisplay;
import minicraft.screen.BookDisplay;
import minicraft.screen.Display;
import minicraft.screen.Menu;
import minicraft.screen.OptionsMainMenuDisplay;
import minicraft.screen.RelPos;
import minicraft.screen.SkinDisplay;
import minicraft.screen.TitleDisplay;
import minicraft.screen.WorldGenDisplay;
import minicraft.screen.WorldSelectDisplay;
import minicraft.screen.entry.BlankEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.screen.entry.StringEntry;
import minicraft.util.BookData;

@Mixin(TitleDisplay.class)
public abstract class TitleDisplayMixin extends Display {
	@Inject(method = "<init>", at = @At(value = "TAIL", remap = false), remap = false)
	private void insertMenusInit(CallbackInfo ci) {
		menus = new Menu[] {
			new Menu.Builder(false, 2, RelPos.CENTER,
			new StringEntry("Checking for updates...", Color.BLUE),
			new BlankEntry(),
			new SelectEntry("Play", () -> {
				if (WorldSelectDisplay.getWorldNames().size() > 0)
					Game.setDisplay(new Display(true, new Menu.Builder(false, 2, RelPos.CENTER,
						new SelectEntry("Load World", () -> Game.setDisplay(new WorldSelectDisplay())),
						new SelectEntry("New World", () -> Game.setDisplay(new WorldGenDisplay()))
					).createMenu()));
				else Game.setDisplay(new WorldGenDisplay());
			}),
			new SelectEntry("Mods", () -> Game.setDisplay(new ModsDisplay())),
			new SelectEntry("Options", () -> Game.setDisplay(new OptionsMainMenuDisplay())),
            new SelectEntry("minicraft.display.skin", () -> Game.setDisplay(new SkinDisplay())),
			new SelectEntry("minicraft.display.achievement", () -> Game.setDisplay(new AchievementsDisplay())),
			new SelectEntry("Help", () ->
				Game.setDisplay(new Display(true, new Menu.Builder(false, 1, RelPos.CENTER,
					new BlankEntry(),
					new SelectEntry("Instructions", () -> Game.setDisplay(new BookDisplay(BookData.instructions))),
					new SelectEntry("Storyline Guide", () -> Game.setDisplay(new BookDisplay(BookData.storylineGuide))),
					new SelectEntry("About", () -> Game.setDisplay(new BookDisplay(BookData.about))),
					new SelectEntry("Credits", () -> Game.setDisplay(new BookDisplay(BookData.credits)))
				).setTitle("Help").createMenu()))
			),
			new SelectEntry("Quit", Game::quit)
			)
			.setPositioning(new Point(Screen.w/2, Screen.h*3/5), RelPos.CENTER)
			.createMenu()
		};
	}
}
