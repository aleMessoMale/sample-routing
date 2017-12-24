package com.amazingsoftware.integration.samples.arch.aspect.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.amazingsoftware.integration.samples.utils.LogFilteringUtils;

/**
 * 
 *         This class is responsible for printing logs automatically through the
 *         use of Aspects.
 * 
 *         Through specific PointCut logs are printed only when certain
 *         conditions are met.
 *         
 *         
 *         
 *         @author al.casula
 */
@Component
@Aspect
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut("execution(public * *(..))")
	private void anyPublicOperation() {
	}

	@Pointcut("anyImplInsideFacade() || anyImplInsideService() ")
	public void allImplementedLayers() {
	}

	@Pointcut("within(com.amazingsoftware.integration..*.service..*.impl..*)")
	public void anyImplInsideService() {
	}

	@Pointcut("within(com.amazingsoftware.integration..*.facade..*.impl..*)")
	public void anyImplInsideFacade() {
	}

	@Pointcut("within(com.amazingsoftware.integration..*.dto..*)")
	public void anyDto() {
	}

	@Pointcut("execution(* get*())")
	public void anyGetter() {
	}

	@Pointcut("execution(* set*())")
	public void anySetter() {
	}

	@Pointcut("anyGetter() && anySetter()")
	public void anyGetterAndSetter() {
	}

	@Pointcut("anyPublicOperation() && allImplementedLayers() && !anyDto() && !anyGetterAndSetter() ")
	public void mainPointcut() {
	}

	/**
	 * mainPointCut is an Around Aspect. When the conditions in the PointCuts
	 * defined before are met, particular logs are written before and after
	 * method execution.
	 * 
	 * Sensible data can be easily removed through a filter called in arguments or result and the use of {@link LogFilteringUtils}
	 * 
	 * @param joinPoint
	 *            the JoinPoint of the Around Aspect
	 * @return
	 * @throws Throwable
	 *             eventual exception that can happen during execution
	 */
	@Around("mainPointcut()")
	public Object aroundMainPointcut(ProceedingJoinPoint joinPoint) throws Throwable {

		return executeAround(joinPoint, new BeforeExecution() {
			@Override
			public void run(String className, String operation, Object[] args) {
				logger.info("Entering: {}.{}({})", className == null ? "className is null" : className,
						operation == null ? "operation is null" : operation,
						args == null ? "args are null" : LogFilteringUtils.maskArgs(args));
			}
		}, new AfterExecution() {
			@Override
			public void run(String className, String operation, Object[] args, Object result, long totalTimeMillis) {
				logger.info("Exiting: {}.{}, Return: {}, Executed in: {} ms",
						className == null ? "className is null" : className,
						operation == null ? "operation is null" : operation,
						result == null ? "result is null" : LogFilteringUtils.maskResult(result), totalTimeMillis);
			}
		});
	}

	@AfterThrowing(pointcut = "mainPointcut()", throwing = "ex")
	public void afterThrow(Exception ex) {

		if (ex != null) {
			logger.error("Got an Exception. Cause: {}, Message: {}, Stacktrace: {}", ex.getCause(), ex.getMessage(),
					ex);
		}

		return;
	}

	private Object executeAround(ProceedingJoinPoint joinPoint, BeforeExecution beforeExecution,
			AfterExecution afterExecution) throws Throwable {

		final Object target = joinPoint.getTarget();

		String className = target != null ? target.getClass().getCanonicalName() : "";
		String operation = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();

		beforeExecution.run(className, operation, args);

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		Object result = joinPoint.proceed();
		stopWatch.stop();

		final long totalTimeMillis = stopWatch.getTotalTimeMillis();

		afterExecution.run(className, operation, args, result, totalTimeMillis);
		return result;
	}

	@FunctionalInterface
	private interface BeforeExecution {
		void run(String className, String operation, Object[] args);
	}

	@FunctionalInterface
	private interface AfterExecution {
		void run(String className, String operation, Object[] args, Object result, long totalTimeMillis);
	}

}
