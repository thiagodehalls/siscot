package br.com.sicot.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by thiago on 21/12/15.
 */
@Entity
@XmlRootElement
@Table(name = "roles")
public class Roles implements Serializable {

    public static final String ADMINISTRADOR = "Administrador";
    public static final String CLIENTE = "Client";
    public static final String EMPRESA = "Empresa";

    private String user;
    private String role;
    private String group;

    @Id
    @Column(name = "user_id")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Column(name = "user_role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "role_group")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Roles roles = (Roles) o;

        if (group != null ? !group.equals(roles.group) : roles.group != null) return false;
        if (role != null ? !role.equals(roles.role) : roles.role != null) return false;
        if (user != null ? !user.equals(roles.user) : roles.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
