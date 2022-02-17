package minicraft.entity;

import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import minicraft.core.Game;
import minicraft.core.Updater;
import minicraft.core.io.Sound;
import minicraft.entity.mob.Player;
import minicraft.entity.mob.RemotePlayer;
import minicraft.entity.particle.SplashParticle;
import minicraft.gfx.Rectangle;
import minicraft.gfx.Screen;
import minicraft.gfx.Sprite;
import minicraft.item.BoatItem;
import minicraft.item.Item;
import minicraft.item.PowerGloveItem;

public class Boat extends Entity {
    private static Sprite boatSprite = new Sprite(1, 30, 2, 2, 0);
    private static Sprite boatUp = new Sprite(3, 30, 2, 2, 0);
    private static Sprite boatDown = new Sprite(5, 30, 2, 2, 0);
    private static Sprite boatLeft = new Sprite(1, 30, 2, 2, 0);
    private static Sprite boatRight = new Sprite(7, 30, 2, 2, 0);

    private Player playerInBoat = null;

    private Random rnd = new Random();

    private int exitTimer = 0;

    public Boat() {
        super(1, 1);
    }

    public Boat(int xr, int yr) {
        super(xr, yr);
    }

    @Override
    public void render(Screen screen) {
        boatSprite.render(screen, x - 8, y - 8);

        if (playerInBoat != null == true) {

            if (Game.input.getKey("move-up").down) {
                boatUp.render(screen, x - 8, y - 8);
            }
            if (Game.input.getKey("move-down").down) {
                boatDown.render(screen, x - 8, y - 8);
            }
            if (Game.input.getKey("move-left").down) {
                boatLeft.render(screen, x - 8, y - 8);
            }
            if (Game.input.getKey("move-right").down) {
                boatRight.render(screen, x - 8, y - 8);
            }

        }
    }

    @Override
    public void tick() {
        if (playerInBoat != null) {
            exitTimer--;

            if (exitTimer <= 0 && Game.input.getKey("SHIFT").down) {
                if (Game.player.equals(playerInBoat)) {
                    playerInBoat = null;
                    return;
                }
            }

            double ya = 0;
            double xa = 0;

            if (Game.input.getKey("move-up").down)
                ya -= 1;
            if (Game.input.getKey("move-down").down)
                ya += 1;
            if (Game.input.getKey("move-left").down)
                xa -= 1;
            if (Game.input.getKey("move-right").down)
                xa += 1;

            int randX = rnd.nextInt(10);
            int randY = rnd.nextInt(9);

            level.add(new SplashParticle(x - 8 + randX, y - 8 + randY));

            move(xa, ya);
            playerInBoat.x = x;
            playerInBoat.y = y;
            playerInBoat.stamina = Player.maxStamina;
        }
    }

    @Override
    public boolean canSwim() {
        return true;
    }

    public boolean use(Player player) {
        if (playerInBoat == null) {
            playerInBoat = player;
            exitTimer = 10;
            return true;
        }

        return false;
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public boolean interact(Player player, @Nullable Item item, Direction attackDir) {
        if (item instanceof PowerGloveItem) {
            Sound.Mob_generic_hurt.play();
            if (!Game.ISONLINE) {
                remove();
                if (!Game.isMode("creative") && player.activeItem != null
                        && !(player.activeItem instanceof PowerGloveItem))
                    player.getInventory().add(0, player.activeItem); // put whatever item the player is holding into
                                                                     // their inventory
                player.activeItem = new BoatItem("Boat"); // make this the player's current item.
                return true;
            } else if (Game.isValidServer() && player instanceof RemotePlayer) {
                remove();
                Game.server.getAssociatedThread((RemotePlayer) player).updatePlayerActiveItem(new BoatItem("Boat"));
                return true;
            } else
                System.out.println("WARNING: undefined behavior; online game was not server and ticked furniture: "
                        + this + "; and/or player in online game found that isn't a RemotePlayer: " + player);
        }
        return false;
    }

    public boolean move(double xa, double ya) {
        if (Updater.saving || (xa == 0 && ya == 0))
            return true; // pretend that it kept moving

        boolean stopped = true; // used to check if the entity has BEEN stopped, COMPLETELY; below checks for a
                                // lack of collision.
        if (move2(xa, 0))
            stopped = false; // becomes false if horizontal movement was successful.
        if (move2(0, ya))
            stopped = false; // becomes false if vertical movement was successful.
        if (!stopped) {
            int xt = x >> 4; // the x tile coordinate that the entity is standing on.
            int yt = y >> 4; // the y tile coordinate that the entity is standing on.
            level.getTile(xt, yt).steppedOn(level, xt, yt, this); // Calls the steppedOn() method in a tile's class.
                                                                  // (used for tiles like sand (footprints) or lava
                                                                  // (burning))
        }

        return !stopped;
    }

    public boolean move2(double xa, double ya) {
        if (xa == 0 && ya == 0)
            return true; // was not stopped

        boolean interact = true;// !Game.isValidClient() || this instanceof ClientTickable;

        // gets the tile coordinate of each direction from the sprite...
        int xto0 = ((x) - 1) >> 4; // to the left
        int yto0 = ((y) - 1) >> 4; // above
        int xto1 = ((x) + 1) >> 4; // to the right
        int yto1 = ((y) + 1) >> 4; // below

        // gets same as above, but after movement.
        int xt0 = (int) ((x + xa) - 1) >> 4;
        int yt0 = (int) ((y + ya) - 1) >> 4;
        int xt1 = (int) ((x + xa) + 1) >> 4;
        int yt1 = (int) ((y + ya) + 1) >> 4;

        // boolean blocked = false; // if the next tile can block you.
        for (int yt = yt0; yt <= yt1; yt++) { // cycles through y's of tile after movement
            for (int xt = xt0; xt <= xt1; xt++) { // cycles through x's of tile after movement
                if (xt >= xto0 && xt <= xto1 && yt >= yto0 && yt <= yto1)
                    continue; // skip this position if this entity's sprite is touching it
                if (level.getTile(xt, yt).id != 6)
                    return false;
            }
        }

        // these lists are named as if the entity has already moved-- it hasn't, though.
        List<Entity> wasInside = level.getEntitiesInRect(getBounds()); // gets all of the entities that are inside this
                                                                       // entity (aka: colliding) before moving.

        int xr = 1;
        int yr = 1;
        List<Entity> isInside = level
                .getEntitiesInRect(new Rectangle(x + (int) xa, y + (int) ya, xr * 2, yr * 2, Rectangle.CENTER_DIMS)); // gets
                                                                                                                      // the
                                                                                                                      // entities
                                                                                                                      // that
                                                                                                                      // this
                                                                                                                      // entity
                                                                                                                      // will
                                                                                                                      // touch
                                                                                                                      // once
                                                                                                                      // moved.
        for (int i = 0; interact && i < isInside.size(); i++) {
            /// cycles through entities about to be touched, and calls touchedBy(this) for
            /// each of them.
            Entity e = isInside.get(i);
            if (e == this)
                continue; // touching yourself doesn't count.

            if (e instanceof Player) {
                touchedBy(e);
            } else
                e.touchedBy(this); // call the method. ("touch" the entity)
        }

        isInside.removeAll(wasInside); // remove all the entities that this one is already touching before moving.
        for (int i = 0; i < isInside.size(); i++) {
            Entity e = isInside.get(i);

            if (e == this) {
                continue; // can't interact with yourself
            }

            if (e.blocks(this)) {
                return false; // if the entity prevents this one from movement, don't move.
            }
        }

        // finally, the entity moves!
        x += xa;
        y += ya;

        return true; // the move was successful.
    }

    @Override
    public Boat clone() {
        return new Boat();
    }
}
