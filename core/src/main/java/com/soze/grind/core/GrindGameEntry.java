package com.soze.grind.core;

import com.badlogic.gdx.Game;
import com.soze.grind.core.game.GameScreen;
import com.soze.grind.core.game.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GrindGameEntry extends Game {

  @Override
  public void create() {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(SpringConfig.class);

    GameScreen gameScreen = context.getBean(GameScreen.class);

    setScreen(gameScreen);
  }
}
