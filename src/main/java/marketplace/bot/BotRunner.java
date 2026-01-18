package marketplace.bot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotRunner {
    private final ScheduledExecutorService agendador = Executors.newScheduledThreadPool(1);
    private final GerarBot botService;
    private int ciclo = 0;

    public BotRunner() {
        this.botService = new GerarBot();
    }

    public void iniciarAutomacao(int n){

        Runnable tarefa = () -> {
            try {
                if (n == 1) {
                    if (ciclo == 0) {
                        botService.criarVendedor();
                    }
                    if (ciclo < 5) {
                        botService.criarProduto();
                    }
                    if (ciclo == 2) {
                        botService.criarCliente();
                    }
                }

                if(ciclo >= 2 ) {
                    botService.fazerCompra(4);
                    botService.finalizarCompra(3);
                }

                ciclo++;
            }catch (Exception e){
                System.out.println(e.getMessage());
                ciclo = 6;
            }
        };

        agendador.scheduleWithFixedDelay(tarefa, 5, 20, TimeUnit.SECONDS);
    }

    public void pararAutomacao() {
        agendador.shutdown();
    }
}
