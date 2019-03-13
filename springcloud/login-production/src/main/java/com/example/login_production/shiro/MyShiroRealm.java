package com.example.login_production.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.login_production.dao.PersionDao;
import com.example.login_production.dao.UserpermissionDao;
import com.example.login_production.dao.UsersroleDao;
import com.example.login_production.domain.Persion;
import com.example.login_production.domain.Userpermission;
import com.example.login_production.domain.Usersrole;

public class MyShiroRealm extends AuthorizingRealm {

	
	@Autowired
	private PersionDao persionDao;
	@Autowired
	private UserpermissionDao userpermissionDao;
	@Autowired
	private UsersroleDao usersroleDao;


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        
    	if (authenticationToken == null) {
			return null;
		}
    	
    	//获取用户信息
        String name = authenticationToken.getPrincipal().toString();

        Persion persion = new Persion();
        persion.setUsername(name);
        Persion users = persionDao.selectOne(persion);
        if (users == null) {
            return null;
        }
        if (!name.equals(users.getUsername())) {
        	//这里验证用户名
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息，这里验证密码
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, users.getPwd(), getName());
            return simpleAuthenticationInfo;
        }
    }


    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        //获取登录用户名
        String name = (String) principals.getPrimaryPrincipal();

        Persion persion = new Persion();
        persion.setUsername(name);
        Persion users = persionDao.selectOne(persion);
        if (users == null) {
            return null;
        }
        //获取用户角色
        Usersrole usersrole = new Usersrole();
        usersrole.setRuid(users.getId());
        usersrole = usersroleDao.selectOne(usersrole);
        if (usersrole == null) {
            return null;
        }

        //获取用户权限
        Userpermission userpermission = new Userpermission();
        userpermission.setProleid(usersrole.getRid());
        Userpermission p = userpermissionDao.selectOne(userpermission);
        if (p == null) {
            return null;
        }

        //添加角色,后边用来限制访问
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole(usersrole.getRole());
        //多个角色
        //simpleAuthorizationInfo.addRoles();

        //添加权限，后边用来限制访问
        simpleAuthorizationInfo.addStringPermission(p.getPlimits());
        return simpleAuthorizationInfo;
    }
}
