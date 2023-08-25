package by.home.dao.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static by.home.dao.util.Constant.Utils.DAO_IMPL_PACKAGE_NAME;
import static by.home.dao.util.Constant.Utils.GET_INSTANCE_METHOD_NAME;
import static by.home.dao.util.Constant.Utils.PROPERTIES_FILE_NAME;
import static by.home.dao.util.Constant.Utils.SINGLETON_CLASS_NAME_PATTERN;

@Slf4j
public class PropertiesUtil {

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
            log.error(e.getMessage());
            throw new RuntimeException("failed load " + PROPERTIES_FILE_NAME);
        }
    }

    private static void loadInstanceDaoClasses() {
        List<String> classNames;
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages(DAO_IMPL_PACKAGE_NAME)
                .enableClassInfo()
                .scan()) {
            classNames = scanResult.getAllClasses().getNames();
            fillDaoClassesMap(classNames);
        }
    }

    private static void fillDaoClassesMap(List<String> classNames) {
        try {
            for (String className : classNames) {
                Class<?> daoClass = Class.forName(className);
                String[] packageNames = className.split("\\.");
                String daoClassName = packageNames[packageNames.length - 1];
                String singletonClassName = String.format(SINGLETON_CLASS_NAME_PATTERN, daoClassName);
                Class<?> singletonClass = Class.forName(singletonClassName);
                Method getInstanceMethod = singletonClass.getDeclaredMethod(GET_INSTANCE_METHOD_NAME);
                Object daoClassInstance = getInstanceMethod.invoke(singletonClass);
                DAO_CLASSES.put(daoClass, daoClassInstance);
            }
        } catch (ReflectiveOperationException e) {
            log.error(e.getMessage());
            throw new RuntimeException("failed load instance dao classes");
        }
    }
}
