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
package com.wechuang.mallshop.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechuang.mallshop.core.web.service.IBaseService;
import com.wechuang.mallshop.shop.model.entity.StoreBase;
import com.wechuang.mallshop.shop.model.req.StoreBaseAddReq;
import com.wechuang.mallshop.shop.model.req.StoreBaseEditReq;
import com.wechuang.mallshop.shop.model.req.StoreBaseListReq;
import com.wechuang.mallshop.shop.model.res.StoreBaseRes;
import com.wechuang.mallshop.shop.model.vo.StoreDetailVo;
import com.wechuang.mallshop.trade.model.vo.StoreInfoVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺基础信息表 服务类
 * </p>
 *
 * @author Xinze
 * @since 2018-07-04
 */
public interface StoreBaseService extends IBaseService<StoreBase, StoreBaseListReq> {

    /**
     * 获取店铺信息
     *
     * @param storeIds
     * @return
     */
    List<StoreInfoVo> getStore(List<Integer> storeIds);

    /**
     * 判断用户是否开店,获取店铺Id
     *
     * @param userId
     * @return
     */
    Integer getStoreId(Integer userId);

    /**
     * 店铺基础信息表-添加
     *
     * @param storeBaseAddReq
     * @return
     */
    boolean addStore(StoreBaseAddReq storeBaseAddReq);

    /**
     * 店铺基础信息表-编辑
     *
     * @param storeBaseEditReq
     * @return
     */
    boolean editStore(StoreBaseEditReq storeBaseEditReq);

    /**
     * 店铺基础信息表-通过store_id删除
     *
     * @param storeId
     * @return
     */
    boolean removeStore(Integer storeId);

    /**
     * 店铺基础信息表-分页列表查询
     *
     * @param storeBaseListReq
     * @return
     */
    IPage<StoreBaseRes> getList(StoreBaseListReq storeBaseListReq);

    // 获取店铺详情
    StoreDetailVo getStoreDetail(Integer storeId);

    IPage<StoreBaseRes> getStoreList(StoreBaseListReq storeBaseListReq);

    /**
     * 修改状态
     * @param storeBase
     * @return
     */
    boolean editState(StoreBase storeBase);

    /**
     * 店铺审核
     * @param storeBase
     * @return
     */
    boolean editStateId(StoreBase storeBase);

    /**
     * 店铺开通(资料审核通过→已开通运营, 3240→3250)
     * @param storeId
     * @return
     */
    boolean openStore(Integer storeId);

    /**
     * 商家入驻
     * @param storeBaseEditReq
     * @return
     */
    boolean storeEnter(StoreBaseEditReq storeBaseEditReq);

    /**
     * 店铺信息
     * @return
     */
    StoreDetailVo getStoreInfo();

    /**
     * 店铺街过滤
     * @return
     */
    List<Map> getFilterOpt();
}
