package com.soze.grind.core.game.ecs.component;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.unit.WorkerStateListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

public class WorkerAiComponent extends Component {

	private Entity worker;

	private StateMachine<WorkerState, WorkerEvent> state = initialState();

	public void setWorker(Entity worker) {
		this.worker = worker;
	}

	public void act(float delta, World world) {

		WorkerState workerState = state.getState().getId();

		switch (workerState) {
			case IDLE:
				handleIdle(delta);
				break;
			case SEARCHING_FOR_RESOURCE:
				handleSearchingForResource(delta, world);
				break;
			case SEARCHING_FOR_PATH:
				handleSearchingForPath(delta);
				break;
			case SEARCHING_FOR_WAREHOUSE:
				handleSearchingForWarehouse(delta, world);
				break;
			case TRAVELLING:
				handleTravelling(delta, world);
				break;
			case WORKING:
				handleWorking(delta, world);
				break;
		}
	}

	/** Gets the progress of the current 'WORKING' state (or 0 if in different state). */
	public float getWorkingProgress() {
		Map<Object, Object> variables = this.state.getExtendedState().getVariables();

		float workingTime = (float) variables.getOrDefault("workingTime", 0f);
		float maxWorkingTime = (float) variables.getOrDefault("maxWorkingTime", 0f);

		if (maxWorkingTime == 0f) {
			return 0f;
		}

		return workingTime / maxWorkingTime;
	}

	/** Gets the current worker state. */
	public WorkerState getState() {
		return this.state.getState().getId();
	}

	/**
	 * Initializes the state machine of the worker AI.
	 *
	 * @return StateMachine
	 */
	private StateMachine<WorkerState, WorkerEvent> initialState() {

		Builder<WorkerState, WorkerEvent> builder = StateMachineBuilder.builder();

		try {
			builder
					.configureConfiguration()
					.withConfiguration()
					.autoStartup(true)
					.listener(new WorkerStateListener());

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.IDLE)
					.target(WorkerState.SEARCHING_FOR_WAREHOUSE)
					.event(WorkerEvent.START_SEARCHING_WAREHOUSE);

			builder
					.configureStates()
					.withStates()
					.initial(WorkerState.IDLE)
					.states(EnumSet.allOf(WorkerState.class));

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.IDLE)
					.target(WorkerState.SEARCHING_FOR_RESOURCE)
					.event(WorkerEvent.START_SEARCHING_RESOURCE);

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.SEARCHING_FOR_RESOURCE)
					.target(WorkerState.SEARCHING_FOR_PATH)
					.event(WorkerEvent.START_SEARCHING_PATH)
					.action(onStartSearchingForPathToTarget());

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.SEARCHING_FOR_WAREHOUSE)
					.target(WorkerState.SEARCHING_FOR_PATH)
					.event(WorkerEvent.START_SEARCHING_PATH)
					.action(onStartSearchingForPathToTarget());

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.SEARCHING_FOR_PATH)
					.target(WorkerState.TRAVELLING)
					.event(WorkerEvent.START_TRAVELLING);

			builder
					.configureTransitions()
					.withExternal()
					.source(WorkerState.TRAVELLING)
					.target(WorkerState.WORKING)
					.event(WorkerEvent.START_WORKING)
					.action(onStartWorking());

			builder
					.configureTransitions()
					.withExternal()
					.target(WorkerState.IDLE)
					.source(WorkerState.TRAVELLING)
					.event(WorkerEvent.STOP);

			builder
					.configureTransitions()
					.withExternal()
					.target(WorkerState.IDLE)
					.source(WorkerState.WORKING)
					.event(WorkerEvent.STOP)
					.action(onStop());

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return builder.build();
	}

	/**
	 * Action invoked when the worker starts searching for a path to the target.
	 *
	 * @return Action
	 */
	private Action<WorkerState, WorkerEvent> onStartSearchingForPathToTarget() {
		return (context) -> {
			Object target = context.getMessage().getHeaders().get("target");

			Map<Object, Object> variables = context.getExtendedState().getVariables();

			variables.put("target", target);
		};
	}

	/**
	 * Action called when the worker starts working.
	 *
	 * @return Action
	 */
	private Action<WorkerState, WorkerEvent> onStartWorking() {
		return (context) -> {
			Map<Object, Object> variables = context.getExtendedState().getVariables();

			variables.put("workingTime", 0f);
			variables.put("maxWorkingTime", 10f);
		};
	}

	/**
	 * Called when the worker stops whatever they were doing.
	 *
	 * @return Action
	 */
	private Action<WorkerState, WorkerEvent> onStop() {
		return (context) -> {
			Map<Object, Object> variables = context.getExtendedState().getVariables();

			variables.put("workingTime", 0f);
			variables.put("maxWorkingTime", 0f);
		};
	}

	/** Handles the IDLE state. */
	private void handleIdle(float delta) {

		ResourceStorage resourceStorage =
				worker.getComponent(ResourceStorageComponent.class).getResourceStorage();

		if (resourceStorage.capacity(ResourceEnum.WOOD) == 0) {
			this.state.sendEvent(WorkerEvent.START_SEARCHING_WAREHOUSE);
		} else {
			this.state.sendEvent(WorkerEvent.START_SEARCHING_RESOURCE);
		}
	}

	/** Handles searching for resources */
	private void handleSearchingForResource(float delta, World world) {

		EntitySubscription entitySubscription =
				world.getAspectSubscriptionManager().get(Aspect.all(ResourceComponent.class));

		IntBag entityIds = entitySubscription.getEntities();
		int[] ids = entityIds.getData();

		List<Integer> resources = new ArrayList<>();

		ComponentMapper<ResourceStorageComponent> resourceStorageMapper =
				world.getMapper(ResourceStorageComponent.class);

		ComponentMapper<ResourceComponent> resourceComponentMapper =
				world.getMapper(ResourceComponent.class);

		for (int i = 0; i < entityIds.size(); i++) {
			int id = ids[i];

			ResourceStorageComponent resourceStorageComponent = resourceStorageMapper.get(id);
			ResourceComponent resourceComponent = resourceComponentMapper.get(id);

			ResourceStorage resourceStorage = resourceStorageComponent.getResourceStorage();

			if (resourceStorage.count(resourceComponent.getResourceEnum()) > 0) {
				resources.add(id);
			}
		}

		resources.sort(
				(o1, o2) -> {
					float distance1 = distance(world.getEntity(o1));
					float distance2 = distance(world.getEntity(o2));

					return Float.compare(distance1, distance2);
				});

		if (!resources.isEmpty()) {

			int targetEntityId = resources.get(0);

			Map<String, Object> headers = new HashMap<>();

			headers.put("target", targetEntityId);

			this.state.sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
		}
	}

	/** Finds the nearest warehouse to transfer goods to. */
	private void handleSearchingForWarehouse(float delta, World world) {

		EntitySubscription entitySubscription =
				world.getAspectSubscriptionManager().get(Aspect.all(WarehouseComponent.class));

		IntBag entityIds = entitySubscription.getEntities();
		int[] ids = entityIds.getData();

		List<Integer> buildings = new ArrayList<>();

		for (int i = 0; i < entityIds.size(); i++) {
			int id = ids[i];

			buildings.add(id);
		}

		buildings.sort(
				(o1, o2) -> {
					float distance1 = distance(world.getEntity(o1));
					float distance2 = distance(world.getEntity(o2));

					return Float.compare(distance1, distance2);
				});

		if (!buildings.isEmpty()) {

			int targetEntityId = buildings.get(0);

			Map<String, Object> headers = new HashMap<>();

			headers.put("target", targetEntityId);

			this.state.sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
		}
	}

	/** Handles searching for path for the target. */
	private void handleSearchingForPath(float delta) {
		this.state.sendEvent(WorkerEvent.START_TRAVELLING);
	}

	/** Handles travelling to the target. */
	private void handleTravelling(float delta, World world) {

		Integer targetEntityId = getTarget();

		if (Objects.isNull(targetEntityId)) {
			return;
		}

		Entity target = world.getEntity(targetEntityId);

		float distance = distance(target);

		if (distance < 12f) {
			// arrived

			if (Objects.nonNull(target.getComponent(ResourceComponent.class))) {
				this.state.sendEvent(WorkerEvent.START_WORKING);
			} else if (Objects.nonNull(target.getComponent(WarehouseComponent.class))) {
				transferAllResources(target);
				this.state.sendEvent(WorkerEvent.STOP);
			}

		} else {

			PositionComponent targetPositionComponent = target.getComponent(PositionComponent.class);
			PositionComponent workerPositionComponent = worker.getComponent(PositionComponent.class);

			float angle =
					new Vector2(targetPositionComponent.getX(), targetPositionComponent.getY())
							.sub(new Vector2(workerPositionComponent.getX(), workerPositionComponent.getY()))
							.nor()
							.angleRad();

			float cos = MathUtils.cos(angle);
			float sin = MathUtils.sin(angle);

			float speed = 2.5f;

			workerPositionComponent.setPosition(
					workerPositionComponent.getX() + cos * speed,
					workerPositionComponent.getY() + sin * speed);
		}
	}

	private void handleWorking(float delta, World world) {
		Map<Object, Object> variables = this.state.getExtendedState().getVariables();

		float workingTime = (float) variables.get("workingTime");
		float maxWorkingTime = (float) variables.get("maxWorkingTime");

		workingTime += delta;

		if (workingTime >= maxWorkingTime) {
			// FINISHED WORKING
			Integer targetEntityId = getTarget();

			Entity targetEntity = world.getEntity(targetEntityId);

			ResourceComponent resourceComponent = targetEntity.getComponent(ResourceComponent.class);
			ResourceStorage targetStorage =
					targetEntity.getComponent(ResourceStorageComponent.class).getResourceStorage();

			int amountRemoved = targetStorage.removeResource(resourceComponent.getResourceEnum(), 1);

			ResourceStorage workerStorage =
					worker.getComponent(ResourceStorageComponent.class).getResourceStorage();

			workerStorage.addResource(resourceComponent.getResourceEnum(), amountRemoved);

			this.state.sendEvent(WorkerEvent.STOP);

		} else {
			variables.put("workingTime", workingTime);
		}
	}

	/** Gets the target from extended state. */
	private Integer getTarget() {
		return this.state.getExtendedState().get("target", Integer.class);
	}

	/** Returns the worker distance to the target. */
	private float distance(Entity targetEntity) {

		PositionComponent positionComponent = targetEntity.getComponent(PositionComponent.class);
		PositionComponent workerPositionComponent = worker.getComponent(PositionComponent.class);

		return new Vector2(positionComponent.getX(), positionComponent.getY())
				.dst(new Vector2(workerPositionComponent.getX(), workerPositionComponent.getY()));
	}

	/** Attempts to transfer all resources to given target. */
	private void transferAllResources(Entity target) {

		ResourceStorageComponent resourceStorageComponent =
				target.getComponent(ResourceStorageComponent.class);

		if (Objects.isNull(resourceStorageComponent)) {
			return;
		}

		ResourceStorage targetStorage = resourceStorageComponent.getResourceStorage();

		if (Objects.nonNull(targetStorage)) {

			ResourceStorage workerStorage =
					worker.getComponent(ResourceStorageComponent.class).getResourceStorage();

			Map<ResourceEnum, Integer> workerResources = workerStorage.getResources();

			for (ResourceEnum resourceEnum : workerResources.keySet()) {
				int count = workerResources.get(resourceEnum);

				int amountAdded = targetStorage.addResource(resourceEnum, count);
				workerStorage.removeResource(resourceEnum, amountAdded);
			}
		}
	}

	public enum WorkerState {
		IDLE,
		SEARCHING_FOR_RESOURCE,
		SEARCHING_FOR_PATH,
		SEARCHING_FOR_WAREHOUSE,
		TRAVELLING,
		WORKING;
	}

	public enum WorkerEvent {
		STOP,
		START_SEARCHING_RESOURCE,
		START_SEARCHING_PATH,
		START_SEARCHING_WAREHOUSE,
		START_TRAVELLING,
		START_WORKING;
	}

}
