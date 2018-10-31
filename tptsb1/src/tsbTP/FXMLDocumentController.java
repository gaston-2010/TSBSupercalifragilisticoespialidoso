/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsbTP;

import static java.awt.SystemColor.window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.K;
import static javafx.scene.input.KeyCode.V;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafxapplication2.TSB_OAHashtable;

/**
 *
 * @author FacundoNahuel
 */
public class FXMLDocumentController implements Initializable {
    
    private Label label;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField txtfield;
    @FXML
    private AnchorPane txtarea;
    
    
     public String controlCadena(String palabra){
        palabra = palabra.replace(".", "");
        palabra = palabra.replace(",", "");
        palabra = palabra.replace("-", "");
        palabra = palabra.replace("(", "");
        palabra = palabra.replace(")", "");
        palabra = palabra.replace(":", "");
        palabra = palabra.replace(";", "");
        palabra = palabra.replace("¿", "");
        palabra = palabra.replace("?", "");
        palabra = palabra.replace("_", "");
        palabra = palabra.replace("*", "");
        palabra = palabra.replace("¡", "");
        palabra = palabra.replace("!", "");
        palabra = palabra.replace("<", "");
        palabra = palabra.replace(">", "");
        palabra = palabra.replace("[", "");
        palabra = palabra.replace("]", "");
        palabra = palabra.replace("#", "");
        palabra = palabra.replace("@", "");
        palabra = palabra.replace("0", "");
        palabra = palabra.replace("1", "");
        palabra = palabra.replace("2", "");
        palabra = palabra.replace("3", "");
        palabra = palabra.replace("4", "");
        palabra = palabra.replace("5", "");
        palabra = palabra.replace("6", "");
        palabra = palabra.replace("7", "");
        palabra = palabra.replace("8", "");
        palabra = palabra.replace("9", "");
        return palabra;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){

                }        
    private void cargarLista(File file) throws FileNotFoundException, IOException{
        String cadena;
        FileReader f = new FileReader(file);
        BufferedReader b = new BufferedReader(f);
        
        TSB_OAHashtable<String,Integer> tabla = new TSB_OAHashtable();
        int contador=0;
        while((cadena = b.readLine())!=null) {
            cadena = controlCadena(cadena);
            
            tabla.put(cadena, contador);
        }
        b.close();
   
    }
    @FXML
    private void handleButtonAction(MouseEvent event) { 
//        buscar y cargar archivo
        FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Buscar Archivo");
//            filtros
            fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Todos los Archivos","*.*"),
            new FileChooser.ExtensionFilter("*.txt","TXT"));
            try{
            Window stage = null;
            File txtArchivo = fileChooser.showOpenDialog(stage);
//            muestro ubicacion del archivo
            txtfield.textProperty().set(txtArchivo.getPath());
//         proceso archivo
            cargarLista(txtArchivo);
            
            }
            catch(Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al cargar el archivo");
            alert.setContentText("No se pudo cargar el archivo indicado.");
            alert.showAndWait();}
            
        }
   
    }

          
                
                
                
      
        
    

