package com.xs.lightpuzzle.puzzle.data;

import android.graphics.PointF;

import com.xs.lightpuzzle.puzzle.param.SignatureSaveVO;

import java.io.Serializable;

/**
 * Created by xs on 2018/4/12.
 */

public class SignatureData implements Serializable, Cloneable {

    //签名信息
    private PointF[] signPoint;

    private String signPic;

    private SignatureSaveVO signatureSaveVO;

    public PointF[] getSignPoint() {
        return signPoint;
    }

    public void setSignPoint(PointF[] signPoint) {
        this.signPoint = signPoint;
    }

    public String getSignPic() {
        return signPic;
    }

    public void setSignPic(String signPic) {
        this.signPic = signPic;
    }

    public SignatureSaveVO getSignatureSaveVO() {
        return signatureSaveVO;
    }

    public void setSignatureSaveVO(SignatureSaveVO signatureSaveVO) {
        this.signatureSaveVO = signatureSaveVO;
    }
}
