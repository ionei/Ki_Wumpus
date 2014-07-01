package ki_FW_RK;

import de.northernstars.jwumpus.core.WumpusMapObject;

public class objectContainer {
	private int risiko=0;
	private int wumpusRisiko=0;
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
	public objectContainer(int risiko, int wumpusRisiko, WumpusMapObject object) {
		this.risiko = risiko;
		this.setWumpusRisiko(wumpusRisiko);
		this.object = object;
	}
	public int getWumpusRisiko() {
		return wumpusRisiko;
	}
	public void setWumpusRisiko(int wumpusRisiko) {
		this.wumpusRisiko = wumpusRisiko;
	}
}
