package College.service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import College.entity.LabAllocation;
import College.util.HibernateUtil;
public class LabAllocationService {

    private SessionFactory factory =
            HibernateUtil.getSessionFactory();

    public String allocateLab(LabAllocation allocation) {

        Transaction transaction = null;

        try (Session session = factory.openSession()) {

            transaction = session.beginTransaction();

            session.persist(allocation);

            transaction.commit();

            return "Lab allocated successfully!";

        } catch (Exception e) {

            if (transaction != null
                    && transaction.isActive()) {
                transaction.rollback();
            }

            e.printStackTrace();

            return "Lab allocation failed!";
        }
    }
}
