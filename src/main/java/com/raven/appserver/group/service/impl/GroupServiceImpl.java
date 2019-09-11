package com.raven.appserver.group.service.impl;


import static com.raven.appserver.utils.Constants.*;
import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.model.GroupMemberModel;
import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.bean.param.GroupDetailParam;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.group.mapper.GroupMapper;
import com.raven.appserver.group.mapper.GroupMemberMapper;
import com.raven.appserver.group.service.GroupService;
import com.raven.appserver.group.validator.GroupOwnerValidator;
import com.raven.appserver.group.validator.GroupValidator;
import com.raven.appserver.group.validator.MemberInValidator;
import com.raven.appserver.group.validator.MemberNotInValidator;
import com.raven.appserver.group.validator.UserValidator;
import com.raven.appserver.pojos.ReqMsgParam;
import com.raven.appserver.user.bean.UserBean;
import com.raven.appserver.user.service.UserService;
import com.raven.appserver.utils.DateTimeUtils;
import com.raven.appserver.restapi.RestApi;
import com.raven.appserver.utils.RestResultCode;
import com.raven.appserver.utils.ShiroUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(rollbackFor = Throwable.class)
@Slf4j
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper memberMapper;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private MemberNotInValidator memberNotValidator;

    @Autowired
    private MemberInValidator memberInValidator;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private GroupOwnerValidator groupOwnerValidator;

    @Autowired
    private RestApi restApi;

    @Autowired
    private UserService userService;

    @Value("${app.superadmin}")
    private String superAdminUid;

    @Override
    public RestResult createGroup(GroupReqParam reqParam) {

        //params check.
        if (reqParam.getMembers() == null || reqParam.getMembers().size() == 0) {
            return RestResult.failure(RestResultCode.COMMON_INVALID_PARAMETER.getCode());
        }

        for(String uid : reqParam.getMembers()) {
            if (!userValidator.isValid(uid)) {
                return RestResult.failure(userValidator.errorCode().getCode());
            }
        }

        // create group in IM server.
        RestResult result = restApi.createGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS.getCode() == result.getRspCode()) {
            Map<String, String> map = (Map) result.getData();
            GroupOutParam param = new GroupOutParam(map.get("groupId"), map.get("converId"),
                DateTimeUtils.getDate(map.get("time")));

            //update local group db.
            GroupModel model = new GroupModel();
            model.setUid(param.getGroupId());
            model.setName(reqParam.getName());
            model.setPortrait(reqParam.getPortrait());
            model.setOwner(reqParam.getMembers().get(0));
            model.setCreateDate(param.getTime());
            model.setUpdateDate(param.getTime());
            model.setStatus(0); //0 for normal
            model.setConverId(param.getConverId());
            groupMapper.insert(model);

            //update local group member db.
            reqParam.getMembers().forEach(uid -> {
                GroupMemberModel member = new GroupMemberModel();
                member.setGroupId(param.getGroupId());
                member.setCreateDate(param.getTime());
                member.setUpdateDate(param.getTime());
                member.setMemberUid(uid);
                member.setStatus(0);// 0 normal status;
                memberMapper.insert(member);
            });

            // send notify
            sendNotify(GroupOperationType.CREATE, null, param.getConverId());
            return RestResult.success(param);
        }
        return result;
    }

    @Override
    public RestResult joinGroup(GroupReqParam reqParam) {
        //params check.
        if (reqParam.getMembers() == null || reqParam.getMembers().size() == 0) {
            return RestResult.generate(RestResultCode.COMMON_INVALID_PARAMETER);
        }

        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return RestResult.generate(groupValidator.errorCode());
        }
        if (!memberInValidator.isValid(reqParam.getGroupId(), reqParam.getMembers())) {
            return RestResult.generate(memberInValidator.errorCode());
        }

        // call IM server.
        RestResult result = restApi.joinGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS.getCode() == result.getRspCode()) {
            Date now = DateTimeUtils.currentUTC();
            reqParam.getMembers().forEach(uid -> {
                Example example = new Example(GroupMemberModel.class);
                example.createCriteria()
                    .andEqualTo("groupId", reqParam.getGroupId())
                    .andEqualTo("memberUid", uid);
                List<GroupMemberModel> list = memberMapper.selectByExample(example);
                if (list != null && list.size() > 0) {
                    //exists already.
                    GroupMemberModel member = new GroupMemberModel();
                    member.setUpdateDate(DateTimeUtils.currentUTC());
                    member.setStatus(0);// 0 for normal state.
                    memberMapper.updateByExampleSelective(member, example);
                } else {
                    GroupMemberModel member = new GroupMemberModel();
                    member.setGroupId(reqParam.getGroupId());
                    member.setCreateDate(now);
                    member.setUpdateDate(now);
                    member.setMemberUid(uid);
                    member.setStatus(0);// 0 normal status;
                    memberMapper.insert(member);
                }
            });

            // send notify
            String convId = getConvIdByGroupId(reqParam.getGroupId());
            sendNotify(GroupOperationType.JOIN, reqParam.getMembers(), convId);
        }

        return result;
    }

    @Override
    public RestResult quitGroup(GroupReqParam reqParam) {
        //params check.
        if (reqParam.getMembers() == null || reqParam.getMembers().size() == 0) {
            return RestResult.generate(RestResultCode.COMMON_INVALID_PARAMETER);
        }

        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return RestResult.generate(groupValidator.errorCode());
        }
        if (!memberNotValidator.isValid(reqParam.getGroupId(), reqParam.getMembers())) {
            return RestResult.generate(memberNotValidator.errorCode());
        }

        // call IM server.
        RestResult result = restApi.quitGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS.getCode() == result.getRspCode()) {
            reqParam.getMembers().forEach(uid -> {
                GroupMemberModel member = new GroupMemberModel();
                member.setUpdateDate(DateTimeUtils.currentUTC());
                member.setStatus(2);// 2 mark delete.

                Example example = new Example(GroupMemberModel.class);
                example.createCriteria()
                    .andEqualTo("groupId", reqParam.getGroupId())
                    .andEqualTo("memberUid", uid);
                memberMapper.updateByExampleSelective(member, example);
            });
            // send notify
            String convId = getConvIdByGroupId(reqParam.getGroupId());
            sendNotify(GroupOperationType.QUIT, reqParam.getMembers(), convId);
        }
        return result;
    }

    @Override
    public RestResult dismissGroup(GroupReqParam reqParam) {
        //params check.
        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return RestResult.generate(groupValidator.errorCode());
        }

        // if not owner dismiss, deny.
        String uid = (String) ShiroUtils.getAttribute(ShiroUtils.USER_UID);
        if (!groupOwnerValidator.isValid(uid, reqParam.getGroupId())) {
            return RestResult.generate(groupOwnerValidator.errorCode());
        }

        // call IM server.
        RestResult result = restApi.dismissGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS.getCode() == result.getRspCode()) {
            // conversation delete.
            Example example1 = new Example(GroupMemberModel.class);
            example1.createCriteria()
                .andEqualTo("groupId", reqParam.getGroupId());

            // clean group info
            GroupModel model = new GroupModel();
            model.setStatus(2); //2 for mark delete
            model.setUpdateDate(DateTimeUtils.currentUTC());
            Example example = new Example(GroupModel.class);
            example.createCriteria()
                .andEqualTo("uid", reqParam.getGroupId());
            groupMapper.updateByExampleSelective(model, example);

            //clean group member info
            GroupMemberModel member = new GroupMemberModel();
            member.setUpdateDate(DateTimeUtils.currentUTC());
            member.setStatus(2);// 2 mark delete.
            memberMapper.updateByExampleSelective(member, example1);

            // send notify
            String convId = getConvIdByGroupId(reqParam.getGroupId());
            sendNotify(GroupOperationType.DISMISS, null, convId);
        }
        return result;
    }

    @Override
    public RestResult groupDetail(String groupId) {
        //params check.
        if (!groupValidator.isValid(groupId)) {
            return RestResult.failure(groupValidator.errorCode().getCode());
        }

        Example example = new Example(GroupModel.class);
        example.createCriteria()
//            .andEqualTo("status", 0)
            .andEqualTo("uid", groupId);
        List<GroupModel> info = groupMapper.selectByExample(example);
        Example example1 = new Example(GroupMemberModel.class);
        example1.createCriteria()
            .andEqualTo("status", 0)
            .andEqualTo("groupId", groupId);
        List<GroupMemberModel> memberModels = memberMapper.selectByExample(example1);
        List<String> members = memberModels.stream()
            .map(x -> x.getMemberUid())
            .collect(Collectors.toList());
        return RestResult.success(new GroupDetailParam(
            groupId,
            info.get(0).getName(),
            info.get(0).getPortrait(),
            info.get(0).getConverId(),
            info.get(0).getOwner(),
            info.get(0).getStatus(),
            members,
            info.get(0).getUpdateDate()));
    }

    private void sendNotify(GroupOperationType type, List<String> members, String covId) {
        String uid = (String) ShiroUtils.getAttribute(ShiroUtils.USER_UID);
        /*
         if uid is null , means no login user.   So the operation is triggered by Server API.
         So we use Superadmin identity to send the notification.
         */
        if (StringUtils.isEmpty(uid)) {
            uid = superAdminUid;
        }
        UserBean bean = userService.getUser(uid);
        String content = "";

        String memberStr = "";
        if (members != null && !members.isEmpty()) {
            List<String> memberNames = new ArrayList<>();
            members.forEach(member -> memberNames.add(userService.getUser(member).getName()));
            for (String name : memberNames) {
                memberStr += name + " ";
            }
        }

        switch (type.getType()) {
            case 0:
                content = String.format(CREATE_GROUP_FORMAT, bean.getName());
                break;
            case 1:
                content = String.format(JOIN_GROUP_FORMAT, bean.getName(), memberStr);
                break;
            case 2:
                content = String.format(KICK_GROUP_FORMAT, bean.getName(), memberStr);
                break;
            case 3:
                content = String.format(DISMISS_GROUP_FORMAT, bean.getName());
                break;
        }
        ReqMsgParam notifyParam = new ReqMsgParam(uid, covId, content);
        RestResult result = restApi.sendNotify2Conversation(notifyParam);
        log.info("Notify result: {}", result);
    }

    private String getConvIdByGroupId(String groupId) {
        Example example = new Example(GroupModel.class);
        example.createCriteria()
            .andEqualTo("groupId", groupId);
        List<GroupModel> list = groupMapper.selectByExample(example);
        return list == null || list.size() <= 0 ? "" : list.get(0).getConverId();
    }
}
