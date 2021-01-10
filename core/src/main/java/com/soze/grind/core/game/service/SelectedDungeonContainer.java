package com.soze.grind.core.game.service;

import com.google.common.eventbus.EventBus;
import com.soze.grind.core.game.Dungeon;
import com.soze.grind.core.game.event.DungeonSelectedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Contains the currently selected dungeon.
 */
@Service
public class SelectedDungeonContainer {

	private final EventBus eventBus;

	private Dungeon dungeon;

	@Autowired
	public SelectedDungeonContainer(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public Dungeon getSelectedDungeon() {
		return dungeon;
	}

	public void setSelectedDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
		eventBus.post(new DungeonSelectedEvent(dungeon));
	}
}
