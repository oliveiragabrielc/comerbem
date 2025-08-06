package br.com.fiap.comerbem.model;

import java.time.LocalTime;

import jakarta.persistence.Embeddable;

@Embeddable
public class HorarioFuncionamento {

    LocalTime abertura;
    LocalTime fechamento;

    public HorarioFuncionamento() {
    }

    public HorarioFuncionamento(LocalTime abertura, LocalTime fechamento) {
        this.abertura = abertura;
        this.fechamento = fechamento;
    }

    public LocalTime getAbertura() {
        return abertura;
    }

    public void setAbertura(LocalTime abertura) {
        this.abertura = abertura;
    }

    public LocalTime getFechamento() {
        return fechamento;
    }

    public void setFechamento(LocalTime fechamento) {
        this.fechamento = fechamento;
    }

}
