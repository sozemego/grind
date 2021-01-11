package com.soze.grind.core.game.ecs.world;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.soze.grind.core.game.ecs.Aspects;
import com.soze.grind.core.game.ecs.domain.AbstractEntity;
import com.soze.grind.core.game.ecs.domain.Hero;
import com.soze.grind.core.game.ecs.domain.Resource;
import com.soze.grind.core.game.ecs.domain.Warehouse;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
		return getEntitiesForAspect(Aspects.HERO, Hero::new);
	}

	/**
	 * Gets all Resources in the game.
	 */
	public List<Resource> getResources() {
		return getEntitiesForAspect(Aspects.RESOURCE, Resource::new);
	}

	/**
	 * Gets all warehouses.
	 */
	public List<Warehouse> getWarehouses() {
		return getEntitiesForAspect(Aspects.WAREHOUSE, Warehouse::new);
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

	/**
	 * Gets all AbstractEntities with given aspect. The producer function takes the artemis Entity
	 * and wraps it with some concrete implementation of AbstractEntity.
	 */
	private <T extends AbstractEntity> List<T> getEntitiesForAspect(Aspect.Builder aspectBuilder, Function<Entity, T> producer) {
		List<Integer> ids = getEntityIdsForAspect(aspectBuilder);

		List<T> entities = new ArrayList<>();

    for (Integer id : ids) {
			entities.add(producer.apply(world.getEntity(id)));
    }

    return entities;
	}
}
