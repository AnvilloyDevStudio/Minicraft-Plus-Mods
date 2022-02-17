// 
// Decompiled by Procyon v0.6-prerelease
// 

package com.mojang.ld22.level.levelgen;

import com.mojang.ld22.level.tile.Tile;
import java.util.Random;

public class LevelGen
{
    private static final Random random;
    public double[] values;
    private int w;
    private int h;
    
    static {
        random = new Random();
    }
    
    public LevelGen(final int w, final int h, final int featureSize) {
        this.w = w;
        this.h = h;
        this.values = new double[w * h];
        for (int y = 0; y < w; y += featureSize) {
            for (int x = 0; x < w; x += featureSize) {
                this.setSample(x, y, LevelGen.random.nextFloat() * 2.0f - 1.0f);
            }
        }
        int stepSize = featureSize;
        double scale = 1.0 / w;
        double scaleMod = 1.0;
        do {
            final int halfStep = stepSize / 2;
            for (int y2 = 0; y2 < w; y2 += stepSize) {
                for (int x2 = 0; x2 < w; x2 += stepSize) {
                    final double a = this.sample(x2, y2);
                    final double b = this.sample(x2 + stepSize, y2);
                    final double c = this.sample(x2, y2 + stepSize);
                    final double d = this.sample(x2 + stepSize, y2 + stepSize);
                    final double e = (a + b + c + d) / 4.0 + (LevelGen.random.nextFloat() * 2.0f - 1.0f) * stepSize * scale;
                    this.setSample(x2 + halfStep, y2 + halfStep, e);
                }
            }
            for (int y2 = 0; y2 < w; y2 += stepSize) {
                for (int x2 = 0; x2 < w; x2 += stepSize) {
                    final double a = this.sample(x2, y2);
                    final double b = this.sample(x2 + stepSize, y2);
                    final double c = this.sample(x2, y2 + stepSize);
                    final double d = this.sample(x2 + halfStep, y2 + halfStep);
                    final double e = this.sample(x2 + halfStep, y2 - halfStep);
                    final double f = this.sample(x2 - halfStep, y2 + halfStep);
                    final double H = (a + b + d + e) / 4.0 + (LevelGen.random.nextFloat() * 2.0f - 1.0f) * stepSize * scale * 0.5;
                    final double g = (a + c + d + f) / 4.0 + (LevelGen.random.nextFloat() * 2.0f - 1.0f) * stepSize * scale * 0.5;
                    this.setSample(x2 + halfStep, y2, H);
                    this.setSample(x2, y2 + halfStep, g);
                }
            }
            stepSize /= 2;
            scale *= scaleMod + 0.8;
            scaleMod *= 0.3;
        } while (stepSize > 1);
    }
    
    private double sample(final int x, final int y) {
        return this.values[(x & this.w - 1) + (y & this.h - 1) * this.w];
    }
    
    private void setSample(final int x, final int y, final double value) {
        this.values[(x & this.w - 1) + (y & this.h - 1) * this.w] = value;
    }
    
    public static byte[][] createAndValidateTopMap(final int w, final int h) {
        final int attempt = 0;
        byte[][] result;
        while (true) {
            result = createTopMap(w, h);
            final int[] count = new int[256];
            for (int i = 0; i < w * h; ++i) {
                final int[] array = count;
                final int n = result[0][i] & 0xFF;
                ++array[n];
            }
            if (count[Tile.rock.id & 0xFF] < 100) {
                continue;
            }
            if (count[Tile.sand.id & 0xFF] < 100) {
                continue;
            }
            if (count[Tile.grass.id & 0xFF] < 100) {
                continue;
            }
            if (count[Tile.tree.id & 0xFF] < 100) {
                continue;
            }
            if (count[Tile.stairsDown.id & 0xFF] < 4) {
                continue;
            }
            if (count[Tile.elm.id & 0xFF] < 2) {
                continue;
            }
            if (count[Tile.pine.id & 0xFF] < 2) {
                continue;
            }
            if (count[Tile.shroom.id & 0xFF] < 2) {
                continue;
            }
            if (count[Tile.bElm.id & 0xFF] < 2) {
                continue;
            }
            if (count[Tile.bPine.id & 0xFF] < 2) {
                continue;
            }
            if (count[Tile.bShroom.id & 0xFF] < 2) {
                continue;
            }
            break;
        }
        return result;
    }
    
    public static byte[][] createAndValidateUndergroundMap(final int w, final int h, final int depth) {
        final int attempt = 0;
        byte[][] result;
        while (true) {
            result = createUndergroundMap(w, h, depth);
            final int[] count = new int[256];
            for (int i = 0; i < w * h; ++i) {
                final int[] array = count;
                final int n = result[0][i] & 0xFF;
                ++array[n];
            }
            if (count[Tile.rock.id & 0xFF] < 100) {
                continue;
            }
            if (count[Tile.dirt.id & 0xFF] < 100) {
                continue;
            }
            if (count[(Tile.ironOre.id & 0xFF) + depth - 1] < 20) {
                continue;
            }
            if (depth < 3 && count[Tile.stairsDown.id & 0xFF] < 6) {
                continue;
            }
            break;
        }
        return result;
    }
    
    public static byte[][] createAndValidateSkyMap(final int w, final int h) {
        final int attempt = 0;
        byte[][] result;
        while (true) {
            result = createSkyMap(w, h);
            final int[] count = new int[256];
            for (int i = 0; i < w * h; ++i) {
                final int[] array = count;
                final int n = result[0][i] & 0xFF;
                ++array[n];
            }
            if (count[Tile.cloud.id & 0xFF] < 2000) {
                continue;
            }
            if (count[Tile.stairsDown.id & 0xFF] < 2) {
                continue;
            }
            break;
        }
        return result;
    }
    
    private static byte[][] createTopMap(final int w, final int h) {
        final LevelGen mnoise1 = new LevelGen(w, h, 16);
        final LevelGen mnoise2 = new LevelGen(w, h, 16);
        final LevelGen mnoise3 = new LevelGen(w, h, 16);
        final LevelGen noise1 = new LevelGen(w, h, 32);
        final LevelGen noise2 = new LevelGen(w, h, 32);
        final byte[] map = new byte[w * h];
        final byte[] data = new byte[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                final int i = x + y * w;
                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3.0 - 2.0;
                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3.0 - 2.0;
                double xd = x / (w - 1.0) * 2.0 - 1.0;
                double yd = y / (h - 1.0) * 2.0 - 1.0;
                if (xd < 0.0) {
                    xd = -xd;
                }
                if (yd < 0.0) {
                    yd = -yd;
                }
                double dist = (xd >= yd) ? xd : yd;
                dist *= dist * dist * dist;
                dist *= dist * dist * dist;
                val = val + 1.0 - dist * 20.0;
                if (val < -0.5) {
                    map[i] = Tile.water.id;
                }
                else if (val > 0.5 && mval < -1.5) {
                    map[i] = Tile.rock.id;
                }
                else {
                    map[i] = Tile.grass.id;
                }
            }
        }
        for (int j = 0; j < w * h / 2800; ++j) {
            final int xs = LevelGen.random.nextInt(w);
            final int ys = LevelGen.random.nextInt(h);
            for (int k = 0; k < 10; ++k) {
                final int x2 = xs + LevelGen.random.nextInt(21) - 10;
                final int y2 = ys + LevelGen.random.nextInt(21) - 10;
                for (int l = 0; l < 100; ++l) {
                    final int xo = x2 + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5);
                    for (int yo = y2 + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5), yy = yo - 1; yy <= yo + 1; ++yy) {
                        for (int xx = xo - 1; xx <= xo + 1; ++xx) {
                            if (xx >= 0 && yy >= 0 && xx < w && yy < h && map[xx + yy * w] == Tile.grass.id) {
                                map[xx + yy * w] = Tile.sand.id;
                            }
                        }
                    }
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 200; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.tree.id;
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 10; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.elm.id;
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 10; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.pine.id;
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 2; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.bPine.id;
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 2; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.bElm.id;
                }
            }
        }
        for (int j = 0; j < w * h / 800; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 2; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.bShroom.id;
                }
            }
        }
        for (int j = 0; j < w * h / 800; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            for (int m = 0; m < 2; ++m) {
                final int xx2 = x + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                final int yy2 = y3 + LevelGen.random.nextInt(15) - LevelGen.random.nextInt(15);
                if (xx2 >= 0 && yy2 >= 0 && xx2 < w && yy2 < h && map[xx2 + yy2 * w] == Tile.grass.id) {
                    map[xx2 + yy2 * w] = Tile.shroom.id;
                }
            }
        }
        for (int j = 0; j < w * h / 400; ++j) {
            final int x = LevelGen.random.nextInt(w);
            final int y3 = LevelGen.random.nextInt(h);
            final int col = LevelGen.random.nextInt(4);
            for (int j2 = 0; j2 < 30; ++j2) {
                final int xx3 = x + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5);
                final int yy3 = y3 + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5);
                if (xx3 >= 0 && yy3 >= 0 && xx3 < w && yy3 < h && map[xx3 + yy3 * w] == Tile.grass.id) {
                    map[xx3 + yy3 * w] = Tile.flower.id;
                    data[xx3 + yy3 * w] = (byte)(col + LevelGen.random.nextInt(4) * 16);
                }
            }
        }
        for (int j = 0; j < w * h / 100; ++j) {
            final int xx4 = LevelGen.random.nextInt(w);
            final int yy4 = LevelGen.random.nextInt(h);
            if (xx4 >= 0 && yy4 >= 0 && xx4 < w && yy4 < h && map[xx4 + yy4 * w] == Tile.sand.id) {
                map[xx4 + yy4 * w] = Tile.cactus.id;
            }
        }
        int count = 0;
        int i2 = 0;
    Label_2106_Outer:
        while (i2 < w * h / 100) {
            final int x3 = LevelGen.random.nextInt(w - 2) + 1;
            final int y4 = LevelGen.random.nextInt(h - 2) + 1;
            int yy5 = y4 - 1;
        Label_2106:
            while (true) {
                while (yy5 <= y4 + 1) {
                    for (int xx3 = x3 - 1; xx3 <= x3 + 1; ++xx3) {
                        if (map[xx3 + yy5 * w] != Tile.rock.id) {
                            break Label_2106;
                        }
                    }
                    ++yy5;
                    continue Label_2106_Outer;
                    ++i2;
                    continue Label_2106_Outer;
                }
                map[x3 + y4 * w] = Tile.stairsDown.id;
                if (++count == 4) {
                    break;
                }
                continue Label_2106;
            }
        }
        return new byte[][] { map, data };
    }
    
    private static byte[][] createUndergroundMap(final int w, final int h, final int depth) {
        final LevelGen mnoise1 = new LevelGen(w, h, 16);
        final LevelGen mnoise2 = new LevelGen(w, h, 16);
        final LevelGen mnoise3 = new LevelGen(w, h, 16);
        final LevelGen nnoise1 = new LevelGen(w, h, 16);
        final LevelGen nnoise2 = new LevelGen(w, h, 16);
        final LevelGen nnoise3 = new LevelGen(w, h, 16);
        final LevelGen wnoise1 = new LevelGen(w, h, 16);
        final LevelGen wnoise2 = new LevelGen(w, h, 16);
        final LevelGen wnoise3 = new LevelGen(w, h, 16);
        final LevelGen noise1 = new LevelGen(w, h, 32);
        final LevelGen noise2 = new LevelGen(w, h, 32);
        final byte[] map = new byte[w * h];
        final byte[] data = new byte[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                final int i = x + y * w;
                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3.0 - 2.0;
                double mval = Math.abs(mnoise1.values[i] - mnoise2.values[i]);
                mval = Math.abs(mval - mnoise3.values[i]) * 3.0 - 2.0;
                double nval = Math.abs(nnoise1.values[i] - nnoise2.values[i]);
                nval = Math.abs(nval - nnoise3.values[i]) * 3.0 - 2.0;
                double wval = Math.abs(wnoise1.values[i] - wnoise2.values[i]);
                wval = Math.abs(nval - wnoise3.values[i]) * 3.0 - 2.0;
                double xd = x / (w - 1.0) * 2.0 - 1.0;
                double yd = y / (h - 1.0) * 2.0 - 1.0;
                if (xd < 0.0) {
                    xd = -xd;
                }
                if (yd < 0.0) {
                    yd = -yd;
                }
                double dist = (xd >= yd) ? xd : yd;
                dist *= dist * dist * dist;
                dist *= dist * dist * dist;
                val = val + 1.0 - dist * 20.0;
                if (val > -2.0 && wval < -2.0 + depth / 2 * 3) {
                    if (depth > 2) {
                        map[i] = Tile.lava.id;
                    }
                    else {
                        map[i] = Tile.water.id;
                    }
                }
                else if (val > -2.0 && (mval < -1.7 || nval < -1.4)) {
                    map[i] = Tile.dirt.id;
                }
                else {
                    map[i] = Tile.rock.id;
                }
            }
        }
        final int r = 2;
        for (int j = 0; j < w * h / 400; ++j) {
            final int x2 = LevelGen.random.nextInt(w);
            final int y2 = LevelGen.random.nextInt(h);
            for (int k = 0; k < 30; ++k) {
                final int xx = x2 + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5);
                final int yy = y2 + LevelGen.random.nextInt(5) - LevelGen.random.nextInt(5);
                if (xx >= r && yy >= r && xx < w - r && yy < h - r && map[xx + yy * w] == Tile.rock.id) {
                    map[xx + yy * w] = (byte)((Tile.ironOre.id & 0xFF) + depth - 1);
                }
            }
        }
        if (depth < 3) {
            int count = 0;
            int j = 0;
        Label_0895_Outer:
            while (j < w * h / 100) {
                final int x2 = LevelGen.random.nextInt(w - 20) + 10;
                final int y2 = LevelGen.random.nextInt(h - 20) + 10;
                int yy2 = y2 - 1;
            Label_0895:
                while (true) {
                    while (yy2 <= y2 + 1) {
                        for (int xx = x2 - 1; xx <= x2 + 1; ++xx) {
                            if (map[xx + yy2 * w] != Tile.rock.id) {
                                break Label_0895;
                            }
                        }
                        ++yy2;
                        continue Label_0895_Outer;
                        ++j;
                        continue Label_0895_Outer;
                    }
                    map[x2 + y2 * w] = Tile.stairsDown.id;
                    if (++count == 6) {
                        break;
                    }
                    continue Label_0895;
                }
            }
        }
        return new byte[][] { map, data };
    }
    
    private static byte[][] createSkyMap(final int w, final int h) {
        final LevelGen noise1 = new LevelGen(w, h, 8);
        final LevelGen noise2 = new LevelGen(w, h, 8);
        final byte[] map = new byte[w * h];
        final byte[] data = new byte[w * h];
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                final int i = x + y * w;
                double val = Math.abs(noise1.values[i] - noise2.values[i]) * 3.0 - 2.0;
                double xd = x / (w - 1.0) * 2.0 - 1.0;
                double yd = y / (h - 1.0) * 2.0 - 1.0;
                if (xd < 0.0) {
                    xd = -xd;
                }
                if (yd < 0.0) {
                    yd = -yd;
                }
                double dist = (xd >= yd) ? xd : yd;
                dist *= dist * dist * dist;
                dist *= dist * dist * dist;
                val = -val * 1.0 - 2.2;
                val = val + 1.0 - dist * 20.0;
                if (val < -0.25) {
                    map[i] = Tile.infiniteFall.id;
                }
                else {
                    map[i] = Tile.cloud.id;
                }
            }
        }
        int j = 0;
    Label_0374_Outer:
        while (j < w * h / 50) {
            final int x = LevelGen.random.nextInt(w - 2) + 1;
            final int y2 = LevelGen.random.nextInt(h - 2) + 1;
            int yy = y2 - 1;
        Label_0374:
            while (true) {
                while (yy <= y2 + 1) {
                    for (int xx = x - 1; xx <= x + 1; ++xx) {
                        if (map[xx + yy * w] != Tile.cloud.id) {
                            break Label_0374;
                        }
                    }
                    ++yy;
                    continue Label_0374_Outer;
                    ++j;
                    continue Label_0374_Outer;
                }
                map[x + y2 * w] = Tile.cloudCactus.id;
                continue Label_0374;
            }
        }
        int count = 0;
        int k = 0;
    Label_0515_Outer:
        while (k < w * h) {
            final int x2 = LevelGen.random.nextInt(w - 2) + 1;
            final int y3 = LevelGen.random.nextInt(h - 2) + 1;
            int yy2 = y3 - 1;
        Label_0515:
            while (true) {
                while (yy2 <= y3 + 1) {
                    for (int xx2 = x2 - 1; xx2 <= x2 + 1; ++xx2) {
                        if (map[xx2 + yy2 * w] != Tile.cloud.id) {
                            break Label_0515;
                        }
                    }
                    ++yy2;
                    continue Label_0515_Outer;
                    ++k;
                    continue Label_0515_Outer;
                }
                map[x2 + y3 * w] = Tile.stairsDown.id;
                if (++count == 2) {
                    break;
                }
                continue Label_0515;
            }
        }
        return new byte[][] { map, data };
    }
}
