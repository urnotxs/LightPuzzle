package com.xs.lightpuzzle.demo.a_tactics_demo.data;

import java.io.Serializable;

/**
 * @author xs
 * @description
 * @since 2019/2/21
 */

public class TeamUserPositionVO implements TacticsModelVO, Serializable {

    private static final long serialVersionUID = 1L;

    public String TID;
    public int PositionID;
    public String PositionName;
    public String UserID;
    public float PosX;
    public float PosY;
    public int Type;
    public boolean IsShow;

    private UserBriefVO userVO = null;

    @Override
    public boolean HasUser() {
        return userVO != null;
    }

    @Override
    public void SetUser(Object user) {
        userVO = (UserBriefVO) user;
    }

    @Override
    public String GetUserName() {
        return userVO.NickName;
    }

    @Override
    public String GetUserImg() {
        return userVO.HeadImgLink;
    }

    @Override
    public int GetType() {
        return Type;
    }

    @Override
    public float GetPosX() {
        return PosX;
    }

    @Override
    public void SetXY(float x, float y) {
        PosX = x;
        PosY = y;
    }

    @Override
    public float GetPosY() {
        return PosY;
    }

    @Override
    public String GetPositionName() {
        return PositionName;
    }

    @Override
    public boolean GetIsShow() {
        return IsShow;
    }

    @Override
    public int GetPositionID() {
        return PositionID;
    }

    @Override
    public String GetUserID() {
        return UserID;
    }

    @Override
    public TeamUserPositionVO GetTeamUserPosition() {
        return this;
    }
}