package com.wechuang.mallshop;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.StateCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.shop.model.entity.UserVoucher;
import com.wechuang.mallshop.shop.repository.UserVoucherRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;


@SpringBootTest
@Disabled("[healthmall-ext] 依赖真实 MySQL/Redis/RabbitMQ 且会写入数据，恢复 surefire 后禁用；需要联调时手动移除本注解")
class ShopsuiteApplicationTests {
    @Autowired
    private UserVoucherRepository userVoucherRepository;

    @Test
    void contextLoads() {
        Date now = new Date();

        UserVoucher userVoucher = new UserVoucher();
        //userVoucher.setUserVoucherId(3);
        userVoucher.setVoucherStateId(StateCode.VOUCHER_STATE_USED);
        userVoucher.setOrderId("tttt");
        userVoucher.setUserVoucherActivetime(now);

        boolean voucherStateId = userVoucherRepository.edit(userVoucher, new QueryWrapper<UserVoucher>().eq("user_voucher_id", 3).eq("voucher_state_id", StateCode.VOUCHER_STATE_UNUSED));
        if (!voucherStateId) {
            throw new BusinessException(__("订单优惠券信息失败"));
        }
    }

}
