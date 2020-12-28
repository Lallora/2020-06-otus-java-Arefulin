package hw17.dto;

import hw17.core.model.User;

public class UserDto {

    private final Long id;
    private final String name;

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserDto{" + "id = " + getId() + ", name = " + getName() + "}";
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserDto object = (UserDto) o;

        if (id != null ? !id.equals(object.id) : object.id != null)
            return false;
        return !(name != null ? !name.equals(object.name) : object.name != null);
    }
}
