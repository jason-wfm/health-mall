package com.wechuang.mallshop.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.model.entity.DictBase;
import com.wechuang.mallshop.sys.model.entity.DictItem;
import com.wechuang.mallshop.sys.model.req.DictItemListReq;
import com.wechuang.mallshop.sys.repository.DictItemRepository;
import com.wechuang.mallshop.sys.service.DictBaseService;
import com.wechuang.mallshop.sys.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 字典项表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2022-12-05
 */
@Service
public class DictItemServiceImpl extends BaseServiceImpl<DictItemRepository, DictItem, DictItemListReq> implements DictItemService {
    @Autowired
    private DictBaseService dictBaseService;

    @Override
    public boolean remove(Serializable dictItemId) {
        DictItem dictItem = get(dictItemId);

        if (dictItem.getDictItemBuildin()) {
            throw new BusinessException(__("系统内置，不可删除"));
        }

        return super.remove(dictItemId);
    }

    public List<String> getLists(List<Long> dictItemIds, String dictId) {
        List<DictItem> dictItemList = find(new QueryWrapper<DictItem>().in("dict_item_id", dictItemIds).eq("dict_id", dictId));
        List<String> dictIds = CommonUtil.column(dictItemList, DictItem::getDictId);

        DictBase dictBase = dictBaseService.get(dictId);
        List<DictBase> dictBaseRow = dictBaseService.gets(dictIds);
        List<DictBase> dictBaseList = dictBaseService.find(new QueryWrapper<DictBase>().in("dict_enable", Arrays.asList(1)).eq("dict_id", dictId));

        return dictIds;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean addDictItem(DictItem dictItem) {
        long currentTime = new Date().getTime();

        if (!add(dictItem)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        return true;
    }
}
