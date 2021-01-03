package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import java.util.HashSet;
import java.util.Set;

/**
 * A component which contains a list of tags of an entity.
 */
public class TagsComponent extends Component {

	private final Set<String> tags = new HashSet<>();



}
