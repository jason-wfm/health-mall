package com.wechuang.mallshop.common.pojo.dto;

import java.util.HashMap;
import java.util.Map;

public class ActivityType {

    private static final Map<Integer, String> activityMap = new HashMap<>();

    static {
        activityMap.put(1101, "加价购");
        activityMap.put(1102, "店铺满赠-小礼品");
        activityMap.put(1103, "限时折扣");
        activityMap.put(1104, "优惠套装");
        activityMap.put(1105, "优惠券");
        activityMap.put(1106, "砸金蛋");
        activityMap.put(1107, "满减");
        activityMap.put(1108, "满返");
        activityMap.put(1109, "积分换购");
        activityMap.put(1132, "A+B组合套餐");
        activityMap.put(1130, "礼包活动");
        activityMap.put(1131, "市场活动");
        activityMap.put(1121, "幸运大抽奖");
        activityMap.put(1122, "秒杀");
        activityMap.put(1123, "拼团");
        activityMap.put(1124, "砍价");
        activityMap.put(1125, "一元购");
        activityMap.put(1126, "团购");
        activityMap.put(1133, "多件折");
        activityMap.put(1135, "阶梯价");
        activityMap.put(1136, "多倍积分");
        activityMap.put(1137, "弹窗活动");
        activityMap.put(1140, "折上折");
    }

    public static String getActivityName(Integer activityTypeId) {
        return activityMap.get(activityTypeId);
    }

}
