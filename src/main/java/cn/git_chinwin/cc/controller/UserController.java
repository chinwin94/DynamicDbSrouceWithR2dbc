package cn.git_chinwin.cc.controller;

import cn.git_chinwin.cc.dao.UserDAO;
import cn.git_chinwin.cc.entity.User;
import cn.git_chinwin.cc.plugins.R2DBCSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author chinwin
 * @date 2022/6/24 11:14 PM
 */
@RestController
public class UserController {


    private final UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/user")
    @R2DBCSource("master")
    public Flux<User> getUsers() {
        return userDAO.findAll();
    }

}
