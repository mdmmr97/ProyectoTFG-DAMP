package com.example.cambioturnos;

import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Main extends Application {

    final String FICHERO_CONFIGURACION = "settings.properties";
    static Properties configuracion = new Properties();
    static MongoDatabase dataBaseMongo;
    static MongoClient clienteMongo;

    public static MongoCollection<Usuarios> coleccionUser;
    public static ObservableList<Usuarios> listauser = FXCollections.observableArrayList();
    public static MongoCollection<Grupos> coleccionGrupos;
    public static ObservableList<Grupos> listagrupos = FXCollections.observableArrayList();;
    public static MongoCollection<Peticiones> coleccionPeticiones;
    public static ObservableList<Peticiones> listapeticiones = FXCollections.observableArrayList();
    public static MainSingleton instance;

    @Override
    public void start(Stage stage) throws IOException {

        instance = MainSingleton.getInstance();
        cargarConfiguracion(FICHERO_CONFIGURACION,configuracion);
        contectarBaseDeDatos(configuracion);

        instance.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
    private void cargarConfiguracion(String ficheroConfiguracion, Properties config) {
        try {
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(ficheroConfiguracion);
            config.load(input);
        } catch (Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error al leer el fichero de configuración");
            alerta.setTitle("Error al leer el fichero de configuración");
            alerta.setContentText("ERROR: No se ha podido leer el contenido del fichero " + ficheroConfiguracion );
            alerta.showAndWait();
            System.exit(1);
        }
    }
    private void contectarBaseDeDatos(Properties config) {

        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        try {
            clienteMongo = MongoClients.create(config.getProperty("MONGODB_URI"));
            dataBaseMongo = clienteMongo.getDatabase(config.getProperty("MONGODB_DATABASE")).withCodecRegistry(pojoCodecRegistry);

            coleccionUser = dataBaseMongo.getCollection(config.getProperty("COLLECTION_USER"), Usuarios.class);
            coleccionUser.find().into(listauser);

            instance.setColeccionUser(coleccionUser);
            instance.setListauser(listauser);

            coleccionGrupos = dataBaseMongo.getCollection(config.getProperty("COLLECTION_GRUP"), Grupos.class);
            coleccionGrupos.find().into(listagrupos);

            instance.setColeccionGrupos(coleccionGrupos);
            instance.setListagrupos(listagrupos);
            /*
            coleccionPeticiones = dataBaseMongo.getCollection(config.getProperty("COLLECTION_TURN"), Peticiones.class);
            coleccionPeticiones.find().into(listapeticiones);
            instance.setListapeticiones(listapeticiones);*/

        } catch ( Exception e) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Error no se ha podido conectar a la base de datos");
            alerta.setTitle("Error no se ha podido conectar a la base de datos");
            alerta.setContentText("ERROR: No se ha podido conectar a la base de datos " + config.getProperty("MONGODB_DATABASE"));
            alerta.showAndWait();
            System.exit(2);
        }
    }
    public static void main(String[] args) {
        launch();
    }
}