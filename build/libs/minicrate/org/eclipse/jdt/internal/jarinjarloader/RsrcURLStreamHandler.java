// 
// Decompiled by Procyon v0.6-prerelease
// 

package org.eclipse.jdt.internal.jarinjarloader;

import java.io.IOException;
import java.net.URLConnection;
import java.net.URL;
import java.net.URLStreamHandler;

public class RsrcURLStreamHandler extends URLStreamHandler
{
    private ClassLoader classLoader;
    
    public RsrcURLStreamHandler(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }
    
    protected URLConnection openConnection(final URL u) throws IOException {
        return new RsrcURLConnection(u, this.classLoader);
    }
    
    protected void parseURL(final URL url, final String spec, final int start, final int limit) {
        String file;
        if (spec.startsWith("rsrc:")) {
            file = spec.substring(5);
        }
        else if (url.getFile().equals("./")) {
            file = spec;
        }
        else if (url.getFile().endsWith("/")) {
            file = String.valueOf(url.getFile()) + spec;
        }
        else {
            file = spec;
        }
        this.setURL(url, "rsrc", "", -1, null, null, file, null, null);
    }
}
