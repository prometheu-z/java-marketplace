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

    public void iniciarAutomacao(){

        Runnable tarefa = () -> {
            try {
                int turno = ciclo%3;

                switch (turno){
                    case 0:
                        botService.criarVendedor();
                        break;
                    case 1:

                        botService.criarProduto();
                        break;
                    case 2:
                        botService.criarCliente();
                        break;
                }

                botService.fazerCompra();
                botService.finalizarCompra();

                ciclo++;
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        };

        agendador.scheduleWithFixedDelay(tarefa, 5, 20, TimeUnit.SECONDS);
    }

    public void pararAutomacao() {
        agendador.shutdown();
    }
}
