package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "ROLES_LIST")
public class RolesList {
    @Id
    @SequenceGenerator(name = "rolesListGenerator", sequenceName = "roleslist_sequence",
            allocationSize = 1, initialValue = 4)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolesListGenerator")
    private long id;
    @ManyToOne
    private  User user;
    @ManyToOne
    private Role role;

    public RolesList() {
    }

    @JsonGetter("user_id")
    public Long getUserId() {
        if (user == null) {
            return null;
        }
        return user.getId();
    }

    @JsonGetter("role_id")
    public Long getRoleId() {
        if (role == null) {
            return null;
        }
        return role.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RolesList rolesList = (RolesList) o;

        if (user != null ? !user.equals(rolesList.user) : rolesList.user != null) return false;
        return role != null ? role.equals(rolesList.role) : rolesList.role == null;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static class RolesListDetails {
        private Long userId;
        private Long roleId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getRoleId() {
            return roleId;
        }

        public void setRoleId(Long roleId) {
            this.roleId = roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            RolesListDetails that = (RolesListDetails) o;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
            return roleId != null ? roleId.equals(that.roleId) : that.roleId == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
            return result;
        }
    }
}
