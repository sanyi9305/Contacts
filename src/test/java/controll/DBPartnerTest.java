
package controll;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;

public class DBPartnerTest {
    EntityManager em;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
    final String p1mobil = "+36207654321";
    final String p1email = "Béci93{mail.hu";
    DBPartner partner1 = new DBPartner("Béla",p1mobil,p1email);
    DBPartner partner2 = new DBPartner("Béla","+723077777","bela@example.com");
    DBPartner partnerDB = DBPartner.ReadOne(17);
    DBPartner tmp = null;

    @Before
    public void setUp() throws Exception {

        em = emf.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        em.close();
        emf.close();
    }




    @Test
    public void chkEmail() {
        final String email = "sanyi.mail@";
      assertFalse(DBPartner.ChkEmail(email));
    }


    @Test
    public void objDiff() {

        assertTrue("Különbség van: ",partner1.ObjDiff(partner2));
    }

    @Test
    public void readOne() {
        DBPartner p = DBPartner.ReadOne(17);
        assertEquals("Találat","17",p.getId().toString());
    }


    @Test
    public void update() {
        final String name = "Bodnár Sándor";
        final String namDB  = partnerDB.getName();
        partnerDB.setName(name);
        partnerDB.Update();
        tmp = DBPartner.ReadOne(partnerDB.getId());
        assertEquals(tmp.getName(),name);

        tmp.setName(namDB);
        tmp.Update();
        assertNotEquals(tmp.getName(),name);

    }




    @Test
    public void getId() {
    }

    @Test
    public void getName() {
        assertEquals(partner1.getName(),"Béla");
    }

    @Test
    public void setName() {
        final String name = "Géza";
        partner1.setName(name);
        assertEquals(partner1.getName(),name);
    }

    @Test
    public void getMobilephone1() {
        assertEquals(partner1.getMobilephone1(),p1mobil);

    }

    @Test
    public void setMobilephone1() {
        partner2.setMobilephone1(p1mobil);
        assertEquals(partner2.getMobilephone1(),p1mobil);
    }

    @Test
    public void getEmail() {
        assertEquals(partner1.getEmail(),p1email);
    }

    @Test
    public void setEmail() {
        partner2.setEmail(p1email);
        assertEquals(partner2.getEmail(),p1email);
    }


    @Test
    public void create() {
        partner1.Create();
        DBPartner p = DBPartner.ReadOne(partner1.getId());
        assertEquals(partner1.getId(),p.getId());
        p.Delete();
    }

    @Test
    public void delete() {
        

    }

    @Test
    public void toString1() {
        final  String s = "{" +
                "Név: " + partner1.getName() +
                "\nMobilszám: " +  partner1.getMobilephone1() +
                "\nE-mailcím: " +  partner1.getEmail() +
                '}';
         assertEquals(partner1.toString(),s);
    }
}