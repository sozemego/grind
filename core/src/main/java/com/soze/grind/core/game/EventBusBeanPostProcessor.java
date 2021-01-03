package com.soze.grind.core.game;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/** This post processor is used to register all beans in the game EventBus. */
@Configuration
public class EventBusBeanPostProcessor implements BeanPostProcessor {

  private final EventBus eventBus;

  @Autowired
  public EventBusBeanPostProcessor(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    this.eventBus.register(bean);
    return bean;
  }
}
