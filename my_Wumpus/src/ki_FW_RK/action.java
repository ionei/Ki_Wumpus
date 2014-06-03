package ki_FW_RK;

import de.northernstars.jwumpus.core.*;

public class action {
	private action aktion;
	private Action action = null;
	public Action getAction() {
		/*
		 * Contains the AI algorithm.
		 * Here it's only grabbing last action set on gui and
		 * returns it to jWumpus main class.
		 */
		Action vAction = action;
		action = null;
		return vAction;
	}

}
