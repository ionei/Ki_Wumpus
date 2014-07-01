package ki_FW_RK;

import de.northernstars.jwumpus.core.WumpusMapObject;

public class MyWumpusMapObject extends WumpusMapObject{

	int risiko;
	public MyWumpusMapObject(WumpusMapObject object) {
		super(object);
		risiko = 0;
	}
	public int getRisiko() {
		return risiko;
	}
	public void setRisiko(int risiko) {
		this.risiko = risiko;
	}

}
