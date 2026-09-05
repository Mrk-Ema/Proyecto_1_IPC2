/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.transportes.conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author mrk-ema
 */
public class Conexion {

    private static final String ARCHIVO_CONFIG = "db.properties";
    private static final String URL_DEFAULT
            = "jdbc:mysql://localhost:3306/transporte_extraurbano?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO_DEFAULT = "mrk";
    private static final String PASSWORD_DEFAULT = "scp049";

    private static String url = URL_DEFAULT;
    private static String usuario = USUARIO_DEFAULT;
    private static String password = PASSWORD_DEFAULT;

    static {
        Properties propiedades = new Properties();
        boolean leido = false;

        File externo = Paths.get(ARCHIVO_CONFIG).toFile();
        try (InputStream entrada = new FileInputStream(externo)) {
            propiedades.load(entrada);
            leido = true;
        } catch (IOException e) {
            try (InputStream entrada = Conexion.class.getClassLoader()
                    .getResourceAsStream(ARCHIVO_CONFIG)) {
                if (entrada != null) {
                    propiedades.load(entrada);
                    leido = true;
                }
            } catch (IOException ex) {
                // no se pudo leer
            }
        }

        if (leido) {
            url = propiedades.getProperty("url", URL_DEFAULT);
            usuario = propiedades.getProperty("usuario", USUARIO_DEFAULT);
            password = propiedades.getProperty("password", PASSWORD_DEFAULT);
        }
    }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(url, usuario, password);
    }
}
