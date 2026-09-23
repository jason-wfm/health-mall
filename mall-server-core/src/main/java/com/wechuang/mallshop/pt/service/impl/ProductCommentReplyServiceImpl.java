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
package com.wechuang.mallshop.pt.service.impl;

import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.pt.model.entity.ProductComment;
import com.wechuang.mallshop.pt.model.entity.ProductCommentReply;
import com.wechuang.mallshop.pt.model.req.ProductCommentReplyListReq;
import com.wechuang.mallshop.pt.repository.ProductCommentReplyRepository;
import com.wechuang.mallshop.pt.repository.ProductCommentRepository;
import com.wechuang.mallshop.pt.service.ProductCommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 商品评价回复表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2021-10-18
 */
@Service
public class ProductCommentReplyServiceImpl extends BaseServiceImpl<ProductCommentReplyRepository, ProductCommentReply, ProductCommentReplyListReq> implements ProductCommentReplyService {

    @Autowired
    private ProductCommentRepository productCommentRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCommentReply(ProductCommentReply productCommentReply) {
        ContextUser user = ContextUtil.getLoginUser();

        if (user == null) {
            throw new BusinessException(ResultCode.NEED_LOGIN);
        }

        ProductComment productComment = productCommentRepository.get(productCommentReply.getCommentId());

        if (productComment == null) {
            throw new BusinessException(__("商品评价信息不存在"));
        }

        if (!user.isPlatform() && !CheckUtil.checkDataRights(user.getStoreId(), productComment, ProductComment::getStoreId)) {
            throw new BusinessException(ResultCode.FORBIDDEN);
        }

        productCommentReply.setUserId(user.getUserId());
        productCommentReply.setUserName(user.getUserNickname());
        productCommentReply.setUserIdTo(productComment.getUserId());
        productCommentReply.setUserNameTo(productComment.getUserName());
        productCommentReply.setCommentReplyEnable(true);
        productCommentReply.setCommentReplyIsadmin(true);

        if (!save(productCommentReply)) {
            throw new BusinessException(__("保存商品评价回复失败！"));
        }

        if (!productComment.getCommentIsReply()) {
            productComment.setCommentIsReply(true);

            if (!productCommentRepository.edit(productComment)) {
                throw new BusinessException(__("修改商品评价信息失败！"));
            }
        }


        return true;
    }
}
