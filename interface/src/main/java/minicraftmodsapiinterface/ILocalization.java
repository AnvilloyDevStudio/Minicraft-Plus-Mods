package minicraftmodsapiinterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public abstract class ILocalization {
    /**
     * Must call this first
     * @param l Localization.class
     */
    public static void init(Class<? extends ILocalization> l) {
        try {
            changeLang = l.getMethod("changeLanguage", String.class);
            getLocal = l.getMethod("getLocalized", String.class);
            getSelectLocal = l.getMethod("getSelectedLocale", new Class[0]);
            getSelectLang = l.getMethod("getSelectedLanguage", new Class[0]);
            getLangs = l.getMethod("getLanguages", new Class[0]);
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private static Method changeLang;
    private static Method getLocal;
    private static Method getSelectLocal;
    private static Method getSelectLang;
    private static Method getLangs;
	public static String getLocalized(String key) {
		try {
            return (String)getLocal.invoke(null, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
	}
	
	public static Locale getSelectedLocale() {
        try {
            return (Locale)getSelectLocal.invoke(null, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
	
	public static String getSelectedLanguage() {
        try {
            return (String)getSelectLang.invoke(null, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public static void changeLanguage(String newLanguage) {
		try {
            changeLang.invoke(null, newLanguage);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

	public static String[] getLanguages() {
        try {
            return (String[])getLangs.invoke(null, new Object[0]);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
