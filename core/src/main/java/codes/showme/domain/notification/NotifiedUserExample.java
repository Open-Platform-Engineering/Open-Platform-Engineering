package codes.showme.domain.notification;

public class NotifiedUserExample implements NotifiedUser{
    private String displayName;

    private long id;

    public NotifiedUserExample(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "NotifiedUserExample{" +
                "displayName='" + displayName + '\'' +
                '}';
    }
}
