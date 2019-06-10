package com.raven.appserver.user.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;
import lombok.Data;

@Data
@Table(name="t_user")
public class UserBean implements Serializable {

    private static final long serialVersionUID = 9129370215157758832L;

    @Id
    private Long id;

    @Column
    private String uid;

	@Column
    private String username;

	@Column
    private String password;

    @Column
	private Date create_dt;

    @Column
    private Date update_dt;

    @Column
    private String pwdsalt;
    
    @Column
    private String name;

    @Column
    private String portrait;

    @Column
    private Integer state;

    @Column
    private Integer type;

}

