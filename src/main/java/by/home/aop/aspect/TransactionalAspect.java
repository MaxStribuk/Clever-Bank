package by.home.aop.aspect;

import by.home.aop.api.Transactional;
import by.home.dao.util.PropertiesUtil;
import by.home.data.exception.CustomSqlException;
import by.home.factory.util.ConnectionSingleton;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import static by.home.util.Constant.Utils.SET_CONN_METHOD_NAME;

@Aspect
@Slf4j
public class TransactionalAspect {

    @Around("@annotation(transactional)")
    public Object handleAnnotationTransactionalAdvice(
            ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
        Connection connection = null;
        Savepoint savepoint = null;
        try {
            connection = ConnectionSingleton.getInstance().open();
            connection.setReadOnly(transactional.readOnly());
            connection.setTransactionIsolation(transactional.isolation().getLevel());
            connection.setAutoCommit(false);
            savepoint = connection.setSavepoint();
            for (Class<?> clazz : transactional.daoInterfaces()) {
                Object daoClass = PropertiesUtil.getDaoClass(clazz);
                Method setConn = daoClass.getClass().getDeclaredMethod(SET_CONN_METHOD_NAME, Connection.class);
                setConn.invoke(daoClass, connection);
            }
            Object returnValue = pjp.proceed();
            connection.commit();
            return returnValue;
        } catch (Throwable e) {
            log.error(e.getMessage());
            if (connection != null) {
                try {
                    connection.rollback(savepoint);
                } catch (SQLException ex) {
                    throw new CustomSqlException(ex.getMessage(), ex);
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }
}
