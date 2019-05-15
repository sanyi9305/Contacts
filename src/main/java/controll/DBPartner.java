package controll;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adatbázis osztályunk mely tartalmazza az objektum tulajdonságait illetve olyan metódusokat amellyel
 * kezelhetjük az adatbázis rekordjainkat.
 */


@Entity
@Table(name = "DBPartner")

public class DBPartner {
    private static Logger logger = LoggerFactory.getLogger(DBPartner.class);

    /**
     * Azonosító.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Név.
     */
    @Column(name = "name")
    private String name;

    /**
     * Mobilszám.
     */
    @Column(name = "mobilephone1")
    private String mobilephone1;

    /**
     * E-mail cím.
     */
    @Column(name = "email")
    private String email;

    /**
     * Konstruktor mely példányosít egy objektumot a megadott paraméterek beállításával.
     *
     * @param name         Név
     * @param mobilephone1 Mobilszám
     * @param email        E-mail cím
     */
    public DBPartner(String name, String mobilephone1, String email) {
        this.name = name;
        this.mobilephone1 = mobilephone1;
        this.email = email;
        logger.info("DBPartner példányosítása" + this.toString());
    }

    /**
     * Default konstruktor.
     */
    public DBPartner() {
    }

    /**
     * Viszaadja az id alapján megtalált adatbázis rekordot a {@link DBPartner} táblából.
     * Ez csak már a
     * @param id azonosító a kereséshez
     * @return a megtalált objektum
     */
    public static DBPartner ReadOne(int id) {
        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
        em = emf.createEntityManager();
        DBPartner partner = em.find(DBPartner.class, id);
        logger.info("Megtalálva");
        return partner;
    }

    /**
     * Visszaadja, hogy a vizsgálandó e-mailcím megfelelő formátumú-e.
     *
     * @param email Vizsgálandó e-mail cím
     * @return az email illeszkedik-e mintánkra
     */
    public static boolean ChkEmail(String email) {

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Objektum perzisztálására szolgáló metódus mely az objektumpéldány adataival létrehoz egy adatbázis rekordot.
     */

    public void Create() {
        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
        DBPartner partner = this;
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(partner);
        em.getTransaction().commit();
        em.close();
        emf.close();
        logger.info("Mentve adatbázisba a következő adatokkal: " + partner.toString());
    }

    /**
     * Mentjük a módosításokat adatbázisba.
     */
    public void Update() {

        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
        em = emf.createEntityManager();
        DBPartner partner = em.find(DBPartner.class, this.id);
        if (em.contains(partner)) {
            if (this.ObjDiff(partner)) {
                partner.setName(this.name);
                partner.setMobilephone1(this.mobilephone1);
                partner.setEmail(this.email);
                em.unwrap(Session.class).update(partner);
                logger.info("Update sikeres");
            }
        }else {
            logger.error("Valami baj van nem található objektum!");
            logger.error("objektum ID: " + this.getId());
        }
        em.getTransaction().begin();
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    /**
     * Összhasonlítja hogy a két objektum mezői között van e különbség.
     * @param partner Az összehasonlítani kívánt objektum.
     * @return Ha van különbség akkor TRUE
     */
    public boolean ObjDiff(DBPartner partner) {

        return !this.name.equals(partner.getName()) || !this.mobilephone1.equals(partner.getMobilephone1()) || !this.email.equals(partner.getEmail());
    }

    /**
     * Törli az adatbázisból az objektumot.
     */
    public void Delete() {
        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
        em = emf.createEntityManager();
        DBPartner partner = em.find(DBPartner.class, this.id);
        em.getTransaction().begin();
        if (em.contains(partner)) {
            em.remove(partner);
            em.getTransaction().commit();
            logger.debug(partner.getMobilephone1() + "törölve lett");
        }


        em.close();
        emf.close();
    }

    /**
     * Visszaadja az objekum azonosítóját.
     *
     * @return objektum azonosító.
     */
    public Integer getId() {
        return id;
    }


    /**
     * Visszaadja az objekum nevét.
     *
     * @return Objektum neve
     */
    public String getName() {
        return name;
    }

    /**
     * Beállítjuk a név értékét.
     *
     * @param name beállítandó név.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Visszaadja amobilszámot.
     *
     * @return mobiltelefonszám.
     */
    public String getMobilephone1() {
        return mobilephone1;
    }

    /**
     * Beállítja a mobilszámot.
     *
     * @param mobilephone1 mobilszám.
     */
    public void setMobilephone1(String mobilephone1) {
        this.mobilephone1 = mobilephone1;
    }

    /**
     * Visszaadja az emailcímet.
     *
     * @return emailcím.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Beállítja az e-mail címet.
     *
     * @param email E-mailcím.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Visszaadja egy Stringként a partner összes adatát.
     *
     * @return partneradatok.
     */
    @Override
    public String toString() {
        return "{" +
                "Név: '" + name + '\'' +
                "\nMobilszám: " + mobilephone1 + '\'' +
                "\nE-mailcím: " + email + '\'' +
                '}';
    }
}
