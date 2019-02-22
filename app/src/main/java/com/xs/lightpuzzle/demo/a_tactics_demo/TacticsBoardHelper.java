package com.xs.lightpuzzle.demo.a_tactics_demo;

import com.xs.lightpuzzle.demo.a_tactics_demo.data.TeamUserPositionVO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author xs
 * @description
 * @since 2019/2/22
 */

public class TacticsBoardHelper {
    public static HashMap<String, String> mLocationHashMap = new HashMap<>();

    static {
        mLocationHashMap.put("GK", "GK守门员");
        mLocationHashMap.put("LB", "LB左边后卫");
        mLocationHashMap.put("CB", "CB中后卫");
        mLocationHashMap.put("RB", "RB右边后卫");
        mLocationHashMap.put("DMF", "DMF防守型中场");
        mLocationHashMap.put("CMF", "CMF进攻型中场");
        mLocationHashMap.put("LWF", "LWF左边前");
        mLocationHashMap.put("RWF", "RWF右边前");
        mLocationHashMap.put("CF", "CF前锋");
    }

    static String TID = "";
    public static String[] FiveFormations = new String[]{"2-0-2", "1-2-1", "3-0-1"};
    public static ArrayList<ArrayList<TeamUserPositionVO>> FiveFormationsItems = new ArrayList<>();

    static {
        ArrayList<TeamUserPositionVO> FiveFirstFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 160;
        item.Type = 1;
        item.PositionName = "DF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 160;
        item.Type = 1;
        item.PositionName = "DF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "LW";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "RW";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        FiveFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        FiveFirstFormationsItems.add(item);
        FiveFormationsItems.add(FiveFirstFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> FiveSecondFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "LW";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "RW";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        FiveSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        FiveSecondFormationsItems.add(item);
        FiveFormationsItems.add(FiveSecondFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> FiveThirdFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 160;
        item.Type = 1;
        item.PositionName = "LB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 160;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 160;
        item.Type = 1;
        item.PositionName = "RB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        FiveThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        FiveThirdFormationsItems.add(item);
        FiveFormationsItems.add(FiveThirdFormationsItems);
    }

    public static String[] SevenFormations = new String[]{"3-2-1", "2-3-1", "3-1-2"};
    public static ArrayList<ArrayList<TeamUserPositionVO>> SevenFormationsItems = new ArrayList<>();

    static {
        ArrayList<TeamUserPositionVO> SevenFirstFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "LB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "RB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "LM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "RM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        SevenFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        SevenFirstFormationsItems.add(item);
        SevenFormationsItems.add(SevenFirstFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> SevenSecondFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "LM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "DM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "RM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        SevenSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        SevenSecondFormationsItems.add(item);
        SevenFormationsItems.add(SevenSecondFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> SevenThirdFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "LB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "RB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "DM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "SS";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        SevenThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 0;
        item.PosY = 0;
        item.Type = 0;
        item.PositionName = "BD";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        SevenThirdFormationsItems.add(item);
        SevenFormationsItems.add(SevenThirdFormationsItems);
    }

    public static String[] EightFormations = new String[]{"3-3-1", "3-2-2", "2-4-1"};
    public static ArrayList<ArrayList<TeamUserPositionVO>> EightFormationsItems = new ArrayList<>();
    static {
        ArrayList<TeamUserPositionVO> EightFirstFormationsItems = new ArrayList<>();
        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "LB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "RB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "DCM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "ACM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "DCM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        EightFirstFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        EightFirstFormationsItems.add(item);
        EightFormationsItems.add(EightFirstFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> EightSecondFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "LB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "RB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "ACM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "ACM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        EightSecondFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        EightSecondFormationsItems.add(item);
        EightFormationsItems.add(EightSecondFormationsItems);
    }

    static {
        ArrayList<TeamUserPositionVO> EightThirdFormationsItems = new ArrayList<>();

        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 1;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 2;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 190;
        item.Type = 1;
        item.PositionName = "CB";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 3;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 150;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "LM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 4;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 220;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "ACM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 5;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 380;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "ACM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 6;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 450;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "RM";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 7;
        EightThirdFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = true;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.TID = TID;
        item.UserID = null;
        item.PositionID = 8;
        EightThirdFormationsItems.add(item);
        EightFormationsItems.add(EightThirdFormationsItems);
    }

    // 600x300
    /**
     * 足球场 11 人制，每个位置上的数据信息
     */
    public static ArrayList<TeamUserPositionVO> TotalFormationsItems = new ArrayList<>();

    static {
        TeamUserPositionVO item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 300;
        item.PosY = 250;
        item.Type = 0;
        item.PositionName = "GK";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 80;
        item.PosY = 210;
        item.Type = 1;
        item.PositionName = "LB";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 200;
        item.PosY = 210;
        item.Type = 1;
        item.PositionName = "CB";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 400;
        item.PosY = 210;
        item.Type = 1;
        item.PositionName = "CB";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 520;
        item.PosY = 210;
        item.Type = 1;
        item.PositionName = "RB";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 150;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "DMF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 300;
        item.PosY = 120;
        item.Type = 3;
        item.PositionName = "CMF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 450;
        item.PosY = 150;
        item.Type = 3;
        item.PositionName = "DMF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 120;
        item.PosY = 80;
        item.Type = 4;
        item.PositionName = "LWF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 300;
        item.PosY = 50;
        item.Type = 4;
        item.PositionName = "CF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        item = new TeamUserPositionVO();
        item.IsShow = false;
        item.PosX = 480;
        item.PosY = 80;
        item.Type = 4;
        item.PositionName = "RWF";
        item.UserID = null;
        TotalFormationsItems.add(item);

        for (TeamUserPositionVO teamUserPositionVO : TotalFormationsItems) {
            teamUserPositionVO.IsShow = true;
        }
    }
}
