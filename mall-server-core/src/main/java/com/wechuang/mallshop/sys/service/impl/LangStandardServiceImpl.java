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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.LangStandard;
import com.wechuang.mallshop.sys.model.req.LangStandardEditReq;
import com.wechuang.mallshop.sys.model.req.LangStandardListReq;
import com.wechuang.mallshop.sys.repository.LangStandardRepository;
import com.wechuang.mallshop.sys.service.LangStandardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 语言翻译表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2024-07-15
 */
@Service
public class LangStandardServiceImpl extends BaseServiceImpl<LangStandardRepository, LangStandard, LangStandardListReq> implements LangStandardService {

    @Autowired
    private LangStandardRepository langStandardRepository;

    @Override
    public boolean saveOrUpdateStandard(LangStandardEditReq langStandardEditReq) {
        LangStandard langStandard = BeanUtil.copyProperties(langStandardEditReq, LangStandard.class);

        if (StrUtil.isNotEmpty(langStandardEditReq.getZhCn())) {
            langStandard.setZhCN(langStandardEditReq.getZhCn());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getZhTw())) {
            langStandard.setZhTW(langStandardEditReq.getZhTw());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getEnGb())) {
            langStandard.setEnGB(langStandardEditReq.getEnGb());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getThTh())) {
            langStandard.setThTH(langStandardEditReq.getThTh());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getEsMx())) {
            langStandard.setEsMX(langStandardEditReq.getEsMx());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getArSa())) {
            langStandard.setArSA(langStandardEditReq.getArSa());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getViVn())) {
            langStandard.setViVN(langStandardEditReq.getViVn());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getTrTr())) {
            langStandard.setTrTR(langStandardEditReq.getTrTr());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getJaJp())) {
            langStandard.setJaJP(langStandardEditReq.getJaJp());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getIdId())) {
            langStandard.setIdID(langStandardEditReq.getIdId());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getDeDe())) {
            langStandard.setDeDE(langStandardEditReq.getDeDe());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getFrFr())) {
            langStandard.setFrFR(langStandardEditReq.getFrFr());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getPtPt())) {
            langStandard.setPtPT(langStandardEditReq.getPtPt());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getItIt())) {
            langStandard.setItIT(langStandardEditReq.getItIt());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getRuRu())) {
            langStandard.setRuRU(langStandardEditReq.getRuRu());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getRoRo())) {
            langStandard.setRoRO(langStandardEditReq.getRoRo());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getAzAz())) {
            langStandard.setAzAZ(langStandardEditReq.getAzAz());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getElGr())) {
            langStandard.setElGR(langStandardEditReq.getElGr());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getFiFi())) {
            langStandard.setFiFI(langStandardEditReq.getFiFi());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getLvLv())) {
            langStandard.setLvLV(langStandardEditReq.getLvLv());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getNlNl())) {
            langStandard.setNlNL(langStandardEditReq.getNlNl());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getDaDk())) {
            langStandard.setDaDK(langStandardEditReq.getDaDk());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getSrRs())) {
            langStandard.setSrRS(langStandardEditReq.getSrRs());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getPlPl())) {
            langStandard.setPlPL(langStandardEditReq.getPlPl());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getUkUa())) {
            langStandard.setUkUA(langStandardEditReq.getUkUa());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getKkKz())) {
            langStandard.setKkKZ(langStandardEditReq.getKkKz());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getMyMm())) {
            langStandard.setMyMM(langStandardEditReq.getMyMm());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getKoKr())) {
            langStandard.setKoKR(langStandardEditReq.getKoKr());
        }

        if (StrUtil.isNotEmpty(langStandardEditReq.getMsMy())) {
            langStandard.setMsMY(langStandardEditReq.getMsMy());
        }

        return langStandardRepository.saveOrUpdate(langStandard);
    }
}
