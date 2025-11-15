package mate.academy.dao.impl;

import java.time.LocalDate;
import java.util.List;
import mate.academy.exception.DataProcessingException;
import mate.academy.model.MovieSession;
import mate.academy.service.MovieSessionService;
import mate.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class MovieSessionDaoImpl implements MovieSessionService {
    @Override
    public MovieSession add(MovieSession movieSession) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(movieSession);
            transaction.commit();
            return movieSession;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add movieSession: " + movieSession + " in DB",
                    e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public MovieSession get(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(MovieSession.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get movieSession from DB", e);
        }
    }

    @Override
    public List<MovieSession> findAvailableSessions(Long movieId, LocalDate date) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<MovieSession> query = session.createQuery("from MovieSession m "
                    + "left join fetch m.movie v "
                    + "where v.id = :movieId and "
                    + "m.showtime > :showtime", MovieSession.class);
            query.setParameter("movieId", movieId);
            query.setParameter("showtime", date);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get Available Sessions for movie with ID: "
                    + movieId + " data is: " + date, e);
        }
    }
}
