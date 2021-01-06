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

	private final Hero hero;

	public SelectedHeroTable(UIElementFactory uiElementFactory, Hero hero) {
		this.uiElementFactory = uiElementFactory;
		this.hero = hero;

		Label nameLabel = uiElementFactory.createHeaderLabel();
		nameLabel.setText(hero.getName());

		add(nameLabel).row();

		add(uiElementFactory.createDivider()).row();

		Label healthLabel = uiElementFactory.createTextLabel();
		healthLabel.setText("Health: " + hero.getHealth() + "/" + hero.getMaxHealth());

		add(healthLabel).row();

	}

}
