/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.to.Logradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Thiago Henrique
 */
public class LogradouroDao {

    private Connection conn;

    public LogradouroDao() {
        this.conn = ConnectionFactory.getConnection();
    }

    public void insereLogradouro(Logradouro logradouro) throws Exception {

		try {
			String sql = "Insert into LOGRADOUROS (CD_LOGRADOURO,CD_BAIRRO,"
                                + "CD_TIPO_LOGRADOUROS,DS_LOGRADOURO_NOME,NO_LOGRADOURO_CEP) values (?,?,?,?,?)";

			PreparedStatement comando = conn.prepareStatement(sql);

                        comando.setInt(1,logradouro.getCodigo());
			comando.setInt(2,logradouro.getBairro().getCodigo());
                        comando.setInt(3,logradouro.getCdTipoLogradouro());
			comando.setString(4, logradouro.getNome());
                        comando.setString(5, logradouro.getCep());

			comando.execute();
                        
		} catch (Exception e) {
			try {
				conn.rollback();
				throw new Exception("Erro ao salvar o logradouro." +e);
			} catch (Exception e1) {
				throw new Exception("Ocorreu um erro de banco de dados." +e1);
			}
		}

    }
}
