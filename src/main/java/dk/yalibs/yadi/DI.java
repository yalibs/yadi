package dk.yalibs.yadi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A static store of object instances and supplier functions.
 */
public class DI {
    private static Map<Class<?>,Object> dependencies = new HashMap<>();
    private static Map<Class<?>,Supplier<Object>> suppliers = new HashMap<>();
    private static Map<String,Object> namedDependencies = new HashMap<>();
    private static Map<String,Supplier<Object>> namedSuppliers = new HashMap<>();
    private static boolean isLocked = false;

    /**
     * Add an object to the store.
     * Note that if a key is added twice, it will simply overwrite the old store entry.
     * @param <T>
     * @param key class key, used for lookup later
     * @param obj the object instance to store
     */
    public static <T> void add(Class<? extends T> key, T obj) throws DIAddException {
        if(isLocked)
            throw new DIAddException("Cannot add with key '%s' because DI store has been locked".formatted(key.toString()));
        dependencies.put(key, obj);
    }

    /**
     * Add a supplier function to the store.
     * The supplier function will be called every time a resource with the appropriate key is requested.
     * Note that if a key is added twice, it will simply overwrite the old store entry.
     * @param <T>
     * @param key class key used for lookup later
     * @param supplier function that can create objects
     */
    public static <T> void add(Class<? extends T> key, Supplier<? extends T> supplier) {
        if(isLocked)
            throw new DIAddException("Cannot add supplier with key '%s' because DI store has been locked".formatted(key.toString()));
        suppliers.put(key, supplier);
    }

    /**
     * Add a named object to the store.
     * It is recommended to use {@link #add(Class, Object) add} instead, since strings can be unreliable and difficult to keep track of
     * Note that if a key is added twice, it will simply overwrite the old store entry.
     * @param <T>
     * @param key string name of the object
     * @param obj the object instance to store
     */
    public static <T> void add(String key, T obj) {
        if(isLocked)
            throw new DIAddException("Cannot add object with key '%s' because DI store has been locked".formatted(key));
        namedDependencies.put(key, obj);
    }

    /**
     * Add a named supplier function to the store
     * It is recommended to use {@link #add(Class, Supplier) add} instead, since strings can be unreliable and difficult to keep track of
     * Note that if a key is added twice, it will simply overwrite the old store entry.
     * @param key string name of the object
     * @param supplier function that can create objects
     */
    public static void add(String key, Supplier<Object> supplier) {
        if(isLocked)
            throw new DIAddException("Cannot add supplier with key '%s' because DI store has been locked".formatted(key));
        namedSuppliers.put(key, supplier);
    }

    /**
     * Locks the global store so no more additions can be done. This may be useful to enforce read-only from some point on.
     * Note that {@link DI} does not provide an 'unlock' feature, so be mindful of when you lock the store.
     */
    public static void lock() {
        isLocked = true;
    }

    /**
     * Get an injected object based off of a class key.
     * The object will be freshly created if a supplier is associated with the key.
     * @param <T>
     * @param key the key to lookup in the store
     * @return the object from the store associated with the key
     * @throws NullPointerException is thrown if the key is not available in the store
     */
    public static <T> T get(Class<? super T> key) throws NullPointerException {
        if(dependencies.containsKey(key))
            return (T)dependencies.get(key);
        if(suppliers.containsKey(key))
            return (T)suppliers.get(key).get();
        throw new NullPointerException("DI unable to resolve class '%s'".formatted(key.getName()));
    }

    /**
     * Get an injected object based off a name key.
     * The object will be freshly created if a supplier is associated with the key.
     * @param <T>
     * @param key
     * @return
     * @throws NullPointerException
     */
    public static <T> T get(String key) throws NullPointerException {
        if(namedDependencies.containsKey(key))
            return (T)namedDependencies.get(key);
        if(namedSuppliers.containsKey(key))
            return (T)namedSuppliers.get(key).get();
        throw new NullPointerException("DI unable to resolve named dependency '%s'".formatted(key));
    }

    /**
     * Checks if the store contains the provided class key
     * @param <T>
     * @param key class key of the object
     * @return true if either an appropriate object instance, or a supplier has been injected
     */
    public static <T> boolean contains(Class<? super T> key) {
        if(dependencies.containsKey(key))
            return true;
        if(suppliers.containsKey(key))
            return true;
        return false;
    }

    /**
     * Checks if the store contains the provided name key
     * @param key string name of the object
     * @return true if either an appropriate object instance, or a supplier has been injected
     */
    public static boolean contains(String key) {
        if(namedDependencies.containsKey(key))
            return true;
        if(namedSuppliers.containsKey(key))
            return true;
        return false;
    }
}

