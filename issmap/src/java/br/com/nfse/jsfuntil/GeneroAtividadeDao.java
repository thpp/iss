/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.to.GeneroAtividade;
import br.com.nfse.to.Logradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Thiago Henrique
 */
public class GeneroAtividadeDao {

    private Connection conn;

    public GeneroAtividadeDao() {
        this.conn = ConnectionFactory.getConnection();
    }

    public void insereGeneroAtividade(GeneroAtividade genAtividade) throws Exception {

		try {
			String sql = "Insert into genero_atividade (ID,NOME) values (?,?)";

			PreparedStatement comando = conn.prepareStatement(sql);

                        comando.setInt(1,genAtividade.getId());
			comando.setString(2,genAtividade.getNome());
                        

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
