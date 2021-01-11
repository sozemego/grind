package com.soze.grind.core.game.ecs.world;

import com.artemis.Aspect;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.soze.grind.core.game.ecs.Aspects;
import com.soze.grind.core.game.ecs.domain.Hero;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Main class to use to interact with {@link com.artemis.World} instance.
 */
@Service
public class GameWorld {

	private final World world;

	@Autowired
	public GameWorld(World world) {
		this.world = world;
	}

	public void setDelta(float delta) {
		world.setDelta(delta);
	}

	public void process() {
		world.process();
	}

	/**
	 * Gets all heroes.
	 */
	public List<Hero> getHeroes() {
		List<Integer> entityIdsForAspect = getEntityIdsForAspect(Aspects.HERO);
		List<Hero> heroes = new ArrayList<>();

    for (Integer entityId : entityIdsForAspect) {
    	heroes.add(new Hero(world.getEntity(entityId)));
    }

    return heroes;
	}

	/**
	 * Returns a list of entity ids that have the given aspect.
	 *
	 * @param aspectBuilder aspectBuilder
	 */
	private List<Integer> getEntityIdsForAspect(Aspect.Builder aspectBuilder) {
		EntitySubscription entitySubscription = world.getAspectSubscriptionManager().get(aspectBuilder);

		IntBag entityIds = entitySubscription.getEntities();
		int[] ids = entityIds.getData();

		List<Integer> integers = new ArrayList<>();

		for (int i = 0; i < entityIds.size(); i++) {
			integers.add(ids[i]);
		}

		return integers;
	}
}
