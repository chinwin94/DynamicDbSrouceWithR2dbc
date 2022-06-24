package cn.git_chinwin.cc.dao;

import cn.git_chinwin.cc.entity.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;


/**
 * @author chinwin
 * @date 2021/4/22 11:06 下午
 */
@Repository
public interface UserDAO extends R2dbcRepository<User, Long> {


}
