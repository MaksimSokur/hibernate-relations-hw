package mate.academy.hibernate.relations.dao.impl;

import java.util.Optional;
import mate.academy.hibernate.relations.DataProcessingException;
import mate.academy.hibernate.relations.dao.ActorDao;
import mate.academy.hibernate.relations.model.Actor;
import mate.academy.hibernate.relations.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ActorDaoImpl extends AbstractDao implements ActorDao {
    public ActorDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Actor add(Actor actor) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            Long id = (Long) session.save(actor);
            actor.setId(id);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add actor " + actor, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return actor;
    }

    @Override
    public Optional<Actor> get(Long id) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Actor.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can't get actor with id " + id, e);
        }
    }
}
