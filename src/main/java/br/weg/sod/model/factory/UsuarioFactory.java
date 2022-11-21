package br.weg.sod.model.factory;

import br.weg.sod.model.entities.*;

public class UsuarioFactory {

    public Usuario getUsuario(Integer tipo) {
        switch (tipo) {
            case 1 -> {
                return new Solicitante();
            }
            case 2 -> {
                return new AnalistaTI();
            }
            case 3 -> {
                return new GerenteNegocio();
            }
            case 4 -> {
                return new GerenteTI();
            }
        }
        return null;
    }

}
