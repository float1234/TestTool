package jupiter.simple_test;

/**
 * Created by dextry on 2017/5/17.
 */
public class dayInfoBean {
    private String ref_date;
    private  int user_source;
    private int new_user;
    private  int cancel_user;
    private  int cumulate_user;

    public String getRef_date() {
        return ref_date;
    }

    public void setRef_date(String ref_date) {
        this.ref_date = ref_date;
    }

    public int getUser_source() {
        return user_source;
    }

    public void setUser_source(int user_source) {
        this.user_source = user_source;
    }

    public int getNew_user() {
        return new_user;
    }

    public void setNew_user(int new_user) {
        this.new_user = new_user;
    }

    public int getCancel_user() {
        return cancel_user;
    }

    public void setCancel_user(int cancel_user) {
        this.cancel_user = cancel_user;
    }

    public int getCumulate_user() {
        return cumulate_user;
    }

    public void setCumulate_user(int cumulate_user) {
        this.cumulate_user = cumulate_user;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
