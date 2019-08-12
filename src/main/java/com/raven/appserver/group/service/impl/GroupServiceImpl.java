package com.raven.appserver.group.service.impl;


import com.raven.appserver.common.RestResult;
import com.raven.appserver.group.bean.model.GroupMemberModel;
import com.raven.appserver.group.bean.model.GroupModel;
import com.raven.appserver.group.bean.param.GroupDetailParam;
import com.raven.appserver.group.bean.param.GroupOutParam;
import com.raven.appserver.group.bean.param.GroupReqParam;
import com.raven.appserver.group.mapper.GroupMapper;
import com.raven.appserver.group.mapper.GroupMemberMapper;
import com.raven.appserver.group.service.GroupService;
import com.raven.appserver.group.validator.GroupValidator;
import com.raven.appserver.group.validator.MemberInValidator;
import com.raven.appserver.group.validator.MemberNotInValidator;
import com.raven.appserver.utils.DateTimeUtils;
import com.raven.appserver.utils.RestApi;
import com.raven.appserver.utils.RestResultCode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private RestApi restApi;

    @Override
    public GroupOutParam createGroup(GroupReqParam reqParam) {

        //TODO  verfiy all the members validation.

        // create group in IM server.
        GroupOutParam result = restApi.createGroup(reqParam);

        //update local group db.
        GroupModel model = new GroupModel();
        model.setUid(result.getGroupId());
        model.setName(reqParam.getName());
        model.setPortrait(reqParam.getPortrait());
        model.setOwner(reqParam.getMembers().get(0));
        model.setCreateDate(result.getTime());
        model.setUpdateDate(result.getTime());
        model.setStatus(0); //0 for normal
        model.setConverId(result.getConverId());
        groupMapper.insert(model);

        //update local group member db.
        reqParam.getMembers().forEach(uid -> {
            GroupMemberModel member = new GroupMemberModel();
            member.setGroupId(result.getGroupId());
            member.setCreateDate(result.getTime());
            member.setUpdateDate(result.getTime());
            member.setMemberUid(uid);
            member.setStatus(0);// 0 normal status;
            memberMapper.insert(member);
        });
        return result;
    }

    @Override
    public RestResultCode joinGroup(GroupReqParam reqParam) {
        //params check.
        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return groupValidator.errorCode();
        }
        if (!memberInValidator.isValid(reqParam.getGroupId(), reqParam.getMembers())) {
            return memberInValidator.errorCode();
        }

        // call IM server.
        RestResultCode result = restApi.joinGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS == result) {
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
        }

        return result;
    }

    @Override
    public RestResultCode quitGroup(GroupReqParam reqParam) {
        //params check.
        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return groupValidator.errorCode();
        }
        if (!memberNotValidator.isValid(reqParam.getGroupId(), reqParam.getMembers())) {
            return memberNotValidator.errorCode();
        }

        // call IM server.
        RestResultCode result = restApi.quitGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS == result) {
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
        }
        return result;
    }

    @Override
    public RestResultCode dismissGroup(GroupReqParam reqParam) {
        //params check.
        if (!groupValidator.isValid(reqParam.getGroupId())) {
            return groupValidator.errorCode();
        }
        // call IM server.
        RestResultCode result = restApi.dismissGroup(reqParam);

        if (RestResultCode.COMMON_SUCCESS == result) {
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
            .andEqualTo("status", 0)
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
        return RestResult.success(new GroupDetailParam(info.get(0).getName(),
            info.get(0).getPortrait(),
            members,
            info.get(0).getUpdateDate()));
    }
}
