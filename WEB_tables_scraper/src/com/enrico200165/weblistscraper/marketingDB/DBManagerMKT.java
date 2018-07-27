package com.enrico200165.weblistscraper.marketingDB;

import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManagerMKT {

    public DBManagerMKT(String persistUnit) {
        super();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(persistUnit);
        em = factory.createEntityManager();
        //for (String key : em.getProperties().keySet()) {
        //   log.info(key + " = " + em.getProperties().get(key));
        //}
    }


    long getNrofProspForHome(String homeID) {
        long ret = 0;

        try {
            EntityTransaction entr = em.getTransaction();
            entr.begin();

            String sql = "SELECT COUNT ( p.internalID) FROM ProspectCoreData p";
            if ((homeID != null)) {
                sql += " WHERE p.homeID = :homeIDPar";
            }

            Query query = em.createQuery(sql);

            if (homeID != null) {
                query.setParameter("homeIDPar", homeID);
            }

            Number cResults = (Number) query.getSingleResult();
            System.out.println("Total Count result = " + cResults);
            entr.commit();
            return cResults.longValue();
        } catch (Exception e) {
            log.error("");
            return -1;
        }
    }


    public List<String> selectProspectHomes() {

        ArrayList<String> ret = new ArrayList<String>();

        // -- set up iniziale ---
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> cQuery = criteriaBuilder.createTupleQuery();
        Root<ProspectCoreData> root = cQuery.from(ProspectCoreData.class);

        // --- columns
        cQuery.multiselect(root.get("homeID")).distinct(true);

        List<Tuple> tupleResult = em.createQuery(cQuery).getResultList();

        for (Tuple t : tupleResult) {
            String item = (String) t.get(0);
            ret.add(item);
        }

        return ret;
    }

    public List<ProspectCoreData> selectProspects(String home) {

        // -- set up iniziale ---
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProspectCoreData> query = criteriaBuilder.createQuery(ProspectCoreData.class);
        Root<ProspectCoreData> from = query.from(ProspectCoreData.class);
        query.select(from);

        // --- where
        Expression<String> column = from.get("homeID");
        // --- dichiara parametri
        ParameterExpression<String> userPar = criteriaBuilder.parameter(String.class);

        // --- --- avvio where
        query.where(criteriaBuilder.equal(column, userPar));
        TypedQuery<ProspectCoreData> q = em.createQuery(query);

        // --- ---- vincola parametri
        q.setParameter(userPar, home);
        // --- estrazione
        List<ProspectCoreData> contacts = q.getResultList();

        return contacts;
    }


    void dumpProsp(List<ProspectABC> prospects) {
        for (ProspectABC p : prospects) {
            log.info(p.dump());
        }
    }

    void dump(List<ProspectCoreData> prospects) {
        for (ProspectCoreData p : prospects) {
            log.info(p.dump());
        }
    }


    public void dumpProspectsForHome(String home) {
        List<ProspectCoreData> prospects = selectProspects(home);
        dump(prospects);
    }


    public List<ProspectCoreData> selectProspectsSAVE(String homeID) {

        // -- set up iniziale ---
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProspectCoreData> query = criteriaBuilder.createQuery(ProspectCoreData.class);
        Root<ProspectCoreData> from = query.from(ProspectCoreData.class);
        query.select(from);

        // --- order
        query.orderBy(criteriaBuilder.asc(from.get("scoreActOnThis")));
        //q.orderBy(cb.asc(c.get("currency")), cb.desc(c.get("population")));

        // --- where
        // --- dichiara parametri
        Expression<String> homeIDCol = from.get("homeID");
        ParameterExpression<String> homeIDVal = criteriaBuilder.parameter(String.class);
        Predicate hasChannel = criteriaBuilder.equal(homeIDCol, homeIDVal);

        Expression<Integer> ageCol = from.get("age");
        ParameterExpression<Integer> ageMinVal = criteriaBuilder.parameter(Integer.class);
        Predicate ageGreaterThanVal = criteriaBuilder.greaterThanOrEqualTo(ageCol, ageMinVal);

        Predicate whereConditions = criteriaBuilder.and(hasChannel, ageGreaterThanVal);

        // --- --- avvio where
        query.where(whereConditions);
        TypedQuery<ProspectCoreData> q = em.createQuery(query);

        // --- ---- vincola parametri
        q.setParameter(homeIDVal, homeID);
        q.setParameter(ageMinVal, 10);
        // --- estrazione
        List<ProspectCoreData> contacts = q.getResultList();

        return contacts;
    }


    public EntityManager it() {
        return em;
    }

    public ProspectCoreData findProspectByInternalID(long userID) {

        // -- set up iniziale ---
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<ProspectCoreData> query = criteriaBuilder.createQuery(ProspectCoreData.class);
        Root<ProspectCoreData> from = query.from(ProspectCoreData.class);
        query.select(from);

        // --- where
        Expression<String> column = from.get("internalID");
        ParameterExpression<Long> userPar = criteriaBuilder.parameter(Long.class);
        query.where(criteriaBuilder.equal(column, userPar));

        // --- finalizzazione
        TypedQuery<ProspectCoreData> q = em.createQuery(query);
        q.setParameter(userPar, userID);
        // --- estrazione
        List<ProspectCoreData> contacts = q.getResultList();

        if (contacts.size() == 0) return null;
        if (contacts.size() == 1) {
            ProspectCoreData p = contacts.get(0);
            p.setFrom(ProspectCoreData.From.CONTACT_HISTORY);
            em.detach(p);
            return p;
        }
        log.error("Exiting abnormal situation with contact userID: " + userID);
        System.exit(1);
        return null;

    }


    public boolean storeProspects(List<ProspectABC> prospects, java.util.Date creationDate, boolean skipIfPresent, boolean commitNow) {

        List<ProspectCoreData> coreData = new ArrayList<ProspectCoreData>();

        for (ProspectABC prospect : prospects) {
            coreData.add(prospect.it());
        }

        storeProspCoreData(coreData, creationDate, skipIfPresent, commitNow);
        return true;
    }


    public int storeProspCoreData(List<ProspectCoreData> prospects, java.util.Date creationDate, boolean skipIfPresent, boolean commitNow) {

        int nrStored = 0;

        EntityTransaction tx = em.getTransaction();
        if (commitNow) tx.begin();

        for (ProspectCoreData prospect : prospects) {
            // log.info("prima prospect, internal ID: "+prospect+" "+prospect.getInternalID());
            if (creationDate != null)
                prospect.setProspectRecordedTStamp(creationDate);
            em.persist(prospect);
            // log.info("dopo prospect, internal ID: "+prospect+" "+prospect.getInternalID());
            nrStored++;
            // non posso fare detach qui, viene staccato
        }
        if (commitNow) {
            if (tx.isActive()) tx.commit();
            else log.warn("committing and inactive tx, ignoring");
        }

        for (ProspectCoreData prospect : prospects) {
            em.detach(prospect);
        }
        return nrStored;
    }


    public boolean storeOneProspect(ProspectABC p, boolean skipIfPresent, boolean commitNow) {
        return storeOneProspect(p.it(), new java.util.Date(), skipIfPresent, commitNow);
    }


    public boolean storeOneProspect(ProspectCoreData p, boolean skipIfPresent, boolean commitNow) {
        return storeOneProspect(p, new java.util.Date(), skipIfPresent, commitNow);
    }

    public boolean storeOneProspect(ProspectCoreData prospect, java.util.Date creationDate, boolean skipIfPresent, boolean commitNow) {
        if (findProspectByInternalID(prospect.getInternalID()) != null) {
            log.info("contatto già presente: " + prospect.IDsToString());
        } else {
            log.info("memorizzo prospect non presente: " + prospect.IDsToString());
            EntityTransaction tx = em.getTransaction();
            if (commitNow) tx.begin();
            em.persist(prospect);
            if (commitNow) tx.commit();
            em.detach(prospect);
        }
        return true;
    }


    public boolean storeContact(Contact c, boolean skipIfPresent, boolean commitNow) {
        return storeContact(c, new java.util.Date(), skipIfPresent, commitNow);
    }

    public boolean storeContact(Contact contact, java.util.Date d, boolean skipIfPresent, boolean commitNow) {

        if (contact.getProspectID() == null || contact.getProspectID().length() <= 0) {
            log.error("contact ID null for contact: " + "\n" + contact.toString());
            System.exit(1);
        }

        if (findProspectByInternalID(contact.getProspectFK()) == null) {
            // dovrei controllare entrambe le foreign key
            log.error("ESCO: prospect non esistente per contatto, prospect ID: " + contact.getProspectID());
            System.exit(1);
            return false;
        }

        {
            // log.info("memorizzo contatto non presente, verso prospect: " + contact.getProspectID());
            EntityTransaction tx = em.getTransaction();
            if (commitNow) tx.begin();
            em.persist(contact);
            if (commitNow) tx.commit();
            em.detach(contact);
        }
        return true;
    }

    void dumpContacts(List<Contact> contacts) {
        int i = 0;
        for (Contact c : contacts) {
            log.info((i++) +" " +c.dump());
        }
    }

    public List<Contact> selectContacts(String homeID, Date startDate, Date endDate) {

        // -- set up iniziale ---
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Contact> query = cb.createQuery(Contact.class);

        Root<Contact> contact = query.from(Contact.class);
        // query.select(contact);

        // --- where
        Expression<String> channelIDCol = contact.get("channelID");
        ParameterExpression<String> channelIDPar = cb.parameter(String.class);

        // --- dichiara parametri

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(channelIDCol,channelIDPar));

        query.where(predicates.toArray(new Predicate[]{}));
        // --- --- avvio where
        TypedQuery<Contact> q = em.createQuery(query);

        // --- ---- vincola parametri
        q.setParameter(channelIDPar, homeID);
        // --- estrazione
        List<Contact> contacts = q.setParameter(channelIDPar, homeID).getResultList();

        //  dumpContacts(contacts);

        return contacts;

    }


    public int getNrofContInDates(String homeID, Date startDate, Date endDate) {
        int ret = 0;

        try {
            EntityTransaction entr = em.getTransaction();
            entr.begin();

            String sql = "SELECT COUNT ( c.internalID) FROM Contact c, ProspectCoreData d";
            if ((homeID != null)) {
                sql += " WHERE d.homeID = :homeIDPar";
                sql += " AND c.contactDate BETWEEN :startDatePar  AND :endDatePar";
            }

            Query query = em.createQuery(sql);

            query.setParameter("homeIDPar", homeID);
            query.setParameter("startDatePar", startDate);
            query.setParameter("endDatePar", endDate);

            Number cResults = (Number) query.getSingleResult();
            System.out.println("Total Count result = " + cResults);
            entr.commit();
            return cResults.intValue();
        } catch (Exception e) {
            log.error("query to get contacts in date failed", e);
            return -1;
        }

    }

    public void enricoFlush() {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        tx.commit();
    }

    private EntityManager em;
    private static org.apache.log4j.Logger log = Logger.getLogger(DBManagerMKT.class);
}
