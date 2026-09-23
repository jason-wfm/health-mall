// +----------------------------------------------------------------------
// | ShopSuite商城系统 [ 赋能开发者，助力企业发展 ]
// +----------------------------------------------------------------------
// | 版权所有 随商信息技术（上海）有限公司
// +----------------------------------------------------------------------
// | 未获商业授权前，不得将本软件用于商业用途。禁止整体或任何部分基础上以发展任何派生版本、
// | 修改版本或第三方版本用于重新分发。
// +----------------------------------------------------------------------
// | 官方网站: https://www.shopsuite.cn  https://www.modulithshop.cn
// +----------------------------------------------------------------------
// | 版权和免责声明:
// | 本公司对该软件产品拥有知识产权（包括但不限于商标权、专利权、著作权、商业秘密等）
// | 均受到相关法律法规的保护，任何个人、组织和单位不得在未经本团队书面授权的情况下对所授权
// | 软件框架产品本身申请相关的知识产权，禁止用于任何违法、侵害他人合法权益等恶意的行为，禁
// | 止用于任何违反我国法律法规的一切项目研发，任何个人、组织和单位用于项目研发而产生的任何
// | 意外、疏忽、合约毁坏、诽谤、版权或知识产权侵犯及其造成的损失 (包括但不限于直接、间接、
// | 附带或衍生的损失等)，本团队不承担任何法律责任，本软件框架只能用于公司和个人内部的
// | 法律所允许的合法合规的软件产品研发，详细见https://www.modulithshop.cn/policy
// +----------------------------------------------------------------------
package com.wechuang.mallshop.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.PageUserForm;
import com.wechuang.mallshop.sys.model.req.PageUserFormListReq;
import com.wechuang.mallshop.sys.repository.PageUserFormRepository;
import com.wechuang.mallshop.sys.service.PageUserFormService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 页面表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
@Service
public class PageUserFormServiceImpl extends BaseServiceImpl<PageUserFormRepository, PageUserForm, PageUserFormListReq> implements PageUserFormService {


    @Override
    public boolean doSurvey(PageUserForm pageUserForm) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }
        QueryWrapper<PageUserForm> pageUserFormQueryWrapper = new QueryWrapper<>();
        pageUserFormQueryWrapper.eq("user_id", user.getUserId());
        pageUserFormQueryWrapper.eq("page_id", pageUserForm.getPageId());
        long count = count(pageUserFormQueryWrapper);

        if (count > 0) {
            throw new BusinessException(__("已提交，请勿重复提交！"));
        }
        pageUserForm.setUserId(user.getUserId());
        pageUserForm.setUserNickname(user.getUserNickname());

        return save(pageUserForm);
    }

    @Override
    public IPage<PageUserForm> getList(PageUserFormListReq pageUserFormListReq) {
        IPage<PageUserForm> lists = lists(pageUserFormListReq);
        List<PageUserForm> records = lists.getRecords();

        if (CollectionUtil.isNotEmpty(records)) {
            for (PageUserForm userForm : records) {
                String formData = userForm.getUserFormData();
                JSONArray jsonArray = JSONUtil.parseArray(formData);
                List<Map> list = new ArrayList<>();

                if (!jsonArray.isEmpty()) {
                    for (Object o : jsonArray) {
                        JSONObject obj = JSONUtil.parseObj(o);
                        Map map = new HashMap();
                        map.put("key", obj.get("key"));
                        map.put("value", obj.get("value"));
                        list.add(map);
                    }
                }
                userForm.setKeyVlaueMap(list);
            }
        }

        return lists;
    }
}
