package cn.edu.usst.cs.campusAid.dto.forum;

public enum Visibility {
    ADMIN("admin"),//被管理员隐藏
    SENDER("sender"),//被发帖人隐藏
    VISIBLE("visible");//公共可见

    private final String value;

    Visibility(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

