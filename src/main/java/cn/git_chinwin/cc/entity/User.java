package cn.git_chinwin.cc.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 * @author chinwin
 * @date 2022/6/24 11:13 PM
 */
@Table("t_user")
public class User {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
