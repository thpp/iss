/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import br.com.nfse.jsfuntil.Moeda;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thiago
 */
public class HistoricoNota {

    List<ItemHistoricoNota> itens;
    private final String NOVA_LINHA = "\n";

    public HistoricoNota() {
        super();
        itens = new ArrayList<ItemHistoricoNota>();
    }

    public void add(ItemHistoricoNota item) {
        itens.add(item);
    }

    public void add(String data, String descricao, String valor, String numeroNota) {
        add(new ItemHistoricoNota(data, descricao, valor, numeroNota));
    }

    public String getDetalhamentoData() {
        return getDetalhamento("DATA", NOVA_LINHA);
    }

    public String getDetalhamentoDescricao() {
        return getDetalhamento("DESCRICAO", NOVA_LINHA);
    }

    public String getDetalhamentoValor() {
        return getDetalhamento("VALOR", NOVA_LINHA);
    }
    
    public String getDetalhamentoNumeroNota() {
        return getDetalhamento("NUMERONOTA", NOVA_LINHA);
    }

    private String getDetalhamento(String campo, String separador) {
        StringBuilder sb = new StringBuilder();
        for (ItemHistoricoNota item : itens) {
            if (campo.equals("DATA")) {
                sb.append(item.data + separador);
            } else if (campo.equals("DESCRICAO")) {
                sb.append(item.descricao + separador);
            } else if (campo.equals("VALOR")) {
                sb.append(item.valor + separador);
            }else if(campo.equals("NUMERONOTA")){
                sb.append(item.numeroNota + separador);
            }
        }
        return sb.toString();

    }
}
