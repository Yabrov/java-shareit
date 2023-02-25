package ru.practicum.shareit.config;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public abstract class BaseEntity<T> implements Serializable {

    private final static Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BaseEntity)) {
            return false;
        }
        BaseEntity entity = (BaseEntity) obj;
        if (!entity.getClass().equals(this.getClass())) {
            return false;
        }
        if (id == null || !id.equals(entity.id)) return false;

        return true;
    }

    @Override
    public String toString() {
        return "BaseEntity{id=" + id + "}";
    }
}
