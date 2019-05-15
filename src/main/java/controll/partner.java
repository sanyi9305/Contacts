/**
 * <h1>Névjegyzék</h1>
 * A névjegyzék alkalmazásban eltudjuk menteni az ismerőseink nevét telefonszámát és e-mail címét.
 *
 * @author Bodnár Sándor
 * @version 1.0
 * @since 2019-05-15
 */
package controll;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


/**
 * Új partner felviteléhez használt képernyő.
 * Itt megtudjuk adni a Nevét,telefonyszámát,e-mail címét.
 */
public class partner {
    /**
     * Az új rekord rögzítéséhez szükséges név.
     */
    @FXML
    TextField name;
    /**
     * Az új rekord rögzítéséhez szükséges mobilszám.
     */
    @FXML
    TextField mobile1;
    /**
     * Az új rekord rögzítéséhez szükséges e-mail cím.
     */
    @FXML
    TextField email;

    /**
     * Mentés gomb mely ha megfelelnek az adatok akkor adatbázisba menti és bezárja a képernyőt.
     */
    @FXML
    Button Save;

    /**
     * A mentés gombra kattintva megvizsgáljuk, hogy a megadott adatok megfelelnek a formai követelményeknek.
     * Ha eleget tesznek a követelménynek akkor mentjük adatbázisba.
     * @param event Kattintási esemény
     */
    public void SavePartner(ActionEvent event) {
        if ((email.getText().length() > 3 && email.getText().contains("@") && email.getText().contains(".")) && name.getText().length() >= 3
                && mobile1.getText().contains("+")) {
            DBPartner partner = new DBPartner(name.getText(), mobile1.getText(), email.getText());
            partner.Create();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sikeres mentés!");
            alert.setHeaderText("Ön sikersen mentett  a következő adatokkal:");
            alert.setContentText(partner.toString());

            alert.showAndWait();
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Információ");
            alert.setHeaderText("Rossz adatok");
            alert.setContentText("Az adatoknak megfelelő formátuma: \n " +
                    "Név: abc (minimum 3 karakter hosszú)\n" +
                    "Mobil: +36307113065\n" +
                    "Email: valami@példa.hu ");

            alert.showAndWait();
        }


    }


}
