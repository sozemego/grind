package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soze.grind.core.game.ecs.domain.Hero;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/**
 * UI element for the selected Hero.
 */
public class SelectedHeroTable extends Table {

	private final UIElementFactory uiElementFactory;

	private final Entity entity;

	public SelectedHeroTable(UIElementFactory uiElementFactory, Entity entity) {
		this.uiElementFactory = uiElementFactory;
		this.entity = entity;

		Label nameLabel = uiElementFactory.createHeaderLabel();
		nameLabel.setText(Hero.getName(entity));

		add(nameLabel).row();

		add(uiElementFactory.createDivider()).row();

		Label healthLabel = uiElementFactory.createTextLabel();
		healthLabel.setText("Health: " + Hero.getHealth(entity) + "/" + Hero.getMaxHealth(entity));

		add(healthLabel).row();

	}

}
