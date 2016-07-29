package com.wen.magi.baseframe.bundles;

import java.io.Serializable;

/**
 * Created by MVEN on 16/6/14.
 */
public class BaseBundleParams implements Serializable {
    public static final String PARAM_SKEY = "params_key";

    /**
     * FROM，TARGET
     * 用来纪录跳转路径
     */
    public static final String FROM = "from_component";

    public static final String TARGET = "target_component";
}
