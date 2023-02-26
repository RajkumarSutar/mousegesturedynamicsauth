package io.rajkumarsutar.mousegesturedynamicsauth.database;

public class User {
	private String userName;
	private String mobile;
	private String profession;

	public User(String userName, String mobile, String profession) {
		this.userName = userName;
		this.mobile = mobile;
		this.profession = profession;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", mobile=" + mobile + ", profession=" + profession + "]";
	}
}
