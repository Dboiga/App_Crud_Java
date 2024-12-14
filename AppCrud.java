package appcrud;

import java.sql.*;
import java.util.Scanner;

public class AppCrud {

    static Connection conexion;

    /**
     * Método principal que inicia la aplicación CRUD.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //Establecer la conexion
        String url = "jdbc:postgresql://localhost:5432/laboratorio2";
        String user = "postgres";
        String pass = "12345678";

        try {
            //ESTABLECER LA CONEXION
            conexion = DriverManager.getConnection(url, user, pass);

            System.out.println("*******CRUD******");
            System.out.println("¿Qué operacion desea realizar?");
            System.out.println("-Insertar un registro(1)\n"
                    + "-Consultar un registro particular(2)\n"
                    + "-Mostrar todos los registros de la tabla prueba(3)\n"
                    + "-Actualizar un registro(4)\n"
                    + "-Eliminar un registro(5)\n"
                    + "-Consultar la tabla prueba de manera ordenada(6)\n"
                    + "-Consultar tablas combinadas(7)");
            System.out.println("Ingrese la opcion: ");
            int opcion = sc.nextInt();
            switch (opcion) {
                case (1) -> {
                    System.out.println("El resgitro será insertado en la tabla 'prueba'");
                    System.out.print("Ingrese el id: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("");
                    System.out.print("Ingrese el nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("");
                    System.out.print("Ingrese el estado: ");
                    String estado = sc.next();
                    System.out.println("");
                    System.out.print("Ingrese el area: ");
                    String area = sc.next();
                    System.out.println("");
                    insertarRegistro(id, nombre, estado, area);
                }
                case (2) -> {
                    System.out.println("La consulta se hará en la tabla 'prueba'");
                    System.out.println("Ingrese el id del registro: ");
                    int id_cubiculo = sc.nextInt();
                    buscarPorId(id_cubiculo);
                }
                case (3) -> {
                    System.out.println("*Solo se muestran los primeros 7*");
                    traerTodo();
                }
                case (4) -> {
                    System.out.print("Ingrese el ID del registro que desea modificar: ");
                    int cubiculo_id = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Ingrese el nombre nuevo: ");
                    String newNombre = sc.nextLine();
                    System.out.println("Ingrese el estado nuevo: ");
                    String newEstado = sc.nextLine();
                    System.out.println("Ingrese el nuevo area: ");
                    String newArea = sc.nextLine();
                    actualizarRegistro(cubiculo_id, newNombre, newEstado, newArea);
                }
                case (5) -> {
                    System.out.println("Ingrese el id del registro que desea eliminar: ");
                    int codigo = sc.nextInt();
                    eliminarRegistro(codigo);
                }
                case (6) ->
                    consultarEnOrdenAscendente();
                case (7) ->
                    consultaInnerJoin();
                default ->
                    System.out.println("Opción incorrecta");
            }

            conexion.close();

        } catch (SQLException ex) {
            System.out.println(ex.getLocalizedMessage());
        }

    }

    private static void insertarRegistro(int id, String nombre, String estado, String area) throws SQLException {
        // Preparar la sentencia SQL para insertar un registro
        PreparedStatement sentencia = conexion.prepareStatement("INSERT INTO prueba VALUES (?,?,?,?)");

        // Establecer los parámetros de la sentencia
        sentencia.setInt(1, id);
        sentencia.setString(2, nombre);
        sentencia.setString(3, estado.toUpperCase());
        sentencia.setString(4, area);

        // Ejecutar la sentencia
        sentencia.executeUpdate();
        sentencia.close();
    }

    /**
     * Muestra todos los registros de la tabla 'prueba', limitando a los
     * primeros 7.
     *
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static void traerTodo() throws SQLException {
        // Crear una sentencia para ejecutar la consulta
        Statement sentencia = conexion.createStatement();

        // Ejecutar la consulta y obtener los resultados
        ResultSet rs = sentencia.executeQuery("SELECT * FROM prueba LIMIT 7");

        // Recorrer los resultados y mostrar cada registro
        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String estado = rs.getString("estado");
            String area = rs.getString("area");
            System.out.println("ID: " + id + ", NOMBRE:" + nombre + ", ESTADO: " + estado + ", AREA: " + area);
        }
        sentencia.close();
    }

    /**
     * Busca un registro en la tabla 'prueba' por ID.
     *
     * @param codigo El ID del prueba a buscar.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static void buscarPorId(int codigo) throws SQLException {
        // Preparar la sentencia SQL para buscar un registro por ID
        PreparedStatement sentencia = conexion.prepareStatement("SELECT * FROM prueba WHERE id = ?");
        sentencia.setInt(1, codigo);

        // Ejecutar la consulta y obtener los resultados
        ResultSet rs = sentencia.executeQuery();

        // Recorrer los resultados y mostrar el registro encontrado
        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String estado = rs.getString("estado");
            String area = rs.getString("area");
            System.out.println("ID: " + id + ", NOMBRE:" + nombre + ", ESTADO: " + estado + ", AREA: " + area);
        }
        sentencia.close();
    }

    /**
     * Actualiza un registro en la tabla 'pruebaa' por ID.
     *
     * @param id El ID del cubículo a actualizar.
     * @param nuevoNombre El nuevo nombre del prueba.
     * @param nuevoEstado El nuevo estado del prueba.
     * @param nuevaArea El nuevo área del prueba.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static void actualizarRegistro(int id, String nuevoNombre, String nuevoEstado, String nuevaArea) throws SQLException {
        // Preparar la sentencia SQL para actualizar un registro
        PreparedStatement sentencia = conexion.prepareStatement("UPDATE prueba SET nombre = ?, estado = ?, area = ? WHERE id = ?");

        // Establecer los parámetros de la sentencia
        sentencia.setInt(4, id);
        sentencia.setString(1, nuevoNombre);
        sentencia.setString(2, nuevoEstado.toUpperCase());
        sentencia.setString(3, nuevaArea);

        // Ejecutar la sentencia
        sentencia.executeUpdate();
        sentencia.close();

    }

    /**
     * Elimina un registro en la tabla 'prueba' por ID.
     *
     * @param id El ID del prueba a eliminar.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static void eliminarRegistro(int id) throws SQLException {
        // Preparar la sentencia SQL para eliminar un registro
        PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM prueba WHERE id = ?");
        sentencia.setInt(1, id);

        // Ejecutar la sentencia
        sentencia.executeUpdate();
        sentencia.close();
    }

    /**
     * Consulta y muestra los registros de la tabla 'prueba' en orden ascendente
     * por color.
     *
     * @throws SQLException Si ocurre un error de SQL.
     */
    private static void consultarEnOrdenAscendente() throws SQLException {
        // Crear una sentencia para ejecutar la consulta
        Statement sentencia = conexion.createStatement();

        // Ejecutar la consulta y obtener los resultados
        ResultSet rs = sentencia.executeQuery("SELECT * FROM prueba ORDER BY nombre ASC");

        // Recorrer los resultados y mostrar cada registro
        while (rs.next()) {
            int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String estado = rs.getString("estado");
            String area = rs.getString("area");
            System.out.println("ID: " + id + ", NOMBRE: " + nombre + ", ESTADO: " + estado + ", AREA: " + area);
        }
        sentencia.close();
    }

    /**
     * Realiza una consulta combinada entre las tablas 'prueba1' y 'paciente'
     * usando INNER JOIN.
     *
     * @throws SQLException Si ocurre un error de SQL.
     */
     private static void consultaInnerJoin() {
    try (Statement sentencia = conexion.createStatement()) {
        ResultSet rs = sentencia.executeQuery(
            "SELECT prueba.id AS prueba_id, prueba.nombre AS prueba_nombre, prueba.area, " +
            "paciente.id AS paciente_id, paciente.nombre AS paciente_nombre, paciente.edad " +
            "FROM prueba " +
            "INNER JOIN paciente ON prueba.id = paciente.id"
        );

        while (rs.next()) {
            int pruebaId = rs.getInt("prueba_id");
            String pruebaNombre = rs.getString("prueba_nombre");
            String area = rs.getString("area");
            int pacienteId = rs.getInt("paciente_id");
            String pacienteNombre = rs.getString("paciente_nombre");
            int edad = rs.getInt("edad");

            System.out.println("PRUEBA_ID: " + pruebaId + ", PRUEBA_NOMBRE: " + pruebaNombre + ", AREA: " + area +"\n"+
                               "PACIENTE_ID: " + pacienteId + ", PACIENTE_NOMBRE: " + pacienteNombre + ", EDAD: " + edad);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


}
