package data;

import java.util.Date;

public class Adotante extends Pessoa {


    public Adotante(
            String nome,
            Date dataNascimento,
            String cpf,
            String telefone,
            String email,
            String cep
        ) {

        super(
            nome,
            dataNascimento,
            cpf,
            telefone,
            email,
            cep
            
        );
    }

    
    public int getQuantidadeAdocoes() {

    int quantidade = 0;

    for (Adoacao a : utils.SistemaDados.adocoes) {

        if (a.getAdotante().getCpf().equals(this.getCpf())) {
            quantidade++;
        }
    }

    return quantidade;
}
}