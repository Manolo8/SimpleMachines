package com.github.manolo8.simplemachines.controller;

import com.github.manolo8.simplemachines.model.Machine;
import com.github.manolo8.simplemachines.service.MachineService;

import java.util.Random;

public class MachineController implements Runnable {

    private MachineService machineService;
    private Random random;
    private long tick;

    public MachineController(MachineService machineService, Random random) {
        this.machineService = machineService;
        this.random = random;
        this.tick = 20;
    }

    @Override
    public void run() {
        for (int i = 0; i < machineService.getMachines().size(); i++) {
            Machine machine = machineService.getMachines().get(i);

            if (machine.isWrong()) {
                //Se estiver errado, vamos colocar um bloco de ar
                //Onde fica a base e então dropar o livro
                //do projeto de construção para que o jogador
                //possa fazer outra (os itens de construção
                //serão perdidos)
                machineService.deleteMachine(machine);
                //Foi usado um iterator...
                //Logo é possível dar continue sem medo
                //Eu espero '-'
                continue;
            }

            //Verificamos se a máquia está funcionando
            //Se não está, ignoramos...
            //Iremos usar eventos para checar se ela pode
            //Voltar a funcionar
            if (!machine.isWorking()) {
                if(machine.isChanged()) {
                    machine.setChanged(false);
                    machine.canWork();
                }
                continue;
            }


            machine.tick(tick);
        }
    }
}
