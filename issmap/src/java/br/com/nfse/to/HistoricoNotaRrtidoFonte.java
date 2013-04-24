/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.nfse.to;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thiago
 */
public class HistoricoNotaRrtidoFonte {

    List<ItemHistoricoNotaRetidoFonte> itens;
    private final String NOVA_LINHA = "\n";

    public HistoricoNotaRrtidoFonte() {
        super();
        itens = new ArrayList<ItemHistoricoNotaRetidoFonte>();
    }

    public void add(ItemHistoricoNotaRetidoFonte item) {
        itens.add(item);
    }

    public void add(String data, String descricao, String valor, String numeroNota, String aliquota, String cidade, String valorIss) {
        add(new ItemHistoricoNotaRetidoFonte(data, descricao, valor, numeroNota, aliquota, cidade, valorIss));
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
    public String getDetalhamentoAliquota() {
        return getDetalhamento("ALIQUOTA", NOVA_LINHA);
    }
    public String getDetalhamentoCidade() {
        return getDetalhamento("CIDADE", NOVA_LINHA);
    }
    public String getDetalhamentoValorIss() {
        return getDetalhamento("VALORISS", NOVA_LINHA);
    }

    private String getDetalhamento(String campo, String separador) {
        StringBuilder sb = new StringBuilder();
        for (ItemHistoricoNotaRetidoFonte item : itens) {
            if (campo.equals("DATA")) {
                sb.append(item.data + separador);
            } else if (campo.equals("DESCRICAO")) {
                sb.append(item.descricao + separador);
            } else if (campo.equals("VALOR")) {
                sb.append(item.valor + separador);
            }else if(campo.equals("NUMERONOTA")){
                sb.append(item.numeroNota + separador);
            }else if(campo.equals("ALIQUOTA")){
                sb.append(item.aliquota + separador);
            }else if(campo.equals("CIDADE")){
                sb.append(item.cidade + separador);
            }else if(campo.equals("VALORISS")){
                sb.append(item.valorIss + separador);
            }
        }
        return sb.toString();

    }
}
