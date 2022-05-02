package com.mycompany.sudoku.view;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import kryptografia.model.cipher.ThreeDes;

public class HelloController implements Initializable {

    @FXML private Button wczytajKluczeButton;
    @FXML private Button zapiszKluczeButton;
    @FXML private Button szyfrujButton;
    @FXML private Button deszyfrujButton;
    @FXML private Button wczytajJawneButton;
    @FXML private Button zapiszJawneButton;
    @FXML private Button wczytajSzyfrogramButton;
    @FXML private Button zapiszSzyfrogramButton;
    @FXML private TextField klucz1;
    @FXML private TextField klucz2;
    @FXML private TextField klucz3;
    @FXML private Text kluczPlik;
    @FXML private TextField tekstJawny;
    @FXML private TextField szyfrogram;
    @FXML private Text plikJawny;
    @FXML private Text plikSzyfrogram;
    @FXML private RadioButton okienkoRadioButton;
    @FXML private RadioButton plikRadioButton;
    
    private File plik = null;
    private byte[] bytes;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wczytajJawneButton.setDisable(true);
        wczytajSzyfrogramButton.setDisable(true);
        tekstJawny.setDisable(false);
        szyfrogram.setDisable(false);
    }
    
    public void radioSelect(ActionEvent event) {
        if (okienkoRadioButton.isSelected()) {
            wczytajJawneButton.setDisable(true);
            wczytajSzyfrogramButton.setDisable(true);
            tekstJawny.clear();
            szyfrogram.clear();
            tekstJawny.setDisable(false);
            szyfrogram.setDisable(false);
        }
        if (plikRadioButton.isSelected()) {
            wczytajJawneButton.setDisable(false);
            wczytajSzyfrogramButton.setDisable(false);
            tekstJawny.clear();
            szyfrogram.clear();
            tekstJawny.setDisable(true);
            szyfrogram.setDisable(true);
        }
    }

    public void szyfruj(ActionEvent event) {
        if (klucz1.getText() == "" || klucz2.getText() == "" || klucz3.getText() == "") {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Prosze o wpisanie kluczy");
                a.show();
        } else {
            ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
            if (okienkoRadioButton.isSelected()) {
                String tekstyJawne = tekstJawny.getText();
                
                if (tekstyJawne != "") {
                    byte[] byteArray = tekstyJawne.getBytes(StandardCharsets.UTF_8);
                    long text = byteToLong(byteArray);
                    long encryptedData = threeDes.encrypt(text);
                    szyfrogram.setText(Long.toHexString(encryptedData));
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Prosze o wpisanie tekstu");
                    a.show();
                }
            }
            if (plikRadioButton.isSelected()) {
                String tekstyJawne = tekstJawny.getText();
                
                if (tekstyJawne != "") {
                    byte[] byteArray = tekstyJawne.getBytes(StandardCharsets.UTF_8);
                    long text = byteToLong(byteArray);
                    long encryptedData = threeDes.encrypt(text);
                    szyfrogram.setText(Long.toHexString(encryptedData));
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Prosze o wpisanie tekstu");
                    a.show();
                }
            }
        }
    }
    
    public long byteToLong (byte[] b) {
        ByteBuffer buffer = ByteBuffer.wrap(b);
        return buffer.getLong();

    }
    
    public byte[] longToByte(long l) {
        int n = String.valueOf(l).length();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(l);
        return buffer.array();
    }
    
    public void deszyfruj(ActionEvent event) {
        if (klucz1.getText() == "" || klucz2.getText() == "" || klucz3.getText() == "") {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Prosze o wpisanie kluczy");
                a.show();
        } else {
            if (okienkoRadioButton.isSelected()) {
                ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
                String szyfrogramy1 = szyfrogram.getText();
                if (szyfrogramy1 != "") {
                    Long decimal = Long.parseLong(szyfrogramy1, 16);
                    String szyfrogramy = Long.toString(decimal);
                    long text1 = Long.parseLong(szyfrogramy);
                    long decryptedData = threeDes.decrypt(text1);
                    byte[] byteArrray1 = longToByte(decryptedData);
                    String string = new String(byteArrray1);
                    tekstJawny.setText(string);
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Prosze o wpisanie szyfru");
                    a.show();
                }

            }
            if (plikRadioButton.isSelected()) {
                ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
                String szyfrogramy1 = szyfrogram.getText();
                if (szyfrogramy1 != "") {
                    Long decimal = Long.parseLong(szyfrogramy1, 16);
                    String szyfrogramy = Long.toString(decimal);
                    long text1 = Long.parseLong(szyfrogramy);
                    long decryptedData = threeDes.decrypt(text1);
                    byte[] byteArrray1 = longToByte(decryptedData);
                    String string = new String(byteArrray1);
                    tekstJawny.setText(string);
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setTitle("Error");
                    a.setHeaderText(null);
                    a.setContentText("Prosze o wpisanie szyfru");
                    a.show();
                }
            }
        }
    }

    public void wczytajKlucze(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        plik = fileChooser.showOpenDialog(null);
        String path = plik.getAbsolutePath();
        BufferedReader reader;
        String line1 = "";
        String line2= "";
        String line3= "";
        if(path != null) {

            try {
                reader = new BufferedReader(new FileReader(plik.getAbsolutePath()));
                line1 = reader.readLine();
                line2 = reader.readLine();
                line3 = reader.readLine();

                kluczPlik.setText(path);
                

            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Błąd kluczy");
                a.show();
            }
            klucz1.setText(line1);
            klucz2.setText(line2);
            klucz3.setText(line3);

        }
    }
    
    public void zapiszKlucze(ActionEvent event) {
        if (klucz1.getText() == "" || klucz2.getText() == "" || klucz3.getText() == "") {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Error");
            a.setHeaderText(null);
            a.setContentText("Prosze o wpisanie kluczy");
            a.show();
        } else {
            String klucze = klucz1.getText() + "\n" + klucz2.getText() + "\n" + klucz3.getText();
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(klucze, file);
            }
        }
    }
    
    public long setKlucz(TextField klucz) {
        String klucze = klucz.getText();
        long key1 = Long.parseLong(klucze);
        return key1;
    }
    
    
    public void zapiszJawne(ActionEvent event) {
        String tekstyJawne = tekstJawny.getText();
        //System.out.print("zapiszjawne");
        FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(tekstyJawne, file);
            }
    }
    
    public void wczytajJawne(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        plik = fileChooser.showOpenDialog(null);
        String path = plik.getAbsolutePath();
        if(plik != null) {

            try {
                bytes = Files.readAllBytes(Path.of(plik.getAbsolutePath()));

                String s = new String(bytes, StandardCharsets.UTF_8);
                tekstJawny.setText(s);
                plikJawny.setText(path);
                

            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Błąd tekstu");
                a.show();
            }

        }
    }
    
    public void zapiszSzyfr(ActionEvent event) {
        String szyfrogramy = szyfrogram.getText();
        FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(szyfrogramy, file);
            }
    }
    
    public void wczytajSzyfr(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        plik = fileChooser.showOpenDialog(null);
        String path = plik.getAbsolutePath();
        if(plik != null) {

            try {
                bytes = Files.readAllBytes(Path.of(plik.getAbsolutePath()));

                String s = new String(bytes, StandardCharsets.UTF_8);
                szyfrogram.setText(s);
                plikSzyfrogram.setText(path);
                

            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error");
                a.setHeaderText(null);
                a.setContentText("Błąd tekstu");
                a.show();
            }

        }
    }
    
    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException ex) {
        }
    }
}
