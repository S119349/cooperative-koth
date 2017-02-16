package coop;

public class PlayerLost extends Exception {

	public PlayerLost() {
		// TODO Auto-generated constructor stub
	}

	public PlayerLost(String arg0) {
		super(arg0);
		System.err.println(arg0);
	}

	public PlayerLost(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public PlayerLost(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public PlayerLost(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
