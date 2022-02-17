// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.eclipse.jdt.internal.jarinjarloader;

import java.util.List;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.Manifest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.net.URL;

public class JarRsrcLoader
{
    public static void main(final String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IOException {
        final ManifestInfo mi = getManifestInfo();
        final ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL.setURLStreamHandlerFactory(new RsrcURLStreamHandlerFactory(cl));
        final URL[] rsrcUrls = new URL[mi.rsrcClassPath.length];
        for (int i = 0; i < mi.rsrcClassPath.length; ++i) {
            final String rsrcPath = mi.rsrcClassPath[i];
            if (rsrcPath.endsWith("/")) {
                rsrcUrls[i] = new URL("rsrc:" + rsrcPath);
            }
            else {
                rsrcUrls[i] = new URL("jar:rsrc:" + rsrcPath + "!/");
            }
        }
        final ClassLoader jceClassLoader = new URLClassLoader(rsrcUrls, (ClassLoader)null);
        Thread.currentThread().setContextClassLoader(jceClassLoader);
        final Class c = Class.forName(mi.rsrcMainClass, true, jceClassLoader);
        final Method main = c.getMethod("main", args.getClass());
        main.invoke(null, args);
    }
    
    private static ManifestInfo getManifestInfo() throws IOException {
        final Enumeration resEnum = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resEnum.hasMoreElements()) {
            try {
                final URL url = resEnum.nextElement();
                final InputStream is = url.openStream();
                if (is == null) {
                    continue;
                }
                final ManifestInfo result = new ManifestInfo(null);
                final Manifest manifest = new Manifest(is);
                final Attributes mainAttribs = manifest.getMainAttributes();
                result.rsrcMainClass = mainAttribs.getValue("Rsrc-Main-Class");
                String rsrcCP = mainAttribs.getValue("Rsrc-Class-Path");
                if (rsrcCP == null) {
                    rsrcCP = "";
                }
                result.rsrcClassPath = splitSpaces(rsrcCP);
                if (result.rsrcMainClass != null && !result.rsrcMainClass.trim().equals("")) {
                    return result;
                }
                continue;
            }
            catch (Exception ex) {}
        }
        System.err.println("Missing attributes for JarRsrcLoader in Manifest (Rsrc-Main-Class, Rsrc-Class-Path)");
        return null;
    }
    
    private static String[] splitSpaces(final String line) {
        if (line == null) {
            return null;
        }
        final List result = new ArrayList();
        int lastPos;
        for (int firstPos = 0; firstPos < line.length(); firstPos = lastPos + 1) {
            lastPos = line.indexOf(32, firstPos);
            if (lastPos == -1) {
                lastPos = line.length();
            }
            if (lastPos > firstPos) {
                result.add(line.substring(firstPos, lastPos));
            }
        }
        return result.toArray(new String[result.size()]);
    }
    
    private static class ManifestInfo
    {
        String rsrcMainClass;
        String[] rsrcClassPath;
    }
}
