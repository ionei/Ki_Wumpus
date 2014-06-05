package ki_FW_RK;

import de.northernstars.jwumpus.core.JWumpus;

public class Startmain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyAi ai = new MyAi();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new JWumpus( ai );
	}

}
