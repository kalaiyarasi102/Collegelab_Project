
package College.dao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import College.entity.LabAllocation;
import College.util.HibernateUtil;

public class LabAllocationDAO {

    private final SessionFactory factory =
            HibernateUtil.getSessionFactory();

    public void save(LabAllocation allocation) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(allocation);

            transaction.commit();

            System.out.println("Lab allocation saved successfully!");

        } catch (Exception e) {

            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error saving lab allocation:");
            e.printStackTrace();
        }
    }

  
    public LabAllocation findById(Long id) {

        try (Session session = factory.openSession()) {

            return session.get(LabAllocation.class, id);

        } catch (Exception e) {

            System.err.println("Error finding allocation:");
            e.printStackTrace();

            return null;
        }
    }

 
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            LabAllocation allocation =
                    session.get(LabAllocation.class, id);

            if (allocation != null) {
                session.remove(allocation);
                System.out.println("Allocation deleted successfully!");
            } else {
                System.out.println("Allocation not found!");
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            System.err.println("Error deleting allocation:");
            e.printStackTrace();
        }
    }
}
