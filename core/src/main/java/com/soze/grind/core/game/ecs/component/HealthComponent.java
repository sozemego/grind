package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;

public class HealthComponent extends Component {

	private int health;
	private int maxHealth;

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
}
