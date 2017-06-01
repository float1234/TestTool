package jupiter.simple_test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jupiter on 2017/5/25.
 */
public class LoggerUtil {
  private static Logger logger;
  private static LoggerUtil loggerUtil;

  private LoggerUtil() {

  }

  public static LoggerUtil getInstance(Class clazz) {
    if (loggerUtil == null) {
      loggerUtil = new LoggerUtil();
    }
    logger = loggerUtil.getLogger(clazz);
    return loggerUtil;
  }

  public static Long begin(String methodName) {
    logger.info("MethodName : " + methodName + " ===> execute begin...");
    return System.currentTimeMillis();
  }

  public static void end(String methodName, Long beginTime) {
    Long endTime = System.currentTimeMillis();
    logger.info("MethodName : " + methodName + " ===> execute end... it cost " + (endTime - beginTime) + " millseconds");
  }

  public static void info(String methodName, String msg) {
    logger.info("MethodName : " + methodName + " ===> " + msg);
  }

  public static void error(String methodName, String msg) {
    logger.error("MethodName : " + methodName + " ===> " + msg);
  }

  public static void error(String methodName, Exception e) {
    String fullStackTrace = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(e);
    logger.error("MethodName : " + methodName + " ===> " + fullStackTrace);
  }

  public static void warn(String methodName, String msg) {
    logger.warn("MethodName : " + methodName + " ===> " + msg);
  }

  private Logger getLogger(Class clazz) {
    logger = LoggerFactory.getLogger(clazz);
    return logger;
  }
}
