package com.enrico200165.weblistscraper.concorsi_it.entities;

import com.enrico200165.weblistscraper.entities.EntityBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class ConcorsiDB {

    public ConcorsiDB() {
        log.error("da implementare a causa di un bug di eclipselink");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("concorsi");
        em = factory.createEntityManager();
    }

    public EntityManager it() {
        return em;
    }

    public Long getNrRows() {
        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        cq.select(qb.count(cq.from(Concor.class)));
        //cq.where(/*your stuff*/);
        long nrRows = em.createQuery(cq).getSingleResult();
        return nrRows;
    }


    public List<Concor> findConcorNotReported() {

        log.error("da implementare a causa di un bug di eclipselink");
        if (true) {
            return new ArrayList<Concor>();
        }
        log.warn("codice temporaneo da correggere");
        // per ora pesco tutto
        log.error("codice sotto va riscritto, forse JPA1 da riscrivere in JPA2");
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Concor> query = criteriaBuilder.createQuery(Concor.class);
        Root<Concor> root = query.from(Concor.class);

        Predicate nullReportDate = criteriaBuilder.isNull(root.get("dateReported"));
        Predicate todayReportDate = criteriaBuilder.equal(root.get("dateReported"), new Date());
        Predicate finalExpr = criteriaBuilder.or(nullReportDate, todayReportDate);
        query.where(finalExpr);

        CriteriaQuery<Concor> all = query.select(root);
        TypedQuery<Concor> allQuery = em.createQuery(all);

        List<Concor> concors = allQuery.getResultList();
        log.info("query, n concorsi: " + concors.size());
        return concors;
    }

    public Concor findConcorByInternalID(String intID, String fieldName) {
        return findConcorByInternalID(intID, fieldName, true);
    }

    /**
     * @param intID
     * @param fieldName : name of DB colum used as internal ID
     * @return
     */
    public Concor findConcorByInternalID(String intID, String fieldName, boolean detach) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Concor> query = criteriaBuilder.createQuery(Concor.class);
        Root<Concor> from = query.from(Concor.class);
        query.select(from);

        Expression<String> column = from.get(fieldName);
        ParameterExpression<String> userPar = criteriaBuilder.parameter(String.class);
        query.where(criteriaBuilder.equal(column, userPar));
        TypedQuery<Concor> q = em.createQuery(query);
        q.setParameter(userPar, intID);
        List<Concor> contacts = q.getResultList();

        if (contacts.size() == 0) return null;
        if (contacts.size() == 1) {
            Concor p = contacts.get(0);
            p.setFrom(EntityBase.From.CONTACT_HISTORY);
            if (detach) em.detach(p);
            return p;
        }
        log.error("Exiting abnormal situation, contact size: " + contacts.size() + " with contact userID: " + intID);
        System.exit(1);

        return null;
    }

    public boolean storeConcor(Concor p, String idFieldName, boolean skipIfPresent, boolean commitNow) {

        boolean ok = false;
        try {
            ok = storeConcor(p, idFieldName, new java.util.Date(), skipIfPresent, commitNow);
        } catch (Exception e) {
            log.error(e);
            ok = false;
        } finally {
            if (!ok) {
                log.error("breakpoint hossk");
            }
            return ok;
        }
    }

    public ConcorsiDB detachConcorsiFromJPA(Collection<Concor> concorsi) {

        log.error("codice sotto va riscritto, forse JPA1 da riscrivere in JPA2");
        for (Concor c : concorsi) {
            em.detach(c);
        }
        return this;
    }

    public ConcorsiDB updateReportedDate(Collection<Concor> concorsi) {
        updateReportedDate(concorsi, new Date(), true);
        return this;
    }

    public ConcorsiDB updateReportedDate(Collection<Concor> concorsi, Date reported, boolean detach) {

        log.info("nr reported: " + concorsi.size());

        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            if (!tx.isActive()) tx.begin();

            // serve ad attivare la sincronizzazione
            for (Concor conc : concorsi) {
                findConcorByInternalID(conc.getInfoInternURL(), Concor.IDFieldName, false);
                conc.setDateReported(reported);
                //log.info("conc reported date: " + conc.getDateReported());
            }
            tx.commit();
            if (detach) detachConcorsiFromJPA(concorsi);
        } catch (Exception e) {
            // MySQLIntegrityConstraintViolationException
            log.error("da sistemare dopo fix di un banco di eclipselink");
            //if (tx.isActive()) tx.rollback();
        }
        return this;
    }

    public boolean storeConcor(Concor conc, String idFieldName, java.util.Date creationDate, boolean skipIfPresent, boolean commitNow) {
        if (findConcorByInternalID(conc.getInfoInternURL(), idFieldName) != null) {
            log.debug("entry già presente: " + conc.toString());
        } else {
            EntityTransaction tx = null;
            try {
                log.debug("memorizzo entry non presente: " + conc.toString());
                tx = em.getTransaction();
                if (commitNow) tx.begin();
                conc.setDateRecorded(new Date());
                em.persist(conc);
                if (commitNow) tx.commit();
                em.detach(conc);
            } catch (Exception e) {
                // MySQLIntegrityConstraintViolationException
                log.error("tentavo di memorizzare:" + conc.toString());
                if (tx.isActive()) tx.rollback();
            }
        }
        return true;
    }

    public void enricoFlush() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        tx.commit();
    }

    EntityManager em;
    private static Logger log = LogManager.getLogger(ConcorsiDB.class.getSimpleName());
}
