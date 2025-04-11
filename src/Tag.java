package src;
public class Tag {
    public String name;
    public String value;
    public Tag (String name, String value) {
        this.name = name;
        this.value = value;
    }
    public getName() {
        return this.name;
    }
    public getValue() {
        return this.value;
    }
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tags other = (Tags) obj;
        return this.name.equals(other.name) && this.value.equals(other.value);
    }
}