package com.xs.lightpuzzle.demo.a_tactics_demo.data;

public interface TacticsModelVO {

	public boolean HasUser();

	public void SetUser(Object user);

	public String GetUserName();

	public String GetUserImg();

	public int GetType(); // 0-GK 1-DF 2-MF 3-MF 4-FW

	public float GetPosX();

	public void SetXY(float x, float y);

	public float GetPosY();

	public String GetPositionName();

	public boolean GetIsShow();
	
	public int GetPositionID();
	
	public String GetUserID();

	public TeamUserPositionVO GetTeamUserPosition();

}
