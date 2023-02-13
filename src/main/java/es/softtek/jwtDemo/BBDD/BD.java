package es.softtek.jwtDemo.BBDD;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class BD {
    static {
        try {
            DriverManager.registerDriver(new Driver() {
                @Override
                public Connection connect(String url, Properties info) throws SQLException {
                    return null;
                }

                @Override
                public boolean acceptsURL(String url) throws SQLException {
                    return false;
                }

                @Override
                public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
                    return new DriverPropertyInfo[0];
                }

                @Override
                public int getMajorVersion() {
                    return 0;
                }

                @Override
                public int getMinorVersion() {
                    return 0;
                }

                @Override
                public boolean jdbcCompliant() {
                    return false;
                }

                @Override
                public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                    return null;
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection abrirConexion() throws SQLException {
        Connection cnn = DriverManager.getConnection(
                "jdbc:mysql://localhost/jwt?useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root", // user
                "pabloygala96");// password
        return cnn;
    }

    public static boolean comprobarLogin(String user, String pass)throws SQLException{
        Connection cnn = BD.abrirConexion();
        PreparedStatement st = cnn.prepareStatement("SELECT * FROM users WHERE nombre = ? AND pwd = ?");
        st.setString(1, user);
        st.setString(2, pass);
        ResultSet rs = st.executeQuery();
        if(rs.next()){
            return true;
        }else{
            return false;
        }
    }

}
