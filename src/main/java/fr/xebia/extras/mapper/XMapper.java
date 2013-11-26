package fr.xebia.extras.mapper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class XMapper {

    private static final Map<String, Object> mappers = new ConcurrentHashMap<>();

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath and instantiate it with default factory.
     *
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @return              A new Mapper instance or previously instantiated mapper
     *
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public static <T> T getMapper(Class<T> mapperClass) throws IllegalArgumentException {
         return getMapper(mapperClass, null);
    }

    /**
     * Mapper Builder DSL for those who like it like that.
     * @param mapperClass   The Mapper interface class
     * @param <T>           The Mapper interface itself
     * @return              Builder for Mapper
     */
    public static <T> XMapperFactoryBuilder<T> mapper(Class<T> mapperClass) {

        return new XMapperFactoryBuilder<T>(mapperClass);
    }

    /**
     * Retrieve the generated Mapper for the corresponding interface in the classpath.
     *
     * @param mapperClass   The Mapper interface class
     * @param factory       The factory to be used for bean instantiation or null for default
     * @param <T>           The Mapper interface itself
     * @return              A new Mapper instance or previously instantiated mapper
     *
     * @throws IllegalArgumentException If for some reason the Mapper class can not be loaded or instantiated
     */
    public synchronized static <T> T getMapper(Class<T> mapperClass, Factory factory) throws IllegalArgumentException {

        final String mapperKey = String.format("%s-%s", mapperClass.getCanonicalName(), factory);
        if (!mappers.containsKey(mapperKey)) {


            // Check that
            // - clazz is an interface
            // - the implementation type implements clazz
            // - clazz is annotated with @Mapper
            //
            // Use privileged action
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            if ( classLoader == null ) {
                classLoader = XMapper.class.getClassLoader();
            }

            @SuppressWarnings("unchecked")
            T mapperInstance = null;
            try {

                Class<T> mapperImpl  = (Class<T>) classLoader.loadClass(mapperClass.getCanonicalName() + XMapperConstants.MAPPER_CLASS_SUFFIX);
                if(factory != null){
                     mapperInstance =  mapperImpl.getDeclaredConstructor(Factory.class).newInstance(factory);
                } else {
                    mapperInstance = (T) mapperImpl.newInstance();
                }
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed (No constructor with Factory parameter !) : %s", mapperClass.getName(), e.getMessage()), e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(String.format("Instantiation of Mapper class %s failed (No constructor with Factory parameter !) : %s", mapperClass.getName(), e.getMessage()), e);
            }

            mappers.put(mapperKey, mapperInstance);
            return mapperInstance;
        }

        return (T) mappers.get(mapperKey);
    }


    public static final class XMapperFactoryBuilder<T> {
        private final Class<T> mapperClass;

        public XMapperFactoryBuilder(Class<T> mapperClass) {
            this.mapperClass = mapperClass;
        }

        public T build(){
            return XMapper.getMapper(mapperClass);
        }

        public XMapperFactoredBuilder<T> withFactory(Factory factory){
            return new XMapperFactoredBuilder<T>(mapperClass, factory);
        }
    }

    public static class XMapperFactoredBuilder<T> {
        private final Class<T> mapperClass;
        private final Factory factory;

        public XMapperFactoredBuilder(Class<T> mapperClass, Factory factory) {
            this.mapperClass = mapperClass;
            this.factory = factory;
        }

        public T build(){
            return XMapper.getMapper(mapperClass, factory);
        }
    }

}