package tranquvis.simplesmsremote.Aspects.ExecSequentially;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by Andreas Kaltenleitner on 06.10.2016.
 */

@Aspect
public class ExecSequentiallyAspect {
    private static String[] currentlyExecutedSequences = new String[100];

    /**
     * @param id sequence id
     * @return position in array
     */
    private static int addCurrentlyExecutedSequence(String id) throws Exception {
        for (int i = 0; i < currentlyExecutedSequences.length; i++) {
            if (currentlyExecutedSequences[i] == null) {
                currentlyExecutedSequences[i] = id;
                return i;
            }
        }

        throw new Exception("currentlyExecutedSequences is full");
    }

    @Pointcut("execution(@tranquvis.simplesmsremote.Helper.ExecSequentially * *(..))")
    public void methodAnnotatedWithExecSequentially() {
    }

    @Around("methodAnnotatedWithExecSequentially()")
    public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ExecSequentially annotation = methodSignature.getMethod().getAnnotation(ExecSequentially.class);

        //wait until currently method execution in sequence ends

        String id = annotation.value();
        //in milliseconds
        int timeout = 10;
        int maxWaitingTime = 10000;
        int i = 0;
        while (ArrayUtils.contains(currentlyExecutedSequences, id)) {
            Thread.sleep(timeout);

            if (timeout * i > maxWaitingTime)
                throw new Exception("method reached max waiting time");
        }

        int pos = addCurrentlyExecutedSequence(id);

        Object result = joinPoint.proceed();

        currentlyExecutedSequences[pos] = null;

        return result;
    }
}