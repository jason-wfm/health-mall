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
package com.wechuang.mallshop.sys.model.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户行为日志表
 * </p>
 *
 * @author Xinze
 * @since 2021-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "用户行为日志表参数")
public class LogActionAddReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "玩家编号")
    private Integer userId;

    @Schema(description = "角色账户")
    private String userAccount;

    @Schema(description = "角色名称")
    private String userName;

    @Schema(description = "请求名称")
    private String logName;

    @Schema(description = "行为id:protocal_id -> rights_id")
    private Integer actionId;

    @Schema(description = "操作类型编号:right_parent_id")
    private Integer actionTypeId;

    @Schema(description = "请求接口")
    private String logUrl;

    @Schema(description = "请求方法")
    private String logMethod;

    @Schema(description = "请求的参数")
    private String logParam;

    @Schema(description = "日志IP")
    private String logIp;

    @Schema(description = "日志日期")
    private Date logDate;

    @Schema(description = "记录时间")
    private Date logTime;


}
