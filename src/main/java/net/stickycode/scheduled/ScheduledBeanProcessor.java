package net.stickycode.scheduled;

import java.lang.reflect.Method;
import java.util.Set;

import javax.inject.Inject;

import net.stickycode.configured.ConfiguredBeanProcessor;
import net.stickycode.reflector.Reflector;
import net.stickycode.reflector.predicate.MethodPredicate;
import net.stickycode.reflector.predicate.PredicateReflector;
import net.stickycode.stereotype.StickyComponent;
import net.stickycode.stereotype.StickyFramework;

@StickyComponent
@StickyFramework
public class ScheduledBeanProcessor {

//  @Inject
//  private ConfigurationRepository configurationRepository;

  @Inject
  private ScheduledRunnableRepository scheduledRunnableRepository;

  @Inject
  private Set<ScheduledMethodInvokerFactory> invokerFactories;
  
  @Inject
  ConfiguredBeanProcessor configuredBeanProcessor;

  private class Invokable
      implements MethodPredicate {

    @Override
    public boolean apply(Method method) {
      for (ScheduledMethodInvokerFactory factory : invokerFactories) {
        if (factory.canInvoke(method))
          return true;
      }

      return false;
    }
  }

  public boolean isSchedulable(Class<?> type) {
    return new PredicateReflector().given(type).areAnyMethods(new Invokable());
  }

  public void process(Object instance) {
//    ConfiguredConfiguration configuration = new ConfiguredConfiguration(instance, Introspector.decapitalize(instance.getClass()
//        .getSimpleName()));

    ScheduledMethodProcessor scheduledMethodProcessor = new ScheduledMethodProcessor()
        .withInvokers(invokerFactories)
        .withSchedulingSystem(scheduledRunnableRepository)
        .withConfiguration(configuredBeanProcessor);

    new Reflector()
        .forEachMethod(scheduledMethodProcessor)
        .process(instance);

  }

}
