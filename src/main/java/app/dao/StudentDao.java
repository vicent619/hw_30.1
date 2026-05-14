package app.dao;

import app.entity.Student;

import jakarta.persistence.*;

import java.util.List;

public class StudentDao implements GenericDao<Student, Long> {
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory("hillel-persistence-unit");

    @Override
    public void save(Student entity) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Student findById(Long id) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return em.find(Student.class, id);
        }
    }

    @Override
    public Student findByEmail(String email) {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.email = :email", Student.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Student> findAll() {
        try (EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager()) {
            return em.createQuery("SELECT s FROM Student s", Student.class).getResultList();
        }
    }

    @Override
    public Student update(Student entity) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            em.getTransaction().begin();
            Student merged = em.merge(entity);
            em.getTransaction().commit();
            return merged;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        EntityManager em = ENTITY_MANAGER_FACTORY.createEntityManager();
        try {
            Student student = em.find(Student.class, id);
            if (student == null) return false;
            em.getTransaction().begin();
            em.remove(student);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
