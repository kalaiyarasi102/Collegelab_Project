
package College.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "laboratory")
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String labName;
    private String labType;
    private int capacity;
    private boolean available = true;

    public Laboratory() {
    }

    public Long getId() {
        return id;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabType() {
        return labType;
    }

    public void setLabType(String labType) {
        this.labType = labType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

	public void setName(String labName2) {
		
	}

	public void setDepartment(Department department) {
		
	}

	public int getName() {
		return 0;
	}
}
