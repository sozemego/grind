package com.soze.grind.core.game.ecs.system;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.ecs.component.ActorComponent;
import com.soze.grind.core.game.ecs.component.PositionComponent;

@All({PositionComponent.class, ActorComponent.class})
public class ActorPositionSystem extends BaseEntitySystem {

	private ComponentMapper<PositionComponent> positionMapper;
	private ComponentMapper<ActorComponent> actorMapper;

	@Override
	protected void processSystem() {

		IntBag activeEntities = getSubscription().getEntities();
		int[] ids = activeEntities.getData();

    for (int i = 0; i < activeEntities.size(); i++) {

    	int id = ids[i];

			PositionComponent positionComponent = positionMapper.get(id);
			ActorComponent actorComponent = actorMapper.get(id);

			Actor actor = actorComponent.getActor();

			actor.setPosition(positionComponent.getX(), positionComponent.getY());

		}
	}
}
