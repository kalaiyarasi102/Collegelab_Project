
package College.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departmentName;

    public Department() {
    }

    public Long getId() {
        return id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

	public void setName(String departmentName2) {
		
	}

	public String getName() {
		return null;
	}

	public String getStudyYear1() {
		return null;
	}

	public String getStudyYear() {
		return null;
	}
}