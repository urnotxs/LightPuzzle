package com.xs.lightpuzzle.puzzle.data.lowdata;

import android.graphics.PointF;

import java.io.Serializable;

/**
 * Created by Lin on 2018/4/12.
 */

public class QrCodeData implements Serializable, Cloneable {

    private String qrCodePic;

    private PointF[] qrCodePoint;

    public String getQrCodePic() {
        return qrCodePic;
    }

    public void setQrCodePic(String qrCodePic) {
        this.qrCodePic = qrCodePic;
    }

    public PointF[] getQrCodePoint() {
        return qrCodePoint;
    }

    public void setQrCodePoint(PointF[] qrCodePoint) {
        this.qrCodePoint = qrCodePoint;
    }
}
