package com.soze.grind.core.game.state;

import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

//@Configuration
//@EnableStateMachine
public class GameStateMachineConfig
    extends EnumStateMachineConfigurerAdapter<GameState, GameEvent> {

  @Override
  public void configure(StateMachineConfigBuilder<GameState, GameEvent> config) throws Exception {}

  @Override
  public void configure(StateMachineStateConfigurer<GameState, GameEvent> states) throws Exception {
    states.withStates().initial(GameState.STARTING).states(EnumSet.allOf(GameState.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<GameState, GameEvent> transitions)
      throws Exception {

    transitions
        .withExternal()
        .source(GameState.STARTING)
        .target(GameState.LOADING_ASSETS)
        .event(GameEvent.START_LOADING_ASSETS);

    transitions
        .withExternal()
        .source(GameState.LOADING_ASSETS)
        .target(GameState.PLAYING)
        .event(GameEvent.START_PLAYING);
  }
}
