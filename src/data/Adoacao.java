package data;

import java.util.Date;

public class Adoacao {

    private String id;
    private Date dataAdoacao;
    private Adotante adotante;
    private Animal animal;
    private Boolean devolucao;
    private Date dataDevolucao;
    private String motivoDevolucao;
    private String responsavel;
    private String comprovante;
    private String observacoes;

    public Adoacao(
            String id,
            Date dataAdoacao,
            Adotante adotante,
            Animal animal,
            Boolean devolucao,
            Date dataDevolucao,
            String motivoDevolucao,
            String responsavel,
            String comprovante,
            String observacoes) {

        this.id = id;
        this.dataAdoacao = dataAdoacao;
        this.adotante = adotante;
        this.animal = animal;
        this.devolucao = devolucao;
        this.dataDevolucao = dataDevolucao;
        this.motivoDevolucao = motivoDevolucao;
        this.responsavel = responsavel;
        this.comprovante = comprovante;
        this.observacoes = observacoes;
    }

    public Adoacao(
            Date dataAdoacao,
            Adotante adotante,
            Animal animal,
            Boolean devolucao,
            Date dataDevolucao,
            String motivoDevolucao) {

        this(
            "",
            dataAdoacao,
            adotante,
            animal,
            devolucao,
            dataDevolucao,
            motivoDevolucao,
            "Ana Costa",
            "",
            ""
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDataAdoacao() {
        return dataAdoacao;
    }

    public void setDataAdoacao(Date dataAdoacao) {
        this.dataAdoacao = dataAdoacao;
    }

    public Adotante getAdotante() {
        return adotante;
    }

    public void setAdotante(Adotante adotante) {
        this.adotante = adotante;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Boolean getDevolucao() {
        return devolucao;
    }

    public void setDevolucao(Boolean devolucao) {
        this.devolucao = devolucao;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public String getMotivoDevolucao() {
        return motivoDevolucao;
    }

    public void setMotivoDevolucao(String motivoDevolucao) {
        this.motivoDevolucao = motivoDevolucao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getComprovante() {
        return comprovante;
    }

    public void setComprovante(String comprovante) {
        this.comprovante = comprovante;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return (animal != null ? animal.getNome() : "Sem Animal") + " - " + (adotante != null ? adotante.getNome() : "Sem Adotante");
    }
}