package org.diegogocode.junitapp.ejemplo.models;

import lombok.*;
import org.diegogocode.junitapp.ejemplo.exception.DineroInsuficienteException;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Cuenta {
    private String persona;
    private BigDecimal saldo;

    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto){
       BigDecimal nuevoSaldo = this.saldo.subtract(monto);
       if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new DineroInsuficienteException("Dinero Insuficiente");
       }
       this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Cuenta)) return false;
        Cuenta c = (Cuenta) obj;
        if(this.persona == null || this.saldo == null) return false;
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }
}
