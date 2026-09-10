package com.mycompany.transportes.servlets;

import com.mycompany.transportes.dao.ChoferDAO;
import com.mycompany.transportes.dao.UsuarioDAO;
import com.mycompany.transportes.modelo.Chofer;
import com.mycompany.transportes.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet(name = "SvUsuario", urlPatterns = {"/SvUsuario"})
public class SvUsuario extends HttpServlet {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private ChoferDAO choferDAO = new ChoferDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tipoRegistro = request.getParameter("tipoRegistro");

        if (tipoRegistro == null) {
            tipoRegistro = "cliente";
        }

        try {
            switch (tipoRegistro) {
                case "cliente" -> {
                    // Registro público de cliente
                    if (validarDatos(request, response, "cliente")) {
                        return;
                    }
                    Usuario nuevoCliente = crearUsuarioDesdeRequest(request, "CLIENTE");
                    usuarioDAO.crear(nuevoCliente);
                    response.sendRedirect("home.jsp?registro=exito");
                }
                case "admin_sucursal" -> {
                    // Creado por ADMIN_SISTEMA
                    if (validarDatos(request, response, "admin_sucursal")) {
                        return;
                    }
                    int idSucursalOrigen = Integer.parseInt(request.getParameter("idSucursalOrigen"));
                    Usuario nuevoAdminSucursal = crearUsuarioDesdeRequest(request, "ADMIN_SUCURSAL");
                    nuevoAdminSucursal.setIdSucursalOrigen(idSucursalOrigen);
                    usuarioDAO.crear(nuevoAdminSucursal);
                    response.sendRedirect("crearAdmiSucursal.jsp?exito=admin_creado");
                }
                case "chofer" -> {
                    // Creado por ADMIN_SUCURSAL
                    if (validarDatos(request, response, "chofer")) {
                        return;
                    }
                    HttpSession session = request.getSession(false);
                    Usuario adminActual = (Usuario) session.getAttribute("usuario");
                    int idSucursalOrigen = adminActual.getIdSucursalOrigen();
                    // Crear usuario con rol CHOFER
                    Usuario nuevoChoferUsuario = crearUsuarioDesdeRequest(request, "CHOFER");
                    nuevoChoferUsuario.setIdSucursalOrigen(idSucursalOrigen);
                    usuarioDAO.crear(nuevoChoferUsuario);
                    // Datos específicos de chofer
                    String dpi = request.getParameter("dpi");
                    String numLicencia = request.getParameter("numLicencia");
                    String tipoLicencia = request.getParameter("tipoLicencia");
                    Date fechaVencimiento = Date.valueOf(request.getParameter("fechaVencimientoLicencia"));
                    double salarioBase = Double.parseDouble(request.getParameter("salarioBaseViaje"));
                    String foto = request.getParameter("foto");
                    // Crear registro en tabla chofer
                    Chofer nuevoChofer = new Chofer(dpi, foto, numLicencia, tipoLicencia, fechaVencimiento, salarioBase);
                    choferDAO.crear(nuevoChofer);
                    response.sendRedirect("crearChofer.jsp?exito=chofer_creado");
                }
                default -> {
                }
            }

        } catch (SQLException | IllegalArgumentException e) {
            throw new ServletException("Error al procesar el registro de usuario: " + e.getMessage(), e);
        }
    }

    private Usuario crearUsuarioDesdeRequest(HttpServletRequest request, String rol) {
        // Crear usuario con rol variado
        String dpi = request.getParameter("dpi");
        String nombreCompleto = request.getParameter("nombreCompleto");
        String nit = request.getParameter("nit");
        String telefono = request.getParameter("telefono");
        String direccion = request.getParameter("direccion");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        return new Usuario(dpi, nombreCompleto, nit, telefono, direccion, correo, password, rol, 0.0);
    }

    private boolean validarDatos(HttpServletRequest request, HttpServletResponse response, String tipoRegistro)
            throws ServletException, IOException, SQLException {
        //debo especificar los errores porque solo se los mando con throws

        boolean esChofer = tipoRegistro.equals("chofer");

        // validar que no haya campos vacíos o en blanco
        String errorVacio = validarCamposVacios(request, esChofer);
        if (errorVacio != null) {
            enviarErrorYRetornar(request, response, errorVacio, tipoRegistro);
            return true;
        }
        
        //validar que no existan correos o DPIs duplicados
        String errorDuplicado = validarDuplicados(request.getParameter("dpi"), request.getParameter("correo"));
        if (errorDuplicado != null) {
            enviarErrorYRetornar(request, response, errorDuplicado, tipoRegistro);
            return true;
        }

        // si están llenos, validar formatos comunes
        String errorComun = validarCamposComunes(request);
        if (errorComun != null) {
            enviarErrorYRetornar(request, response, errorComun, tipoRegistro);
            return true;
        }

        // si es chofer, validar formatos específicos de chofer
        if (esChofer) {
            String errorChofer = validarCamposChofer(request);
            if (errorChofer != null) {
                enviarErrorYRetornar(request, response, errorChofer, tipoRegistro);
                return true;
            }
        }

        return false; // Todo correcto, sin errores
    }

    private String validarCamposVacios(HttpServletRequest request, boolean esChofer) {
        String dpi = request.getParameter("dpi");
        String nit = request.getParameter("nit");
        String telefono = request.getParameter("telefono");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        if (esVacio(dpi) || esVacio(nit) || esVacio(telefono) || esVacio(correo) || esVacio(password)) {
            return "Por favor, llene todos los campos obligatorios.";
        }

        if (esChofer) {
            String licencia = request.getParameter("numLicencia");
            String salario = request.getParameter("salarioBaseViaje");
            String fechaVenc = request.getParameter("fechaVencimientoLicencia");
            String foto = request.getParameter("foto");

            if (esVacio(licencia) || esVacio(salario) || esVacio(fechaVenc) || esVacio(foto)) {
                return "Por favor, llene todos los campos obligatorios del chofer.";
            }
        }

        return null;
    }

    private boolean esVacio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private String validarDuplicados(String dpi, String correo) throws SQLException {
        if (usuarioDAO.obtenerPorDpi(dpi) != null) {
            return "Ya existe una cuenta registrada con este número de DPI.";
        }
        if (usuarioDAO.obtenerPorCorreo(correo) != null) {
            return "Ya existe una cuenta registrada con este correo electrónico.";
        }
        return null;
    }

    private String validarCamposComunes(HttpServletRequest request) {
        String dpi = request.getParameter("dpi");
        if (!dpi.matches("\\d{13}")) {
            return "El DPI debe tener exactamente 13 dígitos.";
        }

        String nit = request.getParameter("nit");
        if (!nit.matches("\\d{8}")) {
            return "El NIT debe tener exactamente 8 dígitos.";
        }

        String telefono = request.getParameter("telefono");
        if (!telefono.matches("\\d{8}")) {
            return "El teléfono debe tener exactamente 8 dígitos, sin espacios.";
        }

        String correo = request.getParameter("correo");
        if (!correo.contains("@")) {
            return "El correo electrónico es inválido.";
        }

        String password = request.getParameter("password");
        if (password.length() < 6) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }

        return null;
    }

    private String validarCamposChofer(HttpServletRequest request) {
        String numLicencia = request.getParameter("numLicencia");
        if (!numLicencia.matches("\\d{14}")) {
            return "El número de licencia debe tener 14 dígitos.";
        }

        try {
            double salario = Double.parseDouble(request.getParameter("salarioBaseViaje"));
            if (salario <= 0) {
                return "El salario base debe ser mayor que 0.";
            }
        } catch (NumberFormatException e) {
            return "El formato del salario es inválido.";
        }

        try {
            Date fechaVenc = Date.valueOf(request.getParameter("fechaVencimientoLicencia"));
            if (!fechaVenc.after(new java.util.Date())) {
                return "La fecha de vencimiento de la licencia debe ser una fecha futura.";
            }
        } catch (IllegalArgumentException e) {
            return "El formato de la fecha de vencimiento es inválido debe ser (MMMM/mm/dd).";
        }

        return null;
    }

    private void enviarErrorYRetornar(HttpServletRequest request, HttpServletResponse response, String mensaje, String tipoRegistro)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        switch (tipoRegistro) {
            case "cliente" ->
                request.getRequestDispatcher("registro.jsp").forward(request, response);
            case "admin_sucursal" ->
                request.getRequestDispatcher("crearAdmiSucursal.jsp").forward(request, response);
            default ->
                request.getRequestDispatcher("crearChofer.jsp").forward(request, response);
        }
    }

}
