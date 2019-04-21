package com.tim.appserver.shiro.bean;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * @Author zxx
 * @Description
 * @Date Created on 2017/11/10
 */
@Table(name = "t_user_role")
@Data
public class UserRoleBean {

    @Id
    private Long id;
    @Column
    private Long userid;
    @Column
    private Long roleid;
    @Column
    private Integer is_deleted;
    @Column
    private Date create_dt;
    @Column
    private Date update_dt;
}
