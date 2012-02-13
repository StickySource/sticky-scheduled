package net.stickycode.scheduled;

import java.lang.reflect.Method;

import net.stickycode.stereotype.Scheduled;
import net.stickycode.stereotype.StickyFramework;
import net.stickycode.stereotype.component.StickyExtension;

@StickyExtension
@StickyFramework
public class SimpleScheduledInvokerFactory
    implements ScheduledMethodInvokerFactory {

  @Override
  public boolean canInvoke(Method method) {
    return method.isAnnotationPresent(Scheduled.class);
  }

  @Override
  public ScheduledMethodInvoker create(Object target, Method method, Schedule schedule) {
    return new ScheduledMethodInvoker(method, target, schedule);
  }

}
