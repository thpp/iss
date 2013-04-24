/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.jsfuntil;

import br.com.nfse.dao.DAOUsuario;
import br.com.nfse.to.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.sql.*;
import java.util.Arrays;
import java.security.SecureRandom;

/**
 *
 * @author Thiago Henrique
 */
public class UsuarioSalt implements java.io.Serializable {

    private final static int ITERATION_NUMBER = 1000;

    public Usuario autenticaUsuario(String login, String password) throws SQLException, NoSuchAlgorithmException {

        // VALIDAÇÃO DE ENTRADA
        if (login == null || password == null) {
            // ATAQUE TEMPO RESISTENTE
            // Computation time is equal to the time needed by a legitimate user
            login = "";
            password = "";
        }
        Usuario usuario = null;
        DAOUsuario daoUsuario = new DAOUsuario();
        usuario = daoUsuario.buscaPorCnpj(login);

        if (autentica(usuario, password)) {
            return usuario;
        } else {
            usuario = null;
            return usuario;
        }
    }

    /**
     * Autentica o usuário com um login e senha fornecidos
     * Se a senha e / ou login é nulo, sempre retorna false.
     * Se o usuário não existe no banco de dados retorna false.
     * @ Param Connection con Uma conexão aberta para um databse
     * @ Param string de login login O do usuário
     * @ Param String senha A senha do usuário
     * @ Return Boolean Retorna true se o usuário é autenticado, false caso contrário
     * @ Throws SQLException Se o banco de dados é inconsistente ou indisponíveis (
     * (Dois usuários com o mesmo login, sal ou senha digerida alterado etc)
     * @ Throws NoSuchAlgorithmException Se o algoritmo SHA-1 não é suportado pela JVM
     */
    public boolean autentica(Usuario usuario, String password)
            throws SQLException, NoSuchAlgorithmException {

        try {
            boolean userExist = true;
            // VALIDAÇÃO DE ENTRADA
            if (usuario == null || password == null) {
                // ATAQUE TEMPO RESISTENTE
                // Computation time is equal to the time needed by a legitimate user
                userExist = false;
                usuario = null;
                password = "";
            }

            String digest, salt;

            if (usuario != null) {
                digest = usuario.getSenha();
                salt = usuario.getSalt();
                // VALIDAÇÃO BASE DE DADOS
                if (digest == null || salt == null) {
                    throw new SQLException("Database inconsistant Salt or Digested Password altered");
                }

            } else { // TIME RESISTANT ATTACK (Even if the user does not exist the
                // Computation time is equal to the time needed for a legitimate user
                digest = "000000000000000000000000000=";
                salt = "00000000000=";
                userExist = false;
            }

            byte[] bDigest = base64ToByte(digest);
            byte[] bSalt = base64ToByte(salt);

            // Compute the new DIGEST
            byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);


            return Arrays.equals(proposedDigest, bDigest) && userExist;
        } catch (IOException ex) {
            throw new SQLException("Database inconsistant Salt or Digested Password altered");
        } finally {
            //close(rs);
            //close(ps);
        }
    }

    /**
     * Insere um novo usuário no banco de dados
     * @ Param string de login login O do usuário
     * @ Param String senha A senha do usuário
     * @ Return Boolean Retorna true se o login ea senha são ok (não nula e comprimento (login) <= 100
     * @ Throws SQLException Se o banco não está disponível
     * @ Throws NoSuchAlgorithmException Se o algoritmo SHA-1 ou o SecureRandom não é suportado pela JVM
     */
    public Usuario criarLoginSenha(Usuario usuario) throws NoSuchAlgorithmException, UnsupportedEncodingException {


        if (usuario.getLogin() != null && usuario.getSenha() != null && usuario.getLogin().length() <= 100) {
            // Usa um Aleatório seguro não aleatória simples
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // Salt generation 64 bits long
            byte[] bSalt = new byte[8];
            random.nextBytes(bSalt);
            // Digest computation
            byte[] bDigest = getHash(ITERATION_NUMBER, usuario.getSenha(), bSalt);
            String sDigest = byteToBase64(bDigest); //password
            String sSalt = byteToBase64(bSalt); // salt

            usuario.setSenha(sDigest);
            usuario.setSalt(sSalt);

            return usuario;

        }
        return null;
    }

    /**
     * A partir de uma senha, um número de iterações e um salt,
     * Retorna o resumo correspondente
     * @ Param int iterationNb O número de iterações do algoritmo
     * @ Param String senha A senha para criptografar
     * @ Param byte sal [] O sal
     * @ Byte de retorno [] A senha digeridos
     * @ Throws NoSuchAlgorithmException Se o algoritmo não existe
     */
    public byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++) {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }

    /**
     * A partir de uma representação de 64 base, retorna o byte correspondente []
     * @ Param string de dados A representação base64
     * @ Byte de retorno []
     * @ Throws IOException
     */
    public static byte[] base64ToByte(String data) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(data);
    }

    /**
     * A partir de um byte [] retorna uma representação de base 64
     * @ Param byte data []
     * @ Return string
     * @ Throws IOException
     */
    public static String byteToBase64(byte[] data) {
        BASE64Encoder endecoder = new BASE64Encoder();
        return endecoder.encode(data);
    }
}
