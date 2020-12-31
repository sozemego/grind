package com.soze.grind.core.game.state;

import java.util.EnumSet;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

//@Configuration
//@EnableStateMachine
public class GameStateMachineConfig
    extends EnumStateMachineConfigurerAdapter<GameStateEnum, GameEvent> {

  @Override
  public void configure(StateMachineConfigBuilder<GameStateEnum, GameEvent> config) throws Exception {}

  @Override
  public void configure(StateMachineStateConfigurer<GameStateEnum, GameEvent> states) throws Exception {
    states.withStates().initial(GameStateEnum.STARTING).states(EnumSet.allOf(GameStateEnum.class));
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<GameStateEnum, GameEvent> transitions)
      throws Exception {

    transitions
        .withExternal()
        .source(GameStateEnum.STARTING)
        .target(GameStateEnum.LOADING_ASSETS)
        .event(GameEvent.START_LOADING_ASSETS);

    transitions
        .withExternal()
        .source(GameStateEnum.LOADING_ASSETS)
        .target(GameStateEnum.PLAYING)
        .event(GameEvent.START_PLAYING);
  }
}
