package com.kms.cura.controller;

import com.kms.cura.entity.ConditionEntity;
import com.kms.cura.model.ConditionModel;

import java.util.List;

/**
 * Created by toanbnguyen on 6/28/2016.
 */
public class ConditionController {
    public static void initData() throws Exception {
        ConditionModel.getInstance().initData();
    }

    public static List<ConditionEntity> getAllCondition() {
        return ConditionModel.getInstance().getAllCondition();
    }
}
