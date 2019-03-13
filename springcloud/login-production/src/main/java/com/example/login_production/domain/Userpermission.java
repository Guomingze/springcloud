package com.example.login_production.domain;

/**
  *  用户权限
 * @author NHX
 *
 */
public class Userpermission {

    private Integer pid;
    // 用户权限
    private String plimits;
    // 用户当前所属角色
    private Integer proleid;


    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public void setProleid(Integer proleid) {
        this.proleid = proleid;
    }

    public String getPlimits() {
        return plimits;
    }

    public void setPlimits(String plimits) {
        this.plimits = plimits;
    }

}
