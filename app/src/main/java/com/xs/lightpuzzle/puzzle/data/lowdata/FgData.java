package com.xs.lightpuzzle.puzzle.data.lowdata;

import java.io.Serializable;

/**
 * Created by Lin on 2018/4/12.
 */

public class FgData implements Serializable, Cloneable {

    private String fgPic;

    private String imageEffect;

    public String getFgPic() {
        return fgPic;
    }

    public void setFgPic(String fgPic) {
        this.fgPic = fgPic;
    }

    public String getImageEffect() {
        return imageEffect;
    }

    public void setImageEffect(String imageEffect) {
        this.imageEffect = imageEffect;
    }
}
