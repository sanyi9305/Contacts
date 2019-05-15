/**
 * <h1>Névjegyzék</h1>
 * A névjegyzék alkalmazásban eltudjuk menteni az ismerőseink nevét telefonszámát és e-mail címét.
 *
 * @author Bodnár Sándor
 * @version 1.0
 * @since 2019-05-15
 */
package controll;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Főképernyő vezérlő osztály.
 * Minden interakció amely a főképernyőhöz/megjelenítéshez köthető itt található.
 */
public class mainscene implements Initializable {
    /**
     * Táblázat amibe majd az adatbázis elemeinket beletöltjük.
     */
    @FXML
    TableView table;


    /**
     * A GetAllContacts metódus visszaadja az összes adatbázisba mentett rekordot.
     * @return DBPartner lista mely tartalmazza az összes elemet.
     */
    private static List<DBPartner> GetAllContacts() {
        List<DBPartner> partners = null;
        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-persistence-unit-1");
        em = emf.createEntityManager();
        TypedQuery<DBPartner> q = em.createQuery("SELECT e FROM DBPartner e", DBPartner.class);

        partners = q.getResultList();
        return partners;

    }

    /**
     * Inicializálja képernyőt feltölti a táblázatunkat a már adatbázisban lévő adatokkal.
     *
     * @param location  Relatív utvonala a gyökér objektumnak lehet  <tt>null</tt> ha nem tudjuk
     * @param resources A gyökérobjektum localizációjára használjuk lehet   <tt>null</tt> ha nem tudjuk
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setTable();

    }

    /**
     * Feltölti a táblázatunkat az adatbázis adataival valamint létrehoz minden sorhoz egy Törlés gombot mellyel törölni tudjuk a rekordokat.
     */
    public void setTable() {
        ObservableList<DBPartner> tabledata = FXCollections.observableList(GetAllContacts());
        TableColumn NameCol = new TableColumn("Név");
        TableColumn Mobilephone1Col = new TableColumn("Mobil1");
        TableColumn EmailCol = new TableColumn("E-mail");

        NameCol.setMinWidth(130);
        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        NameCol.setCellValueFactory(new PropertyValueFactory<DBPartner, String>("name"));

        NameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DBPartner, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DBPartner, String> t) {
                        DBPartner actualPartner = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        actualPartner.setName(t.getNewValue());
                        actualPartner.Update();
                    }
                }
        );


        Mobilephone1Col.setMinWidth(130);
        Mobilephone1Col.setCellFactory(TextFieldTableCell.forTableColumn());
        Mobilephone1Col.setCellValueFactory(new PropertyValueFactory<DBPartner, String>("mobilephone1"));
        Mobilephone1Col.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DBPartner, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DBPartner, String> t) {
                        DBPartner actualPartner = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        actualPartner.setMobilephone1(t.getNewValue());
                        actualPartner.Update();
                    }
                }
        );

        EmailCol.setMinWidth(130);
        EmailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        EmailCol.setCellValueFactory(new PropertyValueFactory<DBPartner, String>("email"));
        EmailCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<DBPartner, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<DBPartner, String> t) {
                        DBPartner actualPartner = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        if (DBPartner.ChkEmail(t.getNewValue())) {
                            actualPartner.setEmail(t.getNewValue());
                            actualPartner.Update();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Információ");
                            alert.setHeaderText("Rossz email");
                            alert.setContentText("Az email megfelelő formátuma:\n" +
                                    "Email: valami@példa.hu ");

                            alert.showAndWait();
                        }
                    }
                }
        );
        TableColumn RemoveCol = new TableColumn("Törlés");
        RemoveCol.setMinWidth(100);

        Callback<TableColumn<DBPartner, String>, TableCell<DBPartner, String>> cellFactory =
                new Callback<TableColumn<DBPartner, String>, TableCell<DBPartner, String>>() {
                    @Override
                    public TableCell call(final TableColumn<DBPartner, String> param) {
                        final TableCell<DBPartner, String> cell = new TableCell<DBPartner, String>() {
                            final Button btn = new Button("Törlés");

                            @Override
                            public void updateItem(String item, boolean empty) {

                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) ->
                                    {
                                        DBPartner partner = getTableView().getItems().get(getIndex());
                                        tabledata.remove(partner);
                                        partner.Delete();
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        RemoveCol.setCellFactory(cellFactory);

        table.getColumns().addAll(NameCol, Mobilephone1Col, EmailCol, RemoveCol);
        table.setItems(tabledata);
    }


    /**
     * Új névjegyet adhatunk hozzá az adatbázisunkhoz.
     * A megjelenő képernyől lehetőségünk van megadni a mentendő adatokat.
     * Ha nem felel meg a formai követelményeknek akkor a információs ablakban megjelenítjük a várt adatok formátumát.
     * @throws Exception kivétel
     */
    public void NewContact() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Stage stage = new Stage();
        Pane root = FXMLLoader.load(getClass().getResource("/fxml/partner.fxml"));


        stage.setScene(new Scene(root, 300, 275));
        stage.setTitle("Partner adatok");
        stage.showAndWait();
        setTable();
    }


}
