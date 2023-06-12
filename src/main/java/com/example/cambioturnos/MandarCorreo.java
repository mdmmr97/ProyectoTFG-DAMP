package com.example.cambioturnos;

import com.example.cambioturnos.entidades.Grupos;
import com.example.cambioturnos.entidades.Peticiones;
import com.example.cambioturnos.entidades.Usuarios;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class MandarCorreo {
    private static String usuario;
    private static String password;
    private static Properties props;

    private MainSingleton instanceMain;
    private UserSingleton instanceUser;
    private Usuarios usuarioLogin;
    private Grupos grupo;
    private Peticiones peticion;
    private String tipoCorreo;
    private String comentario;

    private static void Propiedades() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.setProperty("mail.store.protocol", "imaps");

    }

    public void HacerCorreo() {
        instanceMain = MainSingleton.getInstance();
        instanceUser = UserSingleton.getInstance();

        usuario = instanceMain.getUSUARIO();
        password = instanceMain.getPASSWORD();

        usuarioLogin = instanceUser.getUsuarioLogin();
        grupo = instanceUser.getGrupo();
        peticion = instanceUser.getPeticion();
        tipoCorreo = instanceUser.getTipoCorreo();
        comentario = instanceUser.getComentario();

        Propiedades();
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuario, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));

            if (tipoCorreo.equals("NuevaPeticion")) {
                for (String correoUsuario : grupo.getUsuarios()) {
                    message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(correoUsuario)});
                }

                message.setSubject("Nueva Peticion de Cambio de Turno de "+ usuarioLogin.getCorreo());
                message.setText(ContenidoNuevaPeticion());
            }
            else if (tipoCorreo.equals("RespuestaPeticion")) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(peticion.getUsuario()));
                message.setSubject("Respuesta a la peticion de Cambio de Turno de "+ peticion.getUsuario());
                message.setText(ContenidoRespuestaPeticion());
            }
            Transport.send(message);
        } catch (AddressException ex) {
            throw new RuntimeException(ex);
        } catch (MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String ContenidoNuevaPeticion(){
        return "En el grupo " + grupo.getNombre() + " el usuario " + usuarioLogin.getCorreo() + " ha realizado una nueva " +
                "peticion de cambio de turno.\n La peticion se ha realizado para el turno de " + peticion.getTurno() +
                " para el dia " + peticion.getFechaturno();
    }
    private String ContenidoRespuestaPeticion(){
        String contenido = "El usuario " + usuarioLogin.getCorreo() + " está interesado en su propuesta de cambio de turno " +
                "para el dia " + peticion.getFechaturno() + "en el turno de " + peticion.getTurno();
        if (!comentario.equals(""))  contenido = contenido +"\n El usuario interesado comenta:\n "+ comentario;
        return contenido;
    }
}
