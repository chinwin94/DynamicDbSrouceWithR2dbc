package cn.git_chinwin.cc.config;

/**
 * @author chinwin
 * @date 2021/12/28 7:01 下午
 */
public class DbProp {
    private String driver = "mysql";
    private String host = "127.0.0.1";
    private Integer port = 3306;
    // 数据库名
    private String database;
    private String user;
    private String pass;
    private DbPool pool;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public DbPool getPool() {
        return pool;
    }

    public void setPool(DbPool pool) {
        this.pool = pool;
    }
}
