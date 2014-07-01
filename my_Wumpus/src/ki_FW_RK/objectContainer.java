package ki_FW_RK;

import de.northernstars.jwumpus.core.WumpusMapObject;

public class objectContainer {
	private int risiko=0;
	private WumpusMapObject object;
	public WumpusMapObject getObject() {
		return object;
	}
	public void setObject(WumpusMapObject object) {
		this.object = object;
	}
	public int getRisiko() {
		return risiko;
	}
	public void setRisiko(int risiko) {
		this.risiko = risiko;
	}
	public objectContainer(int risiko, WumpusMapObject object) {
		this.risiko = risiko;
		this.object = object;
	}
}
