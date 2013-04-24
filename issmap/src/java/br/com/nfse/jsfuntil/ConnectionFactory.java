/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.nfse.jsfuntil;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Thiago Henrique
 */
public class ConnectionFactory {

    public static Connection getConnection() {

            String login;
            String senha;
            
            final String driver = "org.postgresql.Driver";
            final String local = "jdbc:postgresql://localhost:5432/nfse";

            login = "postgres";
            senha = "root";

        Connection conexao = null;

            try {
                    Class.forName(driver);
                    conexao = DriverManager.getConnection(local, login, senha);
                   
            } catch (ClassNotFoundException e) {
                    System.out.println("Não encontrado "+e);
            } catch (Exception e) {
                    System.out.print("Erro durante a conexão "+e);
            }
        
        return conexao;
    }
}
