package br.com.sicot.scheduler;

import br.com.sicot.data.EstoqueListProducer;
import br.com.sicot.model.Produto;
import br.com.sicot.util.Resources;

import javax.ejb.*;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by thiago on 26/12/15.
 */
@Stateless
@Remote
@Startup
public class ProdutoSchedulerImpl implements ProdutoScheduler {

    @Inject
    private Resources resources;

    @Inject
    private EstoqueListProducer estoqueListProducer;

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    @Schedule(hour = "*/1")
    public void calcularMediaValorProduto(){

        log.info(":::::::::::::Iniciando Scheduler de calculo de média de preços:::::::::::::::::");

        List<Produto> produtos = resources.findAll(Produto.class);

        for (Produto produto : produtos) {

            Double precoMedio = (Double) estoqueListProducer.avgProdutoEstoque(produto);

            if (precoMedio!=null){
                produto.setPrecoMedio(new BigDecimal(precoMedio.doubleValue()));

                resources.insert(produto);

                log.info("::Atualizando média do produto: "+produto.getNome()+" para " + precoMedio);
            }
        }

        log.info(":::::::::::::Terminado Scheduler de calculo de média de preços:::::::::::::::::");
    }

}
