package com.xs.lightpuzzle.data.net;

import com.xs.lightpuzzle.data.entity.Font;
import com.xs.lightpuzzle.data.entity.TemplateSet;

import java.util.List;

/**
 * Created by xs on 2018/11/5.
 */

public interface RestApi {

    List<Font> getFonts();

    List<TemplateSet> getTemplates();
}
