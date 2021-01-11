package com.soze.grind.core.game.ecs.component.factory;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ecs.component.ActorComponent;
import com.soze.grind.core.game.ecs.component.BuildingComponent;
import com.soze.grind.core.game.ecs.component.HealthComponent;
import com.soze.grind.core.game.ecs.component.HeroComponent;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.PositionComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WarehouseComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerComponent;
import com.soze.grind.core.game.ecs.component.WorkerProgressBarComponent;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.storage.TotalCapacityResourceStorage;
import com.soze.grind.core.game.ui.ProgressBar;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentFactory {

	private final World world;
	private final AssetService assetService;
	private final UIElementFactory uiElementFactory;

	@Autowired
	public ComponentFactory(
			World world,
			AssetService assetService,
			UIElementFactory uiElementFactory
	) {
		this.world = world;
		this.assetService = assetService;
		this.uiElementFactory = uiElementFactory;
	}

	/**
	 * Creates actor component for given entity and sets an Image actor with given texture.
	 *
	 * @param entityId id of the entity
	 * @param texture name of the text
	 * @return ActorComponent
	 */
	public ActorComponent createImageActorComponent(int entityId, String texture) {
		ActorComponent actorComponent = getMapper(ActorComponent.class).create(entityId);
		actorComponent.setActor(new Image(assetService.getTexture(texture)));

		return actorComponent;
	}

	/**
	 * Creates PositionComponent.
	 *
	 * @param entityId id of the entity
	 * @param x x
	 * @param y y
	 * @param width width
	 * @param height height
	 * @return PositionComponent
	 */
	public PositionComponent createPositionComponent(int entityId, float x, float y, float width, float height) {
		PositionComponent positionComponent = getMapper(PositionComponent.class).create(entityId);
		positionComponent.setPosition(x, y);
		positionComponent.setSize(width, height);

		return positionComponent;
	}

	/**
	 * Creates NameComponent with given name.
	 *
	 * @param entityId id of the entity
	 * @param name name
	 * @return NameComponent
	 */
	public NameComponent createNameComponent(int entityId, String name) {
		NameComponent nameComponent = getMapper(NameComponent.class).create(entityId);
		nameComponent.setName(name);

		return nameComponent;
	}

	/**
	 * Creates a WarehouseComponent.
	 *
	 * @param entityId id of the entity
	 * @return WarehouseComponent
	 */
	public WarehouseComponent createWarehouseComponent(int entityId) {
		return getMapper(WarehouseComponent.class).create(entityId);
	}

	/**
	 * Creates a BuildingComponent.
	 *
	 * @param entityId id of the entity
	 * @return BuildingComponent
	 */
	public BuildingComponent createBuildingComponent(int entityId) {
		return getMapper(BuildingComponent.class).create(entityId);
	}

	/**
	 * Creates a ResourceStorageComponent.
	 *
	 * @param entityId id of the entity
	 * @param capacity capacity of the storage
	 * @return ResourceStorageComponent
	 */
	public ResourceStorageComponent createResourceStorageComponent(int entityId, int capacity) {
		ResourceStorageComponent resourceStorageComponent = getMapper(ResourceStorageComponent.class).create(entityId);

		ResourceStorage resourceStorage = new TotalCapacityResourceStorage(capacity);
		resourceStorageComponent.setResourceStorage(resourceStorage);

		return resourceStorageComponent;
	}

	/**
	 * Creates a WorkerComponent.
	 *
	 * @param entityId id of the entity
	 * @return WorkerComponent
	 */
	public WorkerComponent createWorkerComponent(int entityId) {
		return getMapper(WorkerComponent.class).create(entityId);
	}

	/**
	 * Creates a ResourceComponent.
	 *
	 * @param entityId id of the entity
	 * @param resourceEnum resourceEnum
	 * @return ResourceComponent
	 */
	public ResourceComponent createResourceComponent(int entityId, ResourceEnum resourceEnum) {
		ResourceComponent resourceComponent = getMapper(ResourceComponent.class).create(entityId);
		resourceComponent.setResourceEnum(resourceEnum);

		return resourceComponent;
	}

	/**
	 * Creates a WorkerAiComponent.
	 *
	 * @param entityId id of the entity
	 * @return WorkerAiComponent
	 */
	public WorkerAiComponent createWorkerAiComponent(int entityId) {
		WorkerAiComponent workerAiComponent = getMapper(WorkerAiComponent.class).create(entityId);

		return workerAiComponent;
	}

	/**
	 * Creates a WorkerProgressBarComponent.
	 *
	 * @param entityId id of the entity
	 */
	public WorkerProgressBarComponent createWorkerProgressBarComponent(int entityId) {
		WorkerProgressBarComponent workerProgressBarComponent = getMapper(WorkerProgressBarComponent.class).create(entityId);

		ProgressBar progressBar = uiElementFactory.createGameWorldProgressBar();

    workerProgressBarComponent.setProgressBar(progressBar);

    progressBar.setSize(64, 12);

    return workerProgressBarComponent;
	}

	/**
	 * Creates a HealthComponent.
	 *
	 * @param entityId id of the entity
	 * @param health health
	 * @param maxHealth max health
	 */
	public HealthComponent createHealthComponent(int entityId, int health, int maxHealth) {
		HealthComponent healthComponent = getMapper(HealthComponent.class).create(entityId);
		healthComponent.setHealth(health);
		healthComponent.setMaxHealth(maxHealth);

		return healthComponent;
	}

	/**
	 * Creates a HeroComponent.
	 * @param entityId id of the entity
	 */
	public HeroComponent createHeroComponent(int entityId) {
		HeroComponent heroComponent = getMapper(HeroComponent.class).create(entityId);
		return heroComponent;
	}

	/**
	 * Gets a ComponentMapper for a given class.
	 *
	 * @param clazz class of the mapper
	 */
	private <T extends Component> ComponentMapper<T> getMapper(Class<T> clazz) {
		return world.getMapper(clazz);
	}

}
