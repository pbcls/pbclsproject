package com.zucc.pbcls.service;

import com.zucc.pbcls.dao.UserInfoDao;
import com.zucc.pbcls.pojo.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Service
public class Admin_UserService {

    @Autowired
    UserInfoDao userInfoDao;

    //分页查询
    public Page<MyUser> findUsers(boolean needuid ,boolean needname,boolean needemail,String findstr,
                                boolean notAccountNonLocked,boolean isAccountNonLocked,
                                String role,Pageable pageable) {
    Page<MyUser> pageList = userInfoDao.findAll(new Specification<MyUser>(){
        @Override
        public Predicate toPredicate(Root<MyUser> root,CriteriaQuery<?>query,CriteriaBuilder cb) {
            //listAccountNonLocked
            List<Predicate> listAndAccountNonLocked = new ArrayList<Predicate>();

            if (notAccountNonLocked&&!isAccountNonLocked){
                listAndAccountNonLocked.add(cb.equal(root.get("accountNonLocked"),true));
            }else if (!notAccountNonLocked&&isAccountNonLocked){
                listAndAccountNonLocked.add(cb.equal(root.get("accountNonLocked"),false));
            }else if (!notAccountNonLocked&&!isAccountNonLocked){
                listAndAccountNonLocked.add(cb.equal(root.get("accountNonLocked"),3));
            }
            Predicate[] arrayAccountNonLocked = new Predicate[listAndAccountNonLocked.size()];
            //这里的cb.and说明该小段用and连接
            Predicate AccountNonLocked = cb.and(listAndAccountNonLocked.toArray(arrayAccountNonLocked));

            //listOrInfo
            List<Predicate> listOrInfo = new ArrayList<Predicate>();
            if (needuid) {
                listOrInfo.add(cb.like(root.get("uid"), "%" + findstr + "%"));
            }
            if (needname) {
                listOrInfo.add(cb.like(root.get("name"), "%" + findstr + "%"));
            }
            if (needemail) {
                listOrInfo.add(cb.like(root.get("email"), "%" + findstr + "%"));
            }
            Predicate[] arrayInfo = new Predicate[listOrInfo.size()];
            //这里的cb.or说明该小段用or连接
            Predicate Info = cb.or(listOrInfo.toArray(arrayInfo));

            //listOrRole
            List<Predicate> listOrRole = new ArrayList<Predicate>();
            if (!"".equals(role)) {
                String arr[] = role.split(",");
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null && !"".equals(arr[i])) {
                        listOrRole.add(cb.equal(root.get("role"), arr[i]));
                    }
                }
                Predicate[] arrayRole = new Predicate[listOrRole.size()];
                //这里的cb.or说明该小段用or连接
                Predicate Role = cb.or(listOrRole.toArray(arrayRole));

                //这里的where会把里面的用字段用and连接  变成的形式为（and）and（or）and（or）
                return query.where(AccountNonLocked,Info,Role).getRestriction();
            } else {
                return null;
            }
        }
    }, pageable);
        return pageList;
}

    public boolean deleteUser(String uid){
        MyUser user = userInfoDao.findByUid(uid);
        if (user != null) {
            userInfoDao.delete(user);
            return true;
        }
        else
            return false;
    }

    public boolean lockUser(String uid){
        MyUser user = userInfoDao.findByUid(uid);
        if (user != null) {
            user.setAccountNonLocked(false);
            userInfoDao.save(user);
            return true;
        }
        else
            return false;
    }

    public boolean unlockUser(String uid){
        MyUser user = userInfoDao.findByUidAndAccountNonLocked(uid,false);
        if (user != null) {
            user.setAccountNonLocked(true);
            userInfoDao.save(user);
            return true;
        }
        else
            return false;
    }

    public boolean changeRole(String uid,String role){
        MyUser user = userInfoDao.findByUid(uid);
        if (user != null) {
            user.setRole(role);
            userInfoDao.save(user);
            return true;
        }
        else
            return false;
    }

    public boolean resetPwd(String uid){
        MyUser user = userInfoDao.findByUid(uid);
        if (user != null){
            user.setPwd(new BCryptPasswordEncoder().encode(user.getUid()));
            userInfoDao.save(user);
            return true;
        }else
            return false;
    }

}
