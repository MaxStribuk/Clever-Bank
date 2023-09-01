package by.home.aop.aspect;

import by.home.aop.api.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import static by.home.util.Constant.Utils.LOGGING_FILE_NAME;
import static by.home.util.Constant.Utils.LOGGING_MESSAGE_PATTERN;

/**
 * Класс аспект, необходимый для обработки аннотации {@link Loggable}
 */
@Aspect
@Slf4j
public class LaggingAspect {

    /**
     * Данный advice метод срабатывает перед выполнением всех методов,
     * помеченных аннотацией {@link Loggable}
     * <p>
     * Для таких методов осуществляется считывание входных параметров, потом осуществляется
     * фактический вызов метода, отмеченного аннотацией, после чего считывается возвращаемый результат.
     * Информация о входных параметрах и возвращаемом результате записывается в файл логирования.
     *
     * @param pjp      объект, необходимый для вызова метода, отмеченного аннотацией
     * @param loggable объект аннотации. Хоть данный параметр фактически и не используется,
     *                 его наличие обязательно
     * @return возвращает результат работы метода, отмеченного аннотацией
     * @throws IOException при возникновении ошибок записи в лог файл
     * @throws Throwable   при любый исключениях, возникших во время вызова метода,
     *                     помеченного аннотацией
     */
    @Around("@annotation(loggable)")
    public Object handleAnnotationLoggableAdvice(ProceedingJoinPoint pjp, Loggable loggable) throws Throwable {
        Object returnValue;
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(LOGGING_FILE_NAME, true))) {
            String argsString = Arrays.toString(pjp.getArgs());
            Signature signature = pjp.getSignature();
            returnValue = pjp.proceed();
            printWriter.printf(LOGGING_MESSAGE_PATTERN, signature, argsString, returnValue);
            return returnValue;
        } catch (IOException e) {
            log.error("error while working with file " + LOGGING_FILE_NAME);
            throw e;
        }
    }
}
