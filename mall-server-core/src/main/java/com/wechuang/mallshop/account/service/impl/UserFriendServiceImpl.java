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
package com.wechuang.mallshop.account.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechuang.mallshop.account.model.entity.*;
import com.wechuang.mallshop.account.model.output.FriendsInfoOutput;
import com.wechuang.mallshop.account.model.req.UserFriendListReq;
import com.wechuang.mallshop.account.model.vo.UserInfoVo;
import com.wechuang.mallshop.account.repository.UserFriendRepository;
import com.wechuang.mallshop.account.repository.UserInfoRepository;
import com.wechuang.mallshop.account.service.*;
import com.wechuang.mallshop.common.api.ResultCode;
import com.wechuang.mallshop.common.exception.BusinessException;
import com.wechuang.mallshop.common.utils.CheckUtil;
import com.wechuang.mallshop.common.utils.CommonUtil;
import com.wechuang.mallshop.common.utils.ContextUtil;
import com.wechuang.mallshop.common.web.ContextUser;
import com.wechuang.mallshop.core.web.service.impl.BaseServiceImpl;
import com.wechuang.mallshop.sys.service.ConfigBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.wechuang.mallshop.common.utils.I18nUtil.__;

/**
 * <p>
 * 用户好友关系表 服务实现类
 * </p>
 *
 * @author Xinze
 * @since 2024-06-02
 */
@Service
public class UserFriendServiceImpl extends BaseServiceImpl<UserFriendRepository, UserFriend, UserFriendListReq> implements UserFriendService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private UserGroupRelService userGroupRelService;
    @Autowired
    private UserGroupService userGroupService;
    @Autowired
    private UserZoneRelService userZoneRelService;
    @Autowired
    private UserZoneService userZoneService;
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private ConfigBaseService configBaseService;
    @Autowired
    private UserSnsService userSnsService;
    @Autowired
    private UserFriendService userFriendService;
    @Autowired
    private UserFriendRepository userFriendRepository;

    @Override
    public void fixAgreeState(List<Map> items, Integer user_id) {
//        $friend_id = array_column($items, 'user_id');

        List<Integer> friend_id = items.stream().map(s -> Convert.toInt(s.get("user_id"))).collect(Collectors.toList());
        Map story_like_rel_row = new HashMap();
        if (CollUtil.isNotEmpty(friend_id) && user_id != null) {

            QueryWrapper<UserFriend> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", user_id);
            wrapper.in("friend_id", friend_id);
            List<UserFriend> story_like_rows = find(wrapper);
            for (UserFriend story_like_row : story_like_rows) {
                Integer friendId = story_like_row.getFriendId();
                story_like_rel_row.put(friendId, friendId);

            }
        }
        for (Map item : items) {
            Integer userId = Convert.toInt(item.get("user_id"));
            if (userId.equals(Convert.toInt(story_like_rel_row.get(userId)))) {
                item.put("is_follow", 1);

            } else {
                item.put("is_follow", 0);
            }
        }

    }


    @Override
    public UserFriend agree() {
        Integer friend_id = Convert.toInt(getParameter("friend_id", getParameter("uid")));
        ContextUser user = ContextUtil.getLoginUser();
        Integer user_id = user.getUserId();
        Integer group_id = getParameter("group_id", getParameter("group", Integer.class));
        String friend_note = getParameter("friend_note");
        Integer user_friend_id = addFriend(user_id, friend_id, friend_note, group_id);

        UserFriend userFriend = new UserFriend();
        userFriend.setUserFriendId(user_friend_id);
        return userFriend;
    }

    @Override
    @Transactional
    public UserFriend refuse() {

        Integer friend_id = Convert.toInt(getParameter("friend_id", getParameter("uid")));
        // 权限判断
        ContextUser user = ContextUtil.getLoginUser();
        Integer user_id = user.getUserId();

        // 判断状态是否双向关注
        UserFriend invite_row = getInvite(user_id, friend_id);
        if (invite_row != null) {
            // 修改邀请信息
            Integer user_friend_id = Convert.toInt(invite_row.getUserFriendId());

            UserFriend userFriend = new UserFriend();
            userFriend.setUserFriendId(user_friend_id);
            userFriend.setFriendState(1);

            if (!userFriendService.edit(userFriend)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }

        //修改好友和粉丝状态
        //修改关注人数和粉丝人数
        if (!userSnsService.updateUserFriendNum(user_id, friend_id, 1, false)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        //单向关注
        QueryWrapper<UserFriend> reQueryWrapper = new QueryWrapper<>();
        reQueryWrapper.eq("user_id", user_id)
                .eq("friend_id", friend_id);
        UserFriend UserFriend = findOne(reQueryWrapper);
        if (!userFriendRepository.remove(UserFriend.getUserFriendId())) {
            throw new BusinessException(ResultCode.FAILED);
        }

        List<Integer> list = Arrays.asList(friend_id);

        UserFriend userFriend = new UserFriend();
        userFriend.setList(list);

        return userFriend;
    }


    /**
     * 读取好友列表，包含分组，群组等等
     *
     * @param user_id
     * @param friend_id
     * @return
     */
    @Override
    @Transactional
    public Integer addFriend(Integer user_id, Integer friend_id, String friend_note, Integer group_id) {

        friend_note = ObjectUtil.defaultIfBlank(friend_note, "");
        group_id = ObjectUtil.defaultIfNull(group_id, 0);

        if (ObjectUtil.equal(user_id, friend_id)) {
            return 1;
        }

        Date now = new Date();

        UserFriend data = new UserFriend();
        data.setUserId(user_id);
        data.setFriendId(friend_id);
        data.setFriendNote(friend_note);    //备注名称
        data.setUserFriendAddtime(now.getTime());

        // 双向关注
        UserFriend invite_row = getInvite(user_id, friend_id);
        if (invite_row != null) {
            // 修改邀请信息
            BeanUtil.copyProperties(invite_row, data);
            data.setFriendState(2); // 关注状态(ENUM):1-单向关注;2-双向关注
            data.setFriendInvite(2); // 邀请状态(ENUM):0-新邀请;2-处理完成后邀请 即不添加新的对象，表明user和friend已互相关注
        }
        //单向关注
        else {
            data.setFriendState(1); // 关注状态(ENUM):1-单向关注;2-双向关注
            data.setFriendInvite(0); // 邀请状态(ENUM):0-新邀请;2-处理完成后邀请
        }

        if (!userFriendRepository.saveOrUpdate(data)) {
            throw new BusinessException(ResultCode.FAILED);
        }

        // 分组信息
        if (CheckUtil.isNotEmpty(group_id) && CheckUtil.isNotEmpty(friend_id)) {
            UserGroupRel userGroupRel = new UserGroupRel();
            userGroupRel.setUserId(user_id);
            userGroupRel.setGroupId(group_id);
            if (!userGroupRelService.add(userGroupRel)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }

        Integer user_friend_id = data.getUserFriendId();
        if (CheckUtil.isNotEmpty(user_friend_id)) {
            // 修改好友数量
            if (!userSnsService.updateUserFriendNum(user_id, friend_id, 1, true)) {
                throw new BusinessException(ResultCode.FAILED);
            }
        }

        return user_friend_id;
    }

    @Override
    public FriendsInfoOutput getFriendsInfo(QueryWrapper<UserFriend> friendQueryWrapper, Integer user_id, Integer page, Integer rows) {
        Page<UserFriend> friendPage = lists(friendQueryWrapper, page, rows);

        List<UserFriend> friend_rows = friendPage.getRecords();


        //fixUserAvatar
        List<Integer> userIds = CommonUtil.column(friend_rows, UserFriend::getFriendId);
        if (CollUtil.isNotEmpty(userIds)) {
            String user_no_avatar = configBaseService.getConfig("user_no_avatar");
            List<UserInfo> user_info_rows = userInfoRepository.gets(userIds);
            for (UserFriend friend_row : friend_rows) {
                Optional<UserInfo> userInfoOpl = user_info_rows.stream().filter(s -> ObjectUtil.equal(friend_row.getFriendId(), s.getUserId())).findFirst();
                UserInfo userInfo = userInfoOpl.orElseGet(UserInfo::new);

                if (CheckUtil.isNotEmpty(userInfo.getUserId())) {
                    String user_nickname = userInfo.getUserNickname();
                    if (StrUtil.isBlank(user_nickname)) user_nickname = __("佚名");

                    String user_avatar = userInfo.getUserAvatar();
                    if (StrUtil.isBlank(user_avatar)) user_avatar = user_no_avatar;

                    friend_row.setUsername(user_nickname);
                    friend_row.setAvatar(user_avatar);
                    friend_row.setAccount(userInfo.getUserAccount());
                    //friend_row.setLevelName(userLevel.getUserLevelName());
                }
            }
        }

        // 读取用户组
        QueryWrapper<UserGroup> groupQueryWrapper = new QueryWrapper<>();
        groupQueryWrapper.eq("user_id", user_id);
        List<UserGroup> userGroups = userGroupService.find(groupQueryWrapper);

        for (UserGroup group_row : userGroups) {
            //group_row.put("groupname", group_row.get("group_name"));
        }

        List<Integer> group_ids = userGroups.stream().map(s -> s.getGroupId()).distinct().collect(Collectors.toList());
        List<Integer> group_user_ids = new ArrayList<>();

        if (CollUtil.isNotEmpty(group_ids)) {
            QueryWrapper<UserGroupRel> relQueryWrapper = new QueryWrapper<>();
            relQueryWrapper.in("group_id", group_ids);
            List<UserGroupRel> group_rel_rows = userGroupRelService.find(relQueryWrapper);
            group_user_ids = group_rel_rows.stream().map(s -> s.getUserId()).distinct().collect(Collectors.toList());

            for (UserGroupRel group_rel_row : group_rel_rows) {
                Integer group_id = group_rel_row.getGroupId();
                Integer userId = group_rel_row.getUserId();

                Optional<UserGroup> groupOpl = userGroups.stream().filter(s -> ObjectUtil.equal(Convert.toInt(s.getGroupId()), group_id)).findFirst();
                if (groupOpl.isPresent()) {
                    continue;
                }

                UserGroup group = groupOpl.get();
                List<UserFriend> group_rel_list = (List<UserFriend>) ObjectUtil.defaultIfNull(group.getList(), new ArrayList<>());
                if (CollUtil.isEmpty(group_rel_list)) {
                    group.setList(group_rel_list);
                }

                Optional<UserFriend> friendOpl = friend_rows.stream().filter(s -> ObjectUtil.equal(Convert.toInt(s.getUserFriendId()), userId)).findFirst();
                if (friendOpl.isPresent()) {
                    group_rel_list.add(friendOpl.get());
                }
            }
        }

        // 未分组判断
        List<UserFriend> none_group_friend_rows = new ArrayList<>();
        for (UserFriend friend_row : friend_rows) {
            if (!group_user_ids.contains(friend_row.getFriendId())) {
                none_group_friend_rows.add(friend_row);
            }
        }

        if (CollUtil.isNotEmpty(none_group_friend_rows)) {
            UserGroup temp = new UserGroup();
            temp.setList(none_group_friend_rows);
            temp.setGroupname("未分组");
            temp.setId(0);
            temp.setOnline(0);
            userGroups.add(temp);
        }

        // 群组
        QueryWrapper<UserZoneRel> zoneRelQueryWrapper = new QueryWrapper<>();
        zoneRelQueryWrapper.eq("user_id", user_id);
        List<UserZoneRel> user_zone_rel_rows = userZoneRelService.find(zoneRelQueryWrapper);

        List<Integer> zone_ids = user_zone_rel_rows.stream().map(s -> s.getZoneId()).distinct().collect(Collectors.toList());
        List<UserZone> userZones = userZoneService.gets(zone_ids);


        FriendsInfoOutput data = new FriendsInfoOutput();
        data.setFriend(userGroups);
        data.setGroup(new ArrayList());

        if (CollUtil.isNotEmpty(userZones)) {
            for (UserZone zone_row : userZones) {
                zone_row.setAvatar("//tva3.sinaimg.cn/crop.64.106.361.361.50/7181dbb3jw8evfbtem8edj20ci0dpq3a.jpg");
                zone_row.setGroupname(zone_row.getZoneName());
                zone_row.setId(userMessageService.getPlantformUid("zone-" + zone_row.getZoneId()));
            }

            data.setGroup(userZones);
        }

        UserInfo userInfo = userInfoRepository.get(user_id);
        UserInfoVo userInfoVo = BeanUtil.copyProperties(userInfo, UserInfoVo.class);

        if (ObjectUtil.isNotEmpty(userInfo)) {
            userInfoVo.setAvatar(userInfo.getUserAvatar());
            //userInfoVo.setRemark(userInfo.getUserSign());
            userInfoVo.setUsername(userInfo.getUserNickname());
            userInfoVo.setStatus("online");

            userInfoVo.setId(userMessageService.getPlantformUid(Convert.toStr(userInfo.getUserId())));
        }

        data.setMine(userInfoVo);

        // 修正平台Id
        for (UserGroup group_row : userGroups) {
            group_row.setId(userMessageService.getPlantformUid(Convert.toStr(group_row.getGroupId())));

            List<UserFriend> group_rel_list = group_row.getList();
            if (CollUtil.isNotEmpty(group_rel_list)) {
                for (UserFriend group_rel : group_rel_list) {
                    group_rel.setId(userMessageService.getPlantformUid(Convert.toStr(group_rel.getFriendId())));
                }
            }
        }

        return data;
    }

    @Override
    public IPage<Map> getUserFriendLists(UserFriendListReq req) {
        IPage<Map> listPage = new Page<>();
        String type = getParameter("type", String.class);
        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();

        Integer user_id = 0;
        user_id = req.getUserId();
        if (!CheckUtil.isNotEmpty(user_id)) {
            ContextUser user = ContextUtil.getLoginUser();
            user_id = user.getUserId();
        }
        if (type.equals("fans")) {
            queryWrapper.eq("friend_id", user_id);
        } else {
            queryWrapper.eq("user_id", user_id);
        }
        IPage<UserFriend> lists = lists(queryWrapper, req.getPage(), req.getSize());
        List<Map> items = Convert.toList(Map.class, lists.getRecords());

        List<Integer> user_ids = new ArrayList<>();
        if (type.equals("fans")) {
            user_ids = CommonUtil.column(lists.getRecords(), UserFriend::getUserId);
        } else {
            user_ids = CommonUtil.column(lists.getRecords(), UserFriend::getFriendId);
        }


        List<UserSns> userSnsRows = userSnsService.gets(user_ids);
        for (Map item : items) {
            Integer userId = 0;
            if (type.equals("fans")) {
                userId = Convert.toInt(item.get("userId"));
            } else {
                userId = Convert.toInt(item.get("friendId"));
            }

            Integer finalUserId = userId;
            Optional<UserSns> userSnsOpl = userSnsRows.stream().filter(s -> ObjectUtil.equal(finalUserId, s.getUserId())).findFirst();
            UserSns userSnsRow = userSnsOpl.orElseGet(UserSns::new);
            if (userSnsRow != null) {
                item.put("user_fans", userSnsRow.getUserFans());
                item.put("user_friend", userSnsRow.getUserFriend());
                item.put("user_story", userSnsRow.getUserStory());

            } else {
                item.put("user_fans", 0);
                item.put("user_friend", 0);
                item.put("user_story", 0);
            }

        }
        userInfoService.fixUserAvatar(items, false);
        listPage.setRecords(items);
        listPage.setPages(lists.getPages());
        listPage.setTotal(lists.getTotal());
        listPage.setSize(lists.getSize());
        listPage.setCurrent(lists.getCurrent());
        return listPage;
    }

    /**
     * 读取分页列表
     *
     * @param user_id
     * @param friend_id
     */
    private UserFriend getInvite(Integer user_id, Integer friend_id) {

        QueryWrapper<UserFriend> queryWrapper = new QueryWrapper<>();
        UserFriend userFriend = null;
        if (CheckUtil.isNotEmpty(friend_id)) {
            queryWrapper.eq("friend_id", user_id)
                    .eq("user_id", friend_id)
                    .eq("friend_invite", 0);
            userFriend = findOne(queryWrapper);
        }

        if (userFriend == null) {
            return null;
        }

        return userFriend;
    }
}
