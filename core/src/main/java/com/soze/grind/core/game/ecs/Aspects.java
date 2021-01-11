package com.soze.grind.core.game.ecs;

import com.artemis.Aspect;
import com.soze.grind.core.game.ecs.component.HeroComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.WarehouseComponent;

public class Aspects {

	public static final Aspect.Builder HERO = Aspect.all(HeroComponent.class);

	public static final Aspect.Builder RESOURCE = Aspect.all(ResourceComponent.class);

	public static final Aspect.Builder WAREHOUSE = Aspect.all(WarehouseComponent.class);

}
