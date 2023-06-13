package com.example.cambioturnos.controller;

import com.example.cambioturnos.Main;
import com.example.cambioturnos.MainSingleton;
import com.example.cambioturnos.UserSingleton;
import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.client.MongoCollection;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class PeticionesController implements Initializable {

    @FXML
    private TableView<Peticiones> tablaPeticiones;
    @FXML
    private TableColumn<Peticiones, LocalDate> colFecha;
    @FXML
    private TableColumn<Peticiones, String> colUser;
    @FXML
    private TableColumn<Peticiones, String> colTurno;
    @FXML
    private VBox menu;
    @FXML
    private Button todaspeticiones;
    @FXML
    private Button verpeticion;
    @FXML
    private Label nombregrupo;


    private UserSingleton instanceUser;
    private MainSingleton instanceMain;
    private Usuarios usuario;
    private Grupos grupo;
    private Peticiones peticion;
    private MongoCollection<Peticiones> coleccionPeticiones;
    private MongoCollection<Grupos> coleccionGrupos;
    private ObservableList<Grupos> listagrupos;
    private ObservableList<Peticiones> listapeticiones;
    private BorderPane nodo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        usuario = instanceUser.getUsuarioLogin();
        grupo = instanceUser.getGrupo();

        coleccionGrupos = instanceMain.getColeccionGrupos();
        coleccionPeticiones = instanceMain.getColeccionPeticiones();

        listagrupos = instanceMain.getListagrupos();
        listapeticiones = instanceMain.getListapeticiones();

        nombregrupo.setText(grupo.getNombre());
        verpeticion.setDisable(true);
        verpeticion.setVisible(false);
        menu.setVisible(false);
        menu.setManaged(false);
        todaspeticiones.setVisible(false);
        todaspeticiones.setManaged(false);

        colFecha.setCellValueFactory(param -> {
            String fecha = param.getValue().getFechaturno();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dato = LocalDate.parse(fecha,formatter);
            return new SimpleObjectProperty(dato);
        });
        colUser.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colTurno.setCellValueFactory(new PropertyValueFactory<>("turno"));

        EliminarPeticiones();
        tablaPeticiones.setItems(listapeticiones.filtered(peticiones -> !peticiones.getUsuario().equals(usuario.getCorreo())
                                 && peticiones.getGrupo().equals(grupo.getId())));
    }

    @FXML
    void AñadirPeticion(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("CrearPeticion.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void TodasPeticines(ActionEvent event) {
        tablaPeticiones.setItems(listapeticiones.filtered(peticiones -> !peticiones.getUsuario().equals(usuario.getCorreo())));
        todaspeticiones.setVisible(false);
        todaspeticiones.setManaged(false);
        menu.setVisible(false);
        menu.setManaged(false);
    }

    @FXML
    void VerMisPeticiones(ActionEvent event) {
        tablaPeticiones.setItems(listapeticiones.filtered(peticiones -> peticiones.getUsuario().equals(usuario.getCorreo())
                                 && peticiones.getGrupo().equals(grupo.getId())));

        menu.setVisible(!menu.isVisible());
        menu.setManaged(!menu.isManaged());
        todaspeticiones.setVisible(!todaspeticiones.isVisible());
        todaspeticiones.setManaged(!todaspeticiones.isManaged());
    }

    @FXML
    void ModificarPeticion(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("ModificarPeticion.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void BorrarPeticion(ActionEvent event) {

        Document viejogrupo = new Document("_id",grupo.getId());
        Document peticionDel = new Document("_id", peticion.getId());

        coleccionPeticiones.deleteOne(peticionDel);
        listapeticiones.remove(peticion);

        grupo.getPeticiones().remove(peticion.getId());
        coleccionGrupos.replaceOne(viejogrupo,grupo);
    }

    @FXML
    void ElegirPeticion(MouseEvent event) {
        peticion = tablaPeticiones.getSelectionModel().getSelectedItem();
        verpeticion.setDisable(false);
        verpeticion.setVisible(true);

    }

    @FXML
    void InformacionGrupo(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("InformacionGrupo.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void VerPeticion(ActionEvent event) {
        try {
            nodo = instanceUser.getNodo();
            instanceUser.setPeticion(peticion);
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("InformacionPeticion.fxml"));
            nodo.setCenter(fxmlloader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void VolverGrupos(ActionEvent event) {
        try {
            Stage myStage = instanceMain.getStage();
            FXMLLoader fxmlloader = new FXMLLoader(Main.class.getResource("Grupos.fxml"));
            nodo = fxmlloader.load();
            instanceUser.setNodo(nodo);
            Scene escena2 = new Scene(nodo);
            myStage.setTitle("Intercambiar Turnos");
            myStage.setScene(escena2);
            myStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void EliminarPeticiones(){
        ObservableList<Peticiones> listpeticionesdel = listapeticiones.filtered(peticiones -> peticiones.getGrupo().equals(grupo.getId())
                && (peticiones.getFechaturno().equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                || peticiones.getEstado().equals("CERRADA")));

        Document viejogrupo = new Document("codigo",grupo.getCodigo());

        if (grupo.getPeticiones() == null) {
            List<ObjectId> listapeticionesId = new ArrayList<>();
            grupo.setPeticiones(listapeticionesId);
        }

        if (listpeticionesdel.size() > 0) {

            for (Peticiones peticiones : listapeticiones) {

                if (listpeticionesdel.contains(peticiones)){
                    Document peticionDel = new Document("_id", peticiones.getId());
                    coleccionPeticiones.deleteOne(peticionDel);

                    grupo.getPeticiones().remove(peticiones.getId());
                }
            }
            listapeticiones.removeAll(listpeticionesdel);
            coleccionGrupos.replaceOne(viejogrupo,grupo);
        }
    }
}

