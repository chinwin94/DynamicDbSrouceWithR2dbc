package cn.git_chinwin.cc.config;

import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

/**
 * @author chinwin
 * @date 2021/12/28 7:01 下午
 */
public class DbGroup {
    public static final String defaultGroup = "default";
    // 数据源名称
    private String name;
    private List<DbProp> list;

    @ConstructorBinding
    public DbGroup(String name, List<DbProp> list) {
        if (null == name || "".equals(name)) {
            throw new RuntimeException("Properties[DbGroup.name]: - name can not be empty!");
        }
        if (null == list || list.size() < 1) {
            throw new IllegalArgumentException("Properties[DbGroup.list]: can not be empty!");
        }
        this.name = name;
        this.list = list;
    }


    public String getName() {
        return null == name ? defaultGroup : name;
    }

    public List<DbProp> getList() {
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<DbProp> list) {
        this.list = list;
    }
}
