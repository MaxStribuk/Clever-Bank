package by.home.aop.aspect;

import by.home.aop.api.Loggable;
import by.home.data.exception.AspectException;
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

@Aspect
@Slf4j
public class LaggingAspect {

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
        } catch (Throwable e) {
            log.error("error in LoggingAspect");
            throw e;
        }
    }
}
