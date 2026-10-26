
package Main;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import College.entity.LabAllocation;
import College.util.HibernateUtil;

public class CollegeLabMain {

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            while (true) {
                System.out.println("\n===== COLLEGE LAB ALLOCATION SYSTEM =====");
                System.out.println("1. Create Lab Allocation");
                System.out.println("2. View Lab Allocations");
                System.out.println("3. Search Allocation by Department");
                System.out.println("4. Cancel Allocation");
                System.out.println("5. Exit");
                System.out.println("6. View Weekly Timetable");

                int choice = readIntInRange("Choose option: ", 1, 6);

                switch (choice) {
                    case 1:
                        createAllocation();
                        break;
                    case 2:
                        viewAllocations();
                        break;
                    case 3:
                        searchAllocations();
                        break;
                    case 4:
                        cancelAllocation();
                        break;
                    case 5:
                        System.out.println("Thank you!");
                        return;
                    case 6:
                        viewWeeklyTimetable();
                        break;
                    default:
                        break;
                }
            }
        } finally {
            sc.close();
            HibernateUtil.shutdown();
        }
    }

    private static void viewWeeklyTimetable() {
		// TODO Auto-generated method stub
		
	}

	// CREATE ALLOCATION
    private static void createAllocation() {

        String department = readDepartment();

        String[] labs;
        String[] subjects;
        String labType;

        switch (department) {
            case "CSE":
                labs = new String[] {
                    "OOPS Lab", "DBMS Lab", "Programming Lab",
                    "Computer Networks Lab", "AI Lab"
                };
                subjects = new String[] {
                    "OOPS", "DBMS", "Java", "Python",
                    "Computer Networks", "Artificial Intelligence"
                };
                labType = "Computer";
                break;

            case "IT":
                labs = new String[] {
                    "DBMS Lab", "Web Technology Lab",
                    "Networking Lab", "Cloud Computing Lab"
                };
                subjects = new String[] {
                    "DBMS", "Web Technology", "Java",
                    "Cloud Computing", "Cyber Security"
                };
                labType = "Computer";
                break;

            case "ECE":
                labs = new String[] {
                    "Electronics Lab", "Microprocessor Lab",
                    "Communication Lab", "VLSI Lab"
                };
                subjects = new String[] {
                    "Digital Electronics", "Microprocessors",
                    "Communication Systems", "VLSI",
                    "Electronic Circuits"
                };
                labType = "Electronics";
                break;

            case "EEE":
                labs = new String[] {
                    "Electrical Machines Lab",
                    "Power Electronics Lab",
                    "Electrical Circuits Lab"
                };
                subjects = new String[] {
                    "Electrical Machines", "Power Systems",
                    "Power Electronics", "Control Systems"
                };
                labType = "Electrical";
                break;

            case "MECH":
                labs = new String[] {
                    "Mechanical Workshop",
                    "CAD/CAM Lab",
                    "Thermal Engineering Lab"
                };
                subjects = new String[] {
                    "Engineering Workshop", "CAD/CAM",
                    "Thermodynamics", "Fluid Mechanics"
                };
                labType = "Mechanical";
                break;

            default: // CIVIL
                labs = new String[] {
                    "Surveying Lab",
                    "Concrete Testing Lab",
                    "Soil Mechanics Lab"
                };
                subjects = new String[] {
                    "Surveying", "Concrete Technology",
                    "Soil Mechanics", "Structural Engineering"
                };
                labType = "Civil";
                break;
        }

        System.out.println("\nAvailable Labs:");
        for (int i = 0; i < labs.length; i++) {
            System.out.println((i + 1) + ". " + labs[i]);
        }

        int labChoice = readIntInRange(
            "Select Lab Number: ", 1, labs.length
        );
        String laboratoryName = labs[labChoice - 1];

        System.out.println("\nAvailable Subjects:");
        for (int i = 0; i < subjects.length; i++) {
            System.out.println((i + 1) + ". " + subjects[i]);
        }

        int subjectChoice = readIntInRange(
            "Select Subject Number: ", 1, subjects.length
        );
        String subject = subjects[subjectChoice - 1];

        String groupName = readNonEmptyString("Student Group Name: ");

        int studyYear = readIntInRange(
            "Student Study Year (1-4): ", 1, 4
        );

        int studentCount = readPositiveInt("Number of Students: ");

        int labCapacity;
        while (true) {
            labCapacity = readPositiveInt("Lab Capacity: ");

            if (labCapacity >= studentCount) {
                break;
            }

            System.out.println(
                "Invalid capacity! Capacity must be at least "
                + studentCount + "."
            );
        }

        String staffName = readNonEmptyString("Staff Name: ");

        String day = readDay();

        LocalTime startTime;
        LocalTime endTime;

        // Keep asking for time until there is no clash.
        while (true) {

            startTime = readTime("Start Time (HH:mm): ");

            while (true) {
                endTime = readTime("End Time (HH:mm): ");

                if (endTime.isAfter(startTime)) {
                    break;
                }

                System.out.println(
                    "Invalid end time! End time must be after start time."
                );
            }

            if (!hasCollision(
                    laboratoryName, day, startTime, endTime)) {
                break;
            }

            System.out.println(
                "COLLISION WARNING! This lab is already allocated "
                + "during that time."
            );
            System.out.println("Please enter another time.");
        }

        LabAllocation allocation = new LabAllocation();

        allocation.setDepartmentName(department);
        allocation.setLaboratoryName(laboratoryName);
        allocation.setLabType(labType);
        allocation.setLabCapacity(labCapacity);
        allocation.setStudentGroupName(groupName);
        allocation.setStudyYear(studyYear);
        allocation.setSubject(subject);
        allocation.setStudentCount(studentCount);
        allocation.setStaffName(staffName);
        allocation.setDay(day);
        allocation.setStartTime(startTime);
        allocation.setEndTime(endTime);
        allocation.setStatus("Allocated");

        Transaction tx = null;

        try (Session session =
                 HibernateUtil.getSessionFactory().openSession()) {

            tx = session.beginTransaction();
            session.persist(allocation);
            tx.commit();

            System.out.println("\nAllocation saved successfully!");
            System.out.println("Allocation ID: " + allocation.getId());
            System.out.println("Department: " + department);
            System.out.println("Laboratory: " + laboratoryName);
            System.out.println("Subject: " + subject);
            System.out.println("Day: " + day);
            System.out.println("Time: " + startTime + " - " + endTime);
            System.out.println("Status: Allocated");

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            System.out.println("Could not save allocation.");
            System.out.println("Reason: " + e.getMessage());
        }
    }

    // CHECK FOR LAB TIME COLLISION
    private static boolean hasCollision(
            String lab, String day,
            LocalTime start, LocalTime end) {

        try (Session session =
                 HibernateUtil.getSessionFactory().openSession()) {

            Long count = session.createQuery(
                "SELECT COUNT(a) FROM LabAllocation a "
                + "WHERE a.laboratoryName = :lab "
                + "AND a.day = :day "
                + "AND (a.status IS NULL OR a.status <> :cancelled) "
                + "AND a.startTime < :endTime "
                + "AND a.endTime > :startTime",
                Long.class
            )
            .setParameter("lab", lab)
            .setParameter("day", day)
            .setParameter("cancelled", "Cancelled")
            .setParameter("startTime", start)
            .setParameter("endTime", end)
            .uniqueResult();

            return count != null && count > 0;

        } catch (Exception e) {
            System.out.println(
                "Could not check lab availability: " + e.getMessage()
            );
            return true;
        }
    }

    // VIEW ALL ALLOCATIONS
    private static void viewAllocations() {

        try (Session session =
                 HibernateUtil.getSessionFactory().openSession()) {

            List<LabAllocation> list = session.createQuery(
                "FROM LabAllocation ORDER BY id",
                LabAllocation.class
            ).list();

            if (list.isEmpty()) {
                System.out.println("No allocations found.");
                return;
            }

            for (LabAllocation a : list) {
                printAllocation(a);
            }

        } catch (Exception e) {
            System.out.println(
                "Could not view allocations: " + e.getMessage()
            );
        }
    }

    // SEARCH BY DEPARTMENT
    private static void searchAllocations() {

        String department = readDepartment();

        try (Session session =
                 HibernateUtil.getSessionFactory().openSession()) {

            List<LabAllocation> list = session.createQuery(
                "FROM LabAllocation "
                + "WHERE departmentName = :dept ORDER BY id",
                LabAllocation.class
            )
            .setParameter("dept", department)
            .list();

            if (list.isEmpty()) {
                System.out.println(
                    "No allocations found for " + department + "."
                );
                return;
            }

            for (LabAllocation a : list) {
                printAllocation(a);
            }

        } catch (Exception e) {
            System.out.println(
                "Search failed: " + e.getMessage()
            );
        }
    }

    // CANCEL ALLOCATION
    private static void cancelAllocation() {

        while (true) {
            int id = readPositiveInt("Enter Allocation ID to cancel: ");

            Transaction tx = null;

            try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

                LabAllocation allocation =
                    session.get(LabAllocation.class, id);

                if (allocation == null) {
                    System.out.println(
                        "Allocation ID not found. Enter a valid ID."
                    );
                    continue;
                }

                if ("Cancelled".equalsIgnoreCase(
                        allocation.getStatus())) {
                    System.out.println(
                        "This allocation is already cancelled. "
                        + "Enter another ID."
                    );
                    continue;
                }

                tx = session.beginTransaction();
                allocation.setStatus("Cancelled");
                tx.commit();

                System.out.println(
                    "Allocation cancelled successfully!"
                );
                return;

            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                System.out.println(
                    "Could not cancel allocation: " + e.getMessage()
                );
                return;
            }
        }
    }

    // PRINT ONE ALLOCATION
    private static void printAllocation(LabAllocation a) {

        System.out.println("\n------------------------------");
        System.out.println("Allocation ID : " + a.getId());
        System.out.println("Department    : " + a.getDepartmentName());
        System.out.println("Laboratory    : " + a.getLaboratoryName());
        System.out.println("Lab Type      : " + a.getLabType());
        System.out.println("Subject       : " + a.getSubject());
        System.out.println("Student Group : " + a.getStudentGroupName());
        System.out.println("Study Year    : " + a.getStudyYear());
        System.out.println("Students      : " + a.getStudentCount());
        System.out.println("Lab Capacity  : " + a.getLabCapacity());
        System.out.println("Staff         : " + a.getStaffName());
        System.out.println("Day           : " + a.getDay());
        System.out.println("Start Time    : " + a.getStartTime());
        System.out.println("End Time      : " + a.getEndTime());
        System.out.println("Status        : " + a.getStatus());
    }

    // DEPARTMENT VALIDATION
    private static String readDepartment() {

        while (true) {
            System.out.print(
                "Department (CSE/IT/ECE/EEE/MECH/CIVIL): "
            );

            String department = sc.nextLine()
                                  .trim()
                                  .toUpperCase(Locale.ROOT);

            switch (department) {
                case "CSE":
                case "IT":
                case "ECE":
                case "EEE":
                case "MECH":
                case "CIVIL":
                    return department;

                default:
                    System.out.println(
                        "Invalid department! Try again."
                    );
            }
        }
    }

    // DAY VALIDATION
    private static String readDay() {

        while (true) {
            System.out.print(
                "Day (MONDAY-FRIDAY): "
            );

            String day = sc.nextLine()
                           .trim()
                           .toUpperCase(Locale.ROOT);

            switch (day) {
                case "MONDAY":
                case "TUESDAY":
                case "WEDNESDAY":
                case "THURSDAY":
                case "FRIDAY":
                    return day;

                default:
                    System.out.println(
                        "Invalid day! Enter MONDAY to FRIDAY."
                    );
            }
        }
    }

    // TIME VALIDATION
    private static LocalTime readTime(String message) {

        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return LocalTime.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println(
                    "Invalid time! Use HH:mm format, e.g. 09:30."
                );
            }
        }
    }

    // NON-EMPTY TEXT VALIDATION
    private static String readNonEmptyString(String message) {

        while (true) {
            System.out.print(message);
            String value = sc.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println(
                "This field cannot be empty. Try again."
            );
        }
    }

    // INTEGER VALIDATION
    private static int readInt(String message) {

        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid number! Enter a whole number."
                );
            }
        }
    }

    // POSITIVE INTEGER VALIDATION
    private static int readPositiveInt(String message) {

        while (true) {
            int number = readInt(message);

            if (number > 0) {
                return number;
            }

            System.out.println(
                "Enter a number greater than zero."
            );
        }
    }

    // RANGE VALIDATION
    private static int readIntInRange(
            String message, int min, int max) {

        while (true) {
            int number = readInt(message);

            if (number >= min && number <= max) {
                return number;
            }

            System.out.println(
                "Invalid choice! Enter a number from "
                + min + " to " + max + "."
            );
        }
    }

    private static void viewWeeklyTimetable1() {

        String[] days = {
            "MONDAY", "TUESDAY", "WEDNESDAY",
            "THURSDAY", "FRIDAY"
        };

        try (Session session =
                 HibernateUtil.getSessionFactory().openSession()) {

            List<LabAllocation> allocations = session.createQuery(
                "FROM LabAllocation ORDER BY startTime",
                LabAllocation.class
            ).list();

            System.out.println("\n========== WEEKLY TIMETABLE ==========");

            for (String day : days) {

                System.out.println("\n--- " + day + " ---");

                boolean found = false;

                for (LabAllocation a : allocations) {

                    if (day.equalsIgnoreCase(a.getDay())
                            && !"Cancelled".equalsIgnoreCase(a.getStatus())) {

                        found = true;

                        System.out.printf(
                            "%-12s | %-15s | %-8s | %-20s | %s%n",
                            a.getStartTime() + "-" + a.getEndTime(),
                            a.getLaboratoryName(),
                            a.getDepartmentName(),
                            a.getSubject(),
                            a.getStaffName()
                        );
                    }
                }

                if (!found) {
                    System.out.println("No allocations.");
                }
            }

        } catch (Exception e) {
            System.out.println(
                "Error loading timetable: " + e.getMessage()
            );
        }
    }
    }