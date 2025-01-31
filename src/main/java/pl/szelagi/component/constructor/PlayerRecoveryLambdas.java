/*
 * SessionAPI - A framework for game containerization on Minecraft servers
 * Copyright (C) 2025 Szelagi (https://github.com/Szelagi/SessionAPI)
 * Licensed under the GNU General Public License v3.0.
 * For more details, visit <https://www.gnu.org/licenses/>.
 */

package pl.szelagi.component.constructor;

import java.util.ArrayList;
import java.util.List;

public class PlayerRecoveryLambdas<T> {
	private final ArrayList<T> lambdas = new ArrayList<>();

	public PlayerRecoveryLambdas<T> add(T lambda) {
		lambdas.add(lambda);
		return this;
	}

	public List<T> getLambdas() {
		return lambdas;
	}

	public boolean isEmpty() {
		return lambdas.isEmpty();
	}
}
