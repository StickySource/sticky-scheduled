package net.stickycode.scheduled;

import java.lang.reflect.Method;

import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.plugin.StickyExtension;
import net.stickycode.stereotype.scheduled.Scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@StickyExtension
@StickyFramework
public class SimpleScheduledInvokerFactory
    implements ScheduledMethodInvokerFactory {

  private Logger log = LoggerFactory.getLogger(getClass());

  @Override
  public boolean canInvoke(Method method) {
    return method.isAnnotationPresent(Scheduled.class);
  }

  @Override
  public ScheduledMethodInvoker create(Object target, Method method) {
    ScheduledMethodInvoker scheduledMethodInvoker = new ScheduledMethodInvoker(method, target);
    log.debug("creating {}", scheduledMethodInvoker);
    return scheduledMethodInvoker;
  }

}
