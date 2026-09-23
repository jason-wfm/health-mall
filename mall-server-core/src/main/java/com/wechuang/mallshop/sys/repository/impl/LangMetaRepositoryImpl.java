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
package com.wechuang.mallshop.sys.repository.impl;

import cn.hutool.crypto.SecureUtil;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.TransUtil;
import com.wechuang.mallshop.core.web.repository.impl.BaseRepositoryImpl;
import com.wechuang.mallshop.sys.dao.LangMetaDao;
import com.wechuang.mallshop.sys.model.entity.LangMeta;
import com.wechuang.mallshop.sys.repository.LangMetaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;


/**
 * <p>
 * 用户数据翻译扩展表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2024-07-15
 */
@Repository
public class LangMetaRepositoryImpl extends BaseRepositoryImpl<LangMetaDao, LangMeta> implements LangMetaRepository {
    private String getValue(String dataId, String metaKey, String query) {
        String metaId = String.format("%s-%s", dataId, metaKey);
        LangMeta row = get(metaId);

        if (row != null) {
            String metaDataType = row.getMetaDatatype().toLowerCase();

            if (!query.equals(row.getMetaOri())) {
                return null;
            }

            //判断待翻译内容是否变更
            if (!row.getMetaOri().equals(query)) {
                remove(metaId);

                return null;
            }

            return row.getMetaValue();
        } else {

        }

        return null;
    }

    /**
     * $query, $lang_index='en_GB', $from_index='zh_CN', $table_name='default', $primary_key='default', $column_name=null, $store_id=0
     */
    public String getTranslate(String query, String langIndex, String fromIndex, String tableName, String primaryKey, String columnName, int storeId) {
        if (langIndex.equals(fromIndex)) {
            return query;
        }

        if (StringUtils.isEmpty(query)) {
            return query;
        }

        if (columnName == null) {
            columnName = query;
        }

        if (CheckUtil.isNotEmpty(query) && query instanceof String) {
            //query = stripTags(query);
            String dataId = SecureUtil.md5(String.format("%s-%s-%s", tableName, primaryKey, columnName));

            String to = "en";
            switch (langIndex) {
                case "en_US":
                case "en_GB":
                    langIndex = "en_GB";
                    to = "en";
                    break;
                case "es_ES":
                case "es_MX":
                    langIndex = "es_MX";
                    to = "spa";
                    break;
                case "th_TH":
                    to = "th";
                    break;
                case "ar_SA":
                    to = "ara";
                    break;
                case "vi_VN":
                    to = "vie";
                    break;
                case "tr_TR":
                    to = "tr";
                    break;
                case "ja_JP":
                    to = "jp";
                    break;
                case "id_ID":
                    to = "id";
                    break;
                case "de_DE":
                    to = "de";
                    break;
                case "fr_FR":
                    to = "fra";
                    break;
                case "pt_PT":
                    to = "pt";
                    break;
                case "it_IT":
                    to = "it";
                    break;
                case "ru_RU":
                    to = "ru";
                    break;
                case "ro_RO":
                    to = "rom";
                    break;
                case "az_AZ":
                    to = "aze";
                    break;
                case "el_GR":
                    to = "el";
                    break;
                case "fi_FI":
                    to = "fin";
                    break;
                case "lv_LV":
                    to = "lav";
                    break;
                case "nl_NL":
                    to = "nl";
                    break;
                case "da_DK":
                    to = "dan";
                    break;
                case "sr_RS":
                    to = "src";
                    break;
                case "pl_PL":
                    to = "pl";
                    break;
                case "uk_UA":
                    to = "ukr";
                    break;
                case "kk_KZ":
                    to = "kaz";
                    break;
                case "my_MM":
                    to = "bur";
                    break;
                case "lo_LA":
                    to = "lao";
                    break;
                case "zh_HK":
                case "zh_TW":
                    to = "cht";
                    break;
                case "ko_KR":
                    to = "kor";
                    break;
                case "ms_MY":
                    to = "may";
                    break;
                default:
                    return query;
            }

            String from = "zh";
            switch (fromIndex) {
                case "zh_CN":
                    from = "zh";
                    break;
                case "en_US":
                case "en_GB":
                    from = "en";
                    break;
                case "es_ES":
                case "es_MX":
                    from = "spa";
                    break;
                case "ar_SA":
                    from = "ara";
                    break;
                case "vi_VN":
                    from = "vie";
                    break;
                case "tr_TR":
                    from = "tr";
                    break;
                case "ja_JP":
                    from = "jp";
                    break;
                case "id_ID":
                    from = "id";
                    break;
                case "de_DE":
                    from = "de";
                    break;
                case "fr_FR":
                    from = "fra";
                    break;
                case "pt_PT":
                    from = "pt";
                    break;
                case "it_IT":
                    from = "it";
                    break;
                case "ru_RU":
                    from = "ru";
                    break;
                case "ro_RO":
                    from = "rom";
                    break;
                case "az_AZ":
                    from = "aze";
                    break;
                case "el_GR":
                    from = "el";
                    break;
                case "fi_FI":
                    from = "fin";
                    break;
                case "lv_LV":
                    from = "lav";
                    break;
                case "nl_NL":
                    from = "nl";
                    break;
                case "da_DK":
                    from = "dan";
                    break;
                case "sr_RS":
                    from = "src";
                    break;
                case "pl_PL":
                    from = "pl";
                    break;
                case "uk_UA":
                    from = "ukr";
                    break;
                case "kk_KZ":
                    from = "kaz";
                    break;
                case "my_MM":
                    from = "bur";
                    break;
                case "lo_LA":
                    from = "lao";
                    break;
                case "zh_HK":
                case "zh_TW":
                    from = "cht";
                    break;
                case "ko_KR":
                    from = "kor";
                    break;
                case "ms_MY":
                    from = "may";
                    break;
                default:
                    return query;
            }

            String value = getValue(dataId, langIndex, query);

            if (value == null) {
                String res = TransUtil.translate(query, from, to);

                if (CheckUtil.isNotEmpty(res)) {
                    value = res;

                    // 保存翻译结果
                    String metaId = String.format("%s-%s", dataId, langIndex);

                    LangMeta fieldRows = new LangMeta();
                    fieldRows.setMetaId(metaId);
                    fieldRows.setDataId(dataId);
                    fieldRows.setMetaKey(langIndex);
                    fieldRows.setMetaValue(value);
                    fieldRows.setMetaDatatype("string");

                    fieldRows.setMetaOri(query);
                    fieldRows.setTableName(tableName);
                    fieldRows.setPrimaryKey(primaryKey);
                    fieldRows.setColumnName(columnName);
                    fieldRows.setStoreId(storeId);

                    save(fieldRows);
                } else {
                    value = query;
                }
            }

            return value;
        } else {
            return query;
        }
    }
}
