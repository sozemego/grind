package com.soze.grind.core;

import com.badlogic.gdx.Game;
import com.soze.grind.core.game.GameScreen;
import com.soze.grind.core.game.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.CommandLinePropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;

public class GrindGameEntry extends Game {

  private final String[] args;

  public GrindGameEntry(String[] args) {
    this.args = args;
  }

  @Override
  public void create() {
    CommandLinePropertySource<?> commandLinePropertySource =
        new SimpleCommandLinePropertySource(this.args);

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.getEnvironment().getPropertySources().addFirst(commandLinePropertySource);

    context.register(SpringConfig.class);
    context.refresh();

    GameScreen gameScreen = context.getBean(GameScreen.class);

    this.setScreen(gameScreen);
  }
}
