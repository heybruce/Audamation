package utils.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class LoggingAspect {
    @Around("execution(* *.*(..)) && @annotation(utils.log.Loggable)")
    public Object profile(ProceedingJoinPoint  joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggableMethod = method.getAnnotation(Loggable.class);
        Loggable loggableClass = joinPoint.getTarget().getClass().getAnnotation(Loggable.class);
        Object result = null;
        long startTime = 0;
        long endTime = 0;
        StringBuilder sbResult = new StringBuilder();

        //get current log level
        Loggable.Level logLevel = loggableMethod != null ? loggableMethod.level() : loggableClass.level();

        //before
        sbResult.append(method.getName() + "(");

        //show params
        boolean showParams = loggableMethod != null ? loggableMethod.params() : loggableClass.params();
        if (showParams && joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                sb.append(joinPoint.getArgs()[i]);
                if (i < joinPoint.getArgs().length - 1)
                    sb.append(", ");
            }
            sbResult.append(sb);
        }

        sbResult.append(")");
        startTime = System.currentTimeMillis();

        try {
            // start method execution
            result = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            //show after
            LogWriter.write(joinPoint.getTarget().getClass(), logLevel, sbResult + " in "+(endTime-startTime)+" millis second");
        } finally {

            //show results
            if (result != null) {
                boolean showResults = loggableMethod != null ? loggableMethod.result() : loggableClass.result();
                if (showResults) {
                    sbResult.append(" Result : " + result);
                }
            }
        }
        return result;
    }
}
