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
@Table(name = "t_permission")
@Data
public class PermissionBean {

    @Id
    private Long id;
    @Column
    private String perm_name;
    @Column
    private String descinfo;
    @Column
    private Integer is_deleted;
    @Column
    private Date create_dt;
    @Column
    private Date update_dt;

}
