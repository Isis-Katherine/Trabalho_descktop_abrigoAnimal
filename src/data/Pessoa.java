package data;

import java.util.Date;

public class Pessoa {
    private String nome;
    private Date dataNascimento;
    private String cpf;
    private String telefone;
    private String email;
    private String cep;

    public Pessoa(String nome, Date dataNascimento, String cpf, String telefone, String email, String cep) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.cep = cep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCPF() {
        return nome;
    }

    public void setCPF(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return nome + " (" + cpf + ")";
    }

    @Override
    public boolean equals(Object item) {
         if ((this.nome.equals(((Pessoa) item).getNome()) && 
             (this.dataNascimento.equals(((Pessoa) item).getDataNascimento())) &&
             (this.cpf.equals(((Pessoa) item).getCpf())))){
              return true;
         }
         return false;
    }

}

