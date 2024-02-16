package com.mycompany.sudoku.view;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
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
    @FXML private Button zakodujPlikButton;
    @FXML private Button odkodujPlikButton;
    @FXML private TextField klucz1;
    @FXML private TextField klucz2;
    @FXML private TextField klucz3;
    @FXML private Text kluczPlik;
    @FXML private TextArea tekstJawny;
    @FXML private TextArea szyfrogram;
    @FXML private Text plikJawny;
    @FXML private Text plikSzyfrogram;
    @FXML private RadioButton okienkoRadioButton;
    @FXML private RadioButton plikRadioButton;
    
    private File plik = null;
    private File plik2 = null;
    private File plik3 = null;
    private byte[] bytes;
    byte[] resultbytes;
    int number;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wczytajJawneButton.setDisable(true);
        wczytajSzyfrogramButton.setDisable(true);
        tekstJawny.setDisable(false);
        szyfrogram.setDisable(false);
        tekstJawny.setWrapText(true);
        szyfrogram.setWrapText(true);
    }
    
    public void radioSelect(ActionEvent event) {
        if (okienkoRadioButton.isSelected()) {
            wczytajJawneButton.setDisable(true);
            wczytajSzyfrogramButton.setDisable(true);
            tekstJawny.clear();
            szyfrogram.clear();
            tekstJawny.setDisable(false);
            szyfrogram.setDisable(false);
            plikJawny.setText("");
            plikSzyfrogram.setText("");            
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
                alert("Prosze o wpisanie kluczy");
        } else {
            ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
            String tekstyJawne = tekstJawny.getText();
            String[] strings = divideInto64bit(tekstyJawne);
            int len = strings.length;
            if (tekstyJawne != "") {
                String part = "";
                String hex = "";
                for(int i=0; i< len; i++) {
                    byte[] byteArray = strings[i].getBytes();
                    long text = byteToLong(byteArray);
                    long encryptedData = threeDes.encrypt(text);
                    hex = Long.toHexString(encryptedData);
                    if (hex.length() != 16) {
                        String help = "";
                        int add = hex.length() % 16;
                        for (int j = 0; j < (16-add);j++) {
                            help += "0";
                        }
                        hex = help + hex;
                    }
                    part += hex;
                }
                    szyfrogram.setText(part);
            } else {
                alert("Prosze o wpisanie tekstu");
            }
        }
    }
    
    public String[] divideInto64bit(String s) {
        number = 0;
        int diff = 8 - (s.length() % 8);
        String dif = Integer.toString(diff);
        if (s.length() % 8 != 0) {
            for(int i = 0; i < diff; i++) {
                if (diff != 0) {
                    s += " ";
                    number += 1;
                }
            }
        }
        int n = s.length()/8;  
        int temp = 0; 
        int chars = 8;
        String[] equalStr = new String [n];  
        for(int i = 0; i < s.length(); i = i+chars) {  
            String part = s.substring(i, i+chars);  
            equalStr[temp] = part;  
            temp++;  
        }
        return equalStr;
    }
    
    public String[] divideCypher(String s) {
        int n = s.length()/16;  
        int temp = 0; 
        int chars = 16;
        String[] equalStr = new String [n];  
        for(int i = 0; i < s.length(); i = i+chars) {
                String part = s.substring(i, i+chars);  
                equalStr[temp] = part;  
                temp++;  
            }
        return equalStr;
    }
    
    
    public void deszyfruj(ActionEvent event) {
        if (klucz1.getText() == "" || klucz2.getText() == "" || klucz3.getText() == "") {
                alert("Prosze o wpisanie kluczy");
        } else {
            ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
            String szyfrogramy1 = szyfrogram.getText();
            String[] strings = divideCypher(szyfrogramy1);
            int len = strings.length;
            if (szyfrogramy1 != "") {
                String part = "";
                for(int i=0; i< len; i++) {
                    byte[] decimal = hexStringToBytes(strings[i]);
                    long text1 = byteToLong(decimal);
                    long decryptedData = threeDes.decrypt(text1);
                    byte[] byteArray1 = longToByte(decryptedData);
                    String string = new String(byteArray1);
                    part += string;
                }
                tekstJawny.setText(part);
                resultbytes = part.getBytes();
            } else {
                alert("Prosze o wpisanie szyfru");
            }
        }
    }
    
    public void wczytajKlucze(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z kluczami");
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
                alert("Błąd kluczy");
            }
            klucz1.setText(line1);
            klucz2.setText(line2);
            klucz3.setText(line3);
        }
        
    }
    
    public void encodeBase64() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do zakodowania");
        plik2 = fileChooser.showOpenDialog(null);
        String path2 = plik2.getAbsolutePath();
        plikJawny.setText(path2);
        
        ThreeDes threeDes = new ThreeDes(setKlucz(klucz1),setKlucz(klucz2),setKlucz(klucz3));
        
        byte[] inFileBytes = Files.readAllBytes(Paths.get(path2)); 
        byte[] encoded = java.util.Base64.getEncoder().encode(inFileBytes);
        String result = new String(encoded);
        tekstJawny.setText(result);
    }
    
    public void decodeBase64() throws FileNotFoundException, IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik do ktorego zapisac odkodowany plik");
        plik3 = fileChooser.showOpenDialog(null);
        String path3 = plik3.getAbsolutePath();
        byte[] resultbytes2 = new byte[resultbytes.length/2];
        int ile = 0;
        for(int k = 0; k < resultbytes.length;k=k+16) {
            for(int d = 0;d<8;d++) {
                resultbytes2[ile] = resultbytes[k+d];
                ile++;
            }
        }
        byte[] decoded = java.util.Base64.getDecoder().decode(resultbytes2);
        try (FileOutputStream fos = new FileOutputStream(path3)) {
            fos.write(decoded);
            fos.flush();
        }
    }
    
    public void zapiszKlucze(ActionEvent event) {
        if (klucz1.getText() == "" || klucz2.getText() == "" || klucz3.getText() == "") {
            alert("Prosze o wpisanie kluczy");
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
    
    
    public void zapiszJawne(ActionEvent event) throws IOException {
        byte[] allBytes;
        allBytes = Base64.getEncoder().encode(tekstJawny.getText().getBytes());
        byte[] decodedString = Base64.getDecoder().decode(new String(allBytes).getBytes("UTF-8"));
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                Files.write(file.toPath(), decodedString);
            }
    }
    
    
    
    public void wczytajJawne(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik z tekstem jawnym");
        plik = fileChooser.showOpenDialog(null);
        String path = plik.getAbsolutePath();
        if(plik != null) {
            try {
                bytes = Files.readAllBytes(Path.of(plik.getAbsolutePath()));
                String s = new String(bytes);
                tekstJawny.setText(s);
                plikJawny.setText(path);
            } catch (IOException e) {
                alert("Błąd tekstu");
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
        fileChooser.setTitle("Wybierz plik z szyfrem");
        plik = fileChooser.showOpenDialog(null);
        String path = plik.getAbsolutePath();
        if(plik != null) {
            try {
                bytes = Files.readAllBytes(Path.of(plik.getAbsolutePath()));
                String s = new String(bytes);
                szyfrogram.setText(s);
                plikSzyfrogram.setText(path);
            } catch (IOException e) {
                alert("Błąd tekstu");
            }
        }
    }
    
    private void saveTextToFile(String content, File file) {
        Path path = file.toPath();
        try {
            Files.writeString(path, content,StandardCharsets.UTF_8);
        } catch (IOException ex) {
        }
    }
    
    public long byteToLong(byte[] bytes)
    {
        long value = 0L;
        for (byte b : bytes) {
            value = (value << 8) + (b & 255);
        }
        return value;
    }

    public byte[] longToByte(long l) {
        int n = String.valueOf(l).length();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(l);
        return buffer.array();
    }
    
    public byte[] hexStringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
        
    public void alert(String text) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(text);
        a.show();
    }    
    
    public void zakodujPlik(ActionEvent event) throws IOException {
        encodeBase64();
        szyfruj(event); 
        zapiszSzyfr(event);
    }
    
    public void odkodujPlik(ActionEvent event) throws IOException {
        wczytajSzyfr(event);
        deszyfruj(event);
        decodeBase64();
    }
}
    


