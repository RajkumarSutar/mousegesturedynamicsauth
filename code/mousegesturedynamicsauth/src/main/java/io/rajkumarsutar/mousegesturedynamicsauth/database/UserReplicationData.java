package io.rajkumarsutar.mousegesturedynamicsauth.database;

public class UserReplicationData {
	private String userID;
	private int total;
	private int gestureID;

	public UserReplicationData(int gestureID, int total, String userID) {
		super();
		this.userID = userID;
		this.total = total;
		this.gestureID = gestureID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getGestureID() {
		return gestureID;
	}

	public void setGestureID(int gestureID) {
		this.gestureID = gestureID;
	}

	@Override
	public String toString() {
		return "UserReplicationData [userID=" + userID + ", total=" + total + ", gestureID=" + gestureID + "]";
	}
}
