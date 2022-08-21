package minicraft.mods.coremods;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import minicraft.mods.ModContainer;

public class ModLoaderCommunication {
	/** The AppClassLoader of Game. */
	private static final ClassLoader PARENT = ModLoaderCommunication.class.getClassLoader().getParent().getParent().getParent();

	/**
	 * Getting the list of mods from the mod loader.
	 * @return The lsit of mods.
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<ModContainer> getModList() {
		try {
			return new ArrayList<>((ArrayList<ModContainer>) PARENT.loadClass("minicraft.mods.Mods").getDeclaredField("mods").get(null));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException
				| ClassNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	/**
	 * Invoking a static method with the specified class, method and params.
	 * The method specified must be static.
	 * @param className The binary name of the class.
	 * @param methodName The name of the method.
	 * @param paramTypes The parameter array.
	 * @param params The arguments used for the method call.
	 * @throws	IllegalAccessException
	 * 			If this {@code Method} object is enforcing Java language
	 * 			access control and the underlying method is inaccessible.
	 * @throws	IllegalArgumentException
	 * 			If the method is an instance method and the specified
	 * 			object argument is not an instance of the class or interface
	 * 			declaring the underlying method (or of a subclass or implementor thereof);
	 * 			if the number of actual and formal parameters differ;
	 * 			if an unwrapping conversion for primitive arguments fails;
	 * 			or if, after possible unwrapping, a parameter value cannot be converted
	 * 			to the corresponding formal parameter type by a method invocation conversion.
	 * @throws	InvocationTargetException If the underlying method throws an exception.
	 * @throws	NoSuchMethodException If a matching method is not found.
	 * @throws	SecurityException
	 * 			If a security manager, <i>s</i>, is present and any of the
     *          following conditions is met:
     *
     *          <ul>
     *
     *          <li> the caller's class loader is not the same as the
     *          class loader of this class and invocation of
     *          {@link SecurityManager#checkPermission
     *          s.checkPermission} method with
     *          {@code RuntimePermission("accessDeclaredMembers")}
     *          denies access to the declared method
     *
     *          <li> the caller's class loader is not the same as or an
     *          ancestor of the class loader for the current class and
     *          invocation of {@link SecurityManager#checkPackageAccess
     *          s.checkPackageAccess()} denies access to the package
     *          of this class
     *
     *          </ul>
	 *
	 * @throws	ClassNotFoundException If the class was not found.
	 */
	public static void invokeVoid0(String className, String methodName, Class<?>[] paramTypes, Object[] params)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		PARENT.loadClass(className).getDeclaredMethod(methodName, paramTypes).invoke(null, params);
	}

	/**
	 * Invoking a static method with the specified class and method without any parameter.
	 * The method specified must be static.
	 * @param className The binary name of the class.
	 * @param methodName The name of the method.
	 * @throws	IllegalAccessException
	 * 			If this {@code Method} object is enforcing Java language
	 * 			access control and the underlying method is inaccessible.
	 * @throws	IllegalArgumentException
	 * 			If the method is an instance method and the specified
	 * 			object argument is not an instance of the class or interface
	 * 			declaring the underlying method (or of a subclass or implementor thereof);
	 * 			if the number of actual and formal parameters differ;
	 * 			if an unwrapping conversion for primitive arguments fails;
	 * 			or if, after possible unwrapping, a parameter value cannot be converted
	 * 			to the corresponding formal parameter type by a method invocation conversion.
	 * @throws	InvocationTargetException If the underlying method throws an exception.
	 * @throws	NoSuchMethodException If a matching method is not found.
	 * @throws	SecurityException
	 * 			If a security manager, <i>s</i>, is present and any of the
     *          following conditions is met:
     *
     *          <ul>
     *
     *          <li> the caller's class loader is not the same as the
     *          class loader of this class and invocation of
     *          {@link SecurityManager#checkPermission
     *          s.checkPermission} method with
     *          {@code RuntimePermission("accessDeclaredMembers")}
     *          denies access to the declared method
     *
     *          <li> the caller's class loader is not the same as or an
     *          ancestor of the class loader for the current class and
     *          invocation of {@link SecurityManager#checkPackageAccess
     *          s.checkPackageAccess()} denies access to the package
     *          of this class
     *
     *          </ul>
	 *
	 * @throws	ClassNotFoundException If the class was not found.
	 */
	public static void invokeVoid(String className, String methodName)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		invokeVoid0(className, methodName, new Class<?>[0], new Object[0]);
	}

	/**
	 * Getting the static field with the specified class and field name.
	 * @param className The binary name of the class.
	 * @param fieldName The name of the field
     * @return  the {@code Field} object for the specified field in this
     *          class
     * @throws  NoSuchFieldException if a field with the specified name is
     *          not found.
     * @throws  NullPointerException if {@code name} is {@code null}
     * @throws  SecurityException
     *          If a security manager, <i>s</i>, is present and any of the
     *          following conditions is met:
     *
     *          <ul>
     *
     *          <li> the caller's class loader is not the same as the
     *          class loader of this class and invocation of
     *          {@link SecurityManager#checkPermission
     *          s.checkPermission} method with
     *          {@code RuntimePermission("accessDeclaredMembers")}
     *          denies access to the declared field
     *
     *          <li> the caller's class loader is not the same as or an
     *          ancestor of the class loader for the current class and
     *          invocation of {@link SecurityManager#checkPackageAccess
     *          s.checkPackageAccess()} denies access to the package
     *          of this class
     *
     *          </ul>
	 * 
	 * @throws ClassNotFoundException If the class was not found.
	 */
	public static Field getField(String className, String fieldName)
			throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		return PARENT.loadClass(className).getDeclaredField(fieldName);
	}

	/**
	 * Creating a new instance with the specified class and params.
	 * @param className The binary name of the class.
	 * @param paramTypes The parameter array.
     * @param initargs Array of objects to be passed as arguments to
     * the constructor call; values of primitive types are wrapped in
     * a wrapper object of the appropriate type (e.g. a {@code float}
     * in a {@link java.lang.Float Float})
	 * @return A new object created by calling the constructor this object represents
	 * @throws	InstantiationException if the class that declares the
     * 			underlying constructor represents an abstract class.
	 * @throws	IllegalAccessException if this {@code Constructor} object
     * 			is enforcing Java language access control and the underlying
     * 			constructor is inaccessible.
	 * @throws	IllegalArgumentException if the number of actual
     * 			and formal parameters differ; if an unwrapping
     * 			conversion for primitive arguments fails; or if,
     * 			after possible unwrapping, a parameter value
     * 			cannot be converted to the corresponding formal
     * 			parameter type by a method invocation conversion; if
     * 			this constructor pertains to an enum type.
	 * @throws	InvocationTargetException if the underlying constructor
     * 			throws an exception.
     * @throws NoSuchMethodException if a matching method is not found.
     * @throws SecurityException
     * 			If a security manager, <i>s</i>, is present and
     * 			the caller's class loader is not the same as or an
     * 			ancestor of the class loader for the current class and
     * 			invocation of {@link SecurityManager#checkPackageAccess
     * 			s.checkPackageAccess()} denies access to the package
     * 			of this class.
	 * @throws ClassNotFoundException If the class was not found.
	 */
	public static Object createInstance(String className, Class<?>[] paramTypes, Object[] initargs)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
				NoSuchMethodException, SecurityException, ClassNotFoundException {
		return PARENT.loadClass(className).getConstructor(paramTypes).newInstance(initargs);
	}
}
