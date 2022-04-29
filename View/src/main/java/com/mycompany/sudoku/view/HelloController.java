package com.mycompany.sudoku.view;



import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
    @FXML private TextField kluczPlik;
    @FXML private TextField tekstJawny;
    @FXML private TextField szyfrogram;
    @FXML private TextField plikJawny;
    @FXML private TextField plikSzyfrogram;
    @FXML private RadioButton okienkoRadioButton;
    @FXML private RadioButton plikRadioButton;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wczytajJawneButton.setDisable(true);
        wczytajSzyfrogramButton.setDisable(true);
    }
    
    public void radioSelect(ActionEvent event) {
        if (okienkoRadioButton.isSelected()) {
            System.out.print("okienkoradiobutton");
            wczytajJawneButton.setDisable(true);
            wczytajSzyfrogramButton.setDisable(true);
        }
        if (plikRadioButton.isSelected()) {
            System.out.print("plikradiobutton");
            wczytajJawneButton.setDisable(false);
            wczytajSzyfrogramButton.setDisable(false);
        }
    }

    public void szyfruj(ActionEvent event) {
        System.out.print("szyfruj");
        ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
        if (okienkoRadioButton.isSelected()) {
            String tekstyJawne = tekstJawny.getText();
            byte[] byteArrray = tekstyJawne.getBytes();
            long text = byteToLong(byteArrray);
            System.out.println(text);
            long encryptedData = threeDes.encrypt(text);
            szyfrogram.setText(Long.toHexString(encryptedData));

        }
        if (plikRadioButton.isSelected()) {
            System.out.print("plikradiobutton");

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
        System.out.print("deszyfruj");
        if (okienkoRadioButton.isSelected()) {
            ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
            String szyfrogramy1 = szyfrogram.getText();
            Long decimal = Long.parseLong(szyfrogramy1, 16);
            String szyfrogramy = Long.toString(decimal);
            long text1 = Long.valueOf(szyfrogramy).longValue();
            long decryptedData = threeDes.decrypt(text1);
            byte[] byteArrray1 = longToByte(decryptedData);
            String string = new String(byteArrray1);
            tekstJawny.setText(string);

        }
        if (plikRadioButton.isSelected()) {
            System.out.print("plikradiobutton");

        }

    }

    public void wczytajKlucze(ActionEvent event) {
        System.out.print("wczytajklucze");
    }
    
    public void zapiszKlucze(ActionEvent event) {
        long key1 = setKlucz(klucz1);
        long key2 = setKlucz(klucz2);
        long key3 = setKlucz(klucz3);
        String klucze = klucz1.getText() + "\n" + klucz2.getText() + "\n" + klucz3.getText();
        System.out.print(key1);
        System.out.print(key2);
        System.out.print(key3);
        System.out.print("zapiszklucze");
        
        FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(klucze, file);
            }
    }
    
    public long setKlucz(TextField klucz) {
        String klucze = klucz.getText();
        long key1 = Long.valueOf(klucze).longValue();
        return key1;
    }
    
    
    public void zapiszJawne(ActionEvent event) {
        String tekstyJawne = tekstJawny.getText();
        System.out.print("zapiszjawne");
        FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(tekstyJawne, file);
            }
    }
    
    public void wczytajJawne(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(null);
    }
    
    public void zapiszSzyfr(ActionEvent event) {
        System.out.print("zapiszszyfr");
        String szyfrogramy = szyfrogram.getText();
        FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showSaveDialog(null);
 
            if (file != null) {
                saveTextToFile(szyfrogramy, file);
            }
    }
    
    public void wczytajSzyfr(ActionEvent event) {
        System.out.print("wczytajszyfr");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.showOpenDialog(null);
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
