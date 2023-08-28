package by.home.aop.api;

import by.home.dao.entity.IsolationLevel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Transactional {

    IsolationLevel isolation() default IsolationLevel.TRANSACTION_READ_COMMITTED;

    Class<?>[] daoInterfaces() default {};

    boolean readOnly() default false;
}
