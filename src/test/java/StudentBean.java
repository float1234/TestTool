/**
 * Created by dextry on 2017/5/26.
 */
public class StudentBean {
    private int id = 1001;
    private String name = "zhangsan";
    private  int age = 20;
    private boolean gender = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
