package ru.tsconsulting.details;

public class DepartmentDetails {

    private String name;
    private String chiefId;
    private Long parent;

    public DepartmentDetails() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChiefId() {
        return chiefId;
    }

    public void setChiefId(String chiefId) {
        this.chiefId = chiefId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentDetails that = (DepartmentDetails) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (chiefId != null ? !chiefId.equals(that.chiefId) : that.chiefId != null) return false;
        return parent != null ? parent.equals(that.parent) : that.parent == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (chiefId != null ? chiefId.hashCode() : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }
}
