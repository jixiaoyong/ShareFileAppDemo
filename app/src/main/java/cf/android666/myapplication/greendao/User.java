package cf.android666.myapplication.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * GreenDao的实体类
 * Created by jixiaoyong on 2018/8/28.
 * email:jixiaoyong1995@gmail.com
 */
@Entity
public class User {

    @Id(autoincrement=true)
    private long id;

    private String name;

    @Property(nameInDb = "MSG")
    private String msg;

    @Transient
    private String tempString;

    @Generated(hash = 131342188)
    public User(long id, String name, String msg) {
        this.id = id;
        this.name = name;
        this.msg = msg;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
