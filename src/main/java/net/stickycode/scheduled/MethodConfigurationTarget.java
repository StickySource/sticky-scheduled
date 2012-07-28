package net.stickycode.scheduled;

import java.beans.Introspector;
import java.lang.reflect.Method;

import net.stickycode.coercion.CoercionTarget;
import net.stickycode.configuration.ConfigurationTarget;
import net.stickycode.configuration.ResolvedConfiguration;

public class MethodConfigurationTarget
    implements ConfigurationTarget {

  private Method method;

  public MethodConfigurationTarget(Method method) {
    this.method = method;
  }

  @Override
  public String join(String delimeter) {
    return Introspector.decapitalize(method.getDeclaringClass().getSimpleName()) + delimeter + method.getName();
  }

  @Override
  public void resolvedWith(ResolvedConfiguration resolved) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CoercionTarget getCoercionTarget() {
    return null;
  }

}
