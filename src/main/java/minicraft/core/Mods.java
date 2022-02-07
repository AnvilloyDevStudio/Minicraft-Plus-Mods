package minicraft.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import minicraft.core.io.ClassLoader;
import minicraft.gfx.Sprite;
import minicraft.gfx.SpriteSheet;
import minicraft.item.*;
import minicraft.mod.ModItem;

public class Mods extends Game {
    private Mods() {}
    public static ArrayList<JSONObject> Mods = new ArrayList<>();
    public static ArrayList<Mod.Item> Items = new ArrayList<>();
    public static void init() {}

    static {
        File[] mods = FileHandler.readModsFolder();
        if (mods == null) {
            Mods = null;
        } else {
            ClassLoader loader = new ClassLoader();
            for (int a = 0; a<mods.length; a++) {
                Mod modObj = new Mod();
                Pair<Pair<Class<?>, Mod.Resources>, JSONObject> modP = loader.loadJar(mods[a]);
                modObj.Info = modP.getRight();
                Class<?> mod = modP.getLeft().getLeft();
                modObj.Resources = modP.getLeft().getRight();
                Class<?>[] modclasses = mod.getDeclaredClasses();
                for (int b = 0; b<modclasses.length; b++) {
                    if (modclasses[b].getName().equals("mod.Module$Items")) {
                        Class<?>[] itemsC = modclasses[b].getDeclaredClasses();
                        for (int c = 0; c<itemsC.length; c++) {
                            Items.add(new Mod.Item(itemsC[c], modObj.Resources));
                        }
                    }
                }
                if (modObj.Info.getString("name") == null) System.out.println("mod.json name not found.");
                if (modObj.Info.getString("description") == null) System.out.println("mod.json description not found.");
                Mods.add(modObj.Info);
            }
        }
    }
    public static class Mod {
        public JSONObject Info;
        public static class Item {
            public String name;
            public String itemtype;
            public String tooltype;
            public int durability;
            public boolean noLevel;
            public int tooltypelvl;
            public boolean attack;
            public boolean spriteSheet;
            public int[] sprite;
            private Resources resources;
            public SpriteSheet findSpriteSheet(Resources resources) {
                if (spriteSheet) return resources.ItemsSheet;
                else {
                    SpriteSheet img = resources.ItemImages.get(name+".png");
                    if (img == null) {
                        sprite = new int[] {0, 31};
                        return resources.Sheets[0];
                    }
                    else return img;
                }
            }
            Item(Class<?> Obj, Resources res) {
                resources = res;
                try {
                    name = (String)Obj.getDeclaredField("name").get(null); // Item name
                    itemtype = (String)Obj.getDeclaredField("itemtype").get(null); // Item type
                    try{tooltype = (String)Obj.getDeclaredField("type").get(null);} catch (NoSuchFieldException e) {tooltype = null;}; // Item level
                    try{durability = (Integer)Obj.getDeclaredField("durability").get(null);} catch (NoSuchFieldException e) {durability = 1;};
                    try{noLevel = (boolean)Obj.getDeclaredField("noLevel").get(null);} catch (NoSuchFieldException e) {noLevel = true;};
                    if (!noLevel) tooltypelvl = (Integer)Obj.getDeclaredField("level").get(null); // Item level level number
                    try{attack = (boolean)Obj.getDeclaredField("attack").get(null);} catch (NoSuchFieldException e) {attack = true;}; // canAttack
                    spriteSheet = (boolean)Obj.getDeclaredField("spriteSheet").get(null); // false or ignore if sprite is separated from items.png
                    try{sprite = (int[])Obj.getDeclaredField("sprite").get(null);} catch (NoSuchFieldException e) {sprite = new int[] {0, 0};} // xPos, yPos
                } catch (IllegalArgumentException
                        | SecurityException | NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            public ToolItem toToolItem() {
                ToolType toolType = ToolType.Types.containsKey(name)? ToolType.Types.get(name): new ToolType(name, resources.getSprite(findSpriteSheet(resources), sprite[0], sprite[1]), durability, attack, noLevel);
                if (!noLevel) {
                    ItemLevel itemLevel = ItemLevel.Levels.containsKey(tooltype)? ItemLevel.Levels.get(tooltype): new ItemLevel(tooltype, tooltypelvl);
                    return new ToolItem(toolType, itemLevel);
                }
                else return new ToolItem(toolType);
            }
            public StackableItem toStackableItem() {
                return new StackableItem(name, resources.getSprite(findSpriteSheet(resources), sprite[0], sprite[1]));
            }
        }
        public Resources Resources;
        public static class Resources {
            private SpriteSheet BytestoSpriteSheet(byte[] bytes) throws IOException {return new SpriteSheet(ImageIO.read(new ByteArrayInputStream(bytes)));}
            public SpriteSheet ItemsSheet;
            public HashMap<String, SpriteSheet> ItemImages = new HashMap<>();
            public Manifest manifest;
            public SpriteSheet[] Sheets = new SpriteSheet[] {
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/items.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/tiles.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/entities.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/gui.png"))),
                new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/resources/textures/skins.png")))
            };
            public Sprite getSprite(SpriteSheet sheet, int x, int y) {
                return new Sprite(new Sprite.Px[][]{new Sprite.Px[]{new Sprite.Px(x, y, 0, sheet)}});
            }
            public Resources(JarFile jar, Manifest man) throws IOException {
                manifest = man;
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().startsWith("resources/")) {
                        if (entry.getName().equals("resources/items.png")) ItemsSheet = BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes());
                        if (!entry.isDirectory()&&entry.getName().startsWith("resources/item/")&&entry.getName().endsWith(".png")) {
                            ItemImages.put(entry.getName().substring(15), BytestoSpriteSheet(jar.getInputStream(entry).readAllBytes()));
                        }
                    }
                }
            }
        }
    }
}
