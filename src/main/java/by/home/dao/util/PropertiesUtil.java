package by.home.dao.util;

import by.home.data.exception.PropertiesLoadException;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static by.home.util.Constant.ExceptionMessage.FAILED_LOAD_DAO_CLASSES;
import static by.home.util.Constant.Utils.DAO_API_PACKAGE_NAME;
import static by.home.util.Constant.Utils.DAO_IMPL_PACKAGE_NAME;
import static by.home.util.Constant.Utils.GET_INSTANCE_METHOD_NAME;
import static by.home.util.Constant.Utils.PROPERTIES_FILE_NAME;
import static by.home.util.Constant.Utils.SINGLETON_CLASS_NAME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();
    private static final Map<Class<?>, Object> DAO_CLASSES = new HashMap<>();

    static {
        loadProperties();
        loadInstanceDaoClasses();
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static Object getDaoClass(Class<?> key) {
        return DAO_CLASSES.get(key);
    }

    private static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new PropertiesLoadException("failed load " + PROPERTIES_FILE_NAME, e);
        }
    }

    private static void loadInstanceDaoClasses() {
        List<String> classNames;
        List<String> interfaceNames;
        try (ScanResult scanDaoImplClassesResult = new ClassGraph()
                .acceptPackages(DAO_IMPL_PACKAGE_NAME)
                .enableClassInfo()
                .scan();
             ScanResult scanDaoInterfacesResult = new ClassGraph()
                     .acceptPackages(DAO_API_PACKAGE_NAME)
                     .enableClassInfo()
                     .scan()) {
            classNames = scanDaoImplClassesResult.getAllClasses().getNames();
            interfaceNames = scanDaoInterfacesResult.getAllClasses().getNames();
            fillDaoClassesMap(classNames, new HashSet<>(interfaceNames));
        }
    }

    private static void fillDaoClassesMap(List<String> classNames, Set<String> interfaceNames) {
        try {
            for (String className : classNames) {
                Class<?> daoClass = Class.forName(className);
                String[] packageNames = className.split("\\.");
                String daoClassName = packageNames[packageNames.length - 1];
                String singletonClassName = String.format(SINGLETON_CLASS_NAME_PATTERN, daoClassName);
                Class<?> singletonClass = Class.forName(singletonClassName);
                Method getInstanceMethod = singletonClass.getDeclaredMethod(GET_INSTANCE_METHOD_NAME);
                Object daoClassInstance = getInstanceMethod.invoke(singletonClass);
                Class<?> daoInterface = Arrays.stream(daoClass.getInterfaces())
                        .filter(x -> interfaceNames.contains(x.getName()))
                        .findFirst()
                        .orElseThrow(() -> new PropertiesLoadException(FAILED_LOAD_DAO_CLASSES));
                DAO_CLASSES.put(daoInterface, daoClassInstance);
            }
        } catch (ReflectiveOperationException e) {
            throw new PropertiesLoadException(FAILED_LOAD_DAO_CLASSES, e);
        }
    }
}
