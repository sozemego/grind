package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.soze.grind.core.game.ui.ProgressBar;

public class WorkerProgressBarComponent extends Component {

	private ProgressBar progressBar;

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
}
