package College.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;
    private int year;
    private String section;
    private int studentCount;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public StudentGroup() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public int getStudentCount() {
		return studentCount;
	}

	public void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setName(String groupName2) {
		
	}

	public void setStudyYear(int studyYear) {
		
	}

	public int getName() {
		return 0;
	}

	public int getStudyYear() {
		return 0;
	}
}