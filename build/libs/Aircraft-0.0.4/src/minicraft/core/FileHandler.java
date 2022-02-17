package minicraft.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import minicraft.saveload.Save;

public class FileHandler extends Game {
    private FileHandler() {}

    public static final int REPLACE_EXISTING = 0;
    public static final int RENAME_COPY = 1;
    public static final int SKIP = 2;

    static final String OS;
    private static final String localGameDir;
    static final String systemGameDir;

    static {
        OS = System.getProperty("os.name").toLowerCase();
        String local = "playminicraft/mods/Aircraft";

        if (OS.contains("windows")) { // Windows filesystem
            systemGameDir = System.getenv("APPDATA");

        } else {
            systemGameDir = System.getProperty("user.home");
            if (!OS.contains("mac")) { // Linux and Mac filesystem
                local = "." + local;
            }
        }

        localGameDir = "/" + local;

        if (Game.debug) {
            System.out.println("OS name: \"" + OS + "\"");
        }
        if (Game.debug) {
            System.out.println("System game dir: " + systemGameDir);
        }

    }

    public static String getSystemGameDir() {
        return systemGameDir;
    }

    public static String getLocalGameDir() {
        return localGameDir;
    }

    static void determineGameDir(String saveDir) {
        gameDir = saveDir + localGameDir;

        if (debug) {
            System.out.println("Determined gameDir: " + gameDir);
        }

        File testFile = new File(gameDir);
        testFile.mkdirs();

        File oldFolder = new File(saveDir + "/.playminicraft/mods/Aircraft");
        if (oldFolder.exists() && !oldFolder.equals(testFile)) {
            try {
                copyFolderContents(oldFolder.toPath(), testFile.toPath(), RENAME_COPY, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (OS.contains("mac")) {
            oldFolder = new File(saveDir + "/.playminicraft");
            if (oldFolder.exists()) {
                try {
                    copyFolderContents(oldFolder.toPath(), testFile.toPath(), RENAME_COPY, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void deleteFolder(File top) {
        if (top == null) {
            return;
        }
        if (top.isDirectory()) {
            File[] subfiles = top.listFiles();

            if (subfiles != null) {
                for (File subfile: subfiles) {
                    deleteFolder(subfile);
                }
            }
        }
        // noinspection ResultOfMethodCallIgnored
        top.delete();
    }

    public static void copyFolderContents(Path origFolder, Path newFolder, int ifExisting, boolean deleteOriginal)
    throws IOException {
        
        // I can determine the local folder structure with origFolder.relativize(file),
        // then use newFolder.resolve(relative).
        // if (Game.debug) System.out.println("Copying contents of folder " + origFolder
        // + " to new folder " + newFolder);

        Files.walkFileTree(origFolder, new FileVisitor < Path > () {
            public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

                String newFilename = newFolder.resolve(origFolder.relativize(file)).toString();

                if (new File(newFilename).exists()) {
                    if (ifExisting == SKIP) {
                        return FileVisitResult.CONTINUE;

                    } else if (ifExisting == RENAME_COPY) {
                        newFilename = newFilename.substring(0, newFilename.lastIndexOf("."));
                        do {
                            newFilename += "(Old)";
                        } while (new File(newFilename).exists());
                        newFilename += Save.extension;
                    }
                }

                Path newFile = new File(newFilename).toPath();

                if (Game.debug) {
                    System.out.println("Visiting file " + file + "; translating to " + newFile);
                }

                try {
                    Files.copy(file, newFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult preVisitDirectory(Path p, BasicFileAttributes bfa) {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult postVisitDirectory(Path p, IOException ex) {
                return FileVisitResult.CONTINUE;
            }

            public FileVisitResult visitFileFailed(Path p, IOException ex) {
                return FileVisitResult.CONTINUE;
            }
        });

        if (deleteOriginal) {
            deleteFolder(origFolder.toFile());
        }
    }

}