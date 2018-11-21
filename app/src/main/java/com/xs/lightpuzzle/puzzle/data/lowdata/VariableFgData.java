package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;

import java.io.Serializable;

public class VariableFgData implements Serializable,Cloneable {

	//矩形区域
	public PointF[] varFgPoint;
	//图片路径
	public String varFgPic;

	public PointF[] getVarFgPoint() {
		return varFgPoint;
	}

	public void setVarFgPoint(PointF[] varFgPoint) {
		this.varFgPoint = varFgPoint;
	}

	public String getVarFgPic() {
		return varFgPic;
	}

	public void setVarFgPic(String varFgPic) {
		this.varFgPic = varFgPic;
	}
}
