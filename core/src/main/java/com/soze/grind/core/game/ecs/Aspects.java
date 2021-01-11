package com.soze.grind.core.game.ecs;

import com.artemis.Aspect;
import com.soze.grind.core.game.ecs.component.HeroComponent;

public class Aspects {

	public static final Aspect.Builder HERO = Aspect.all(HeroComponent.class);

}
