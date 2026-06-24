package data;

import java.util.Date;

public class Animal {

    private String nome;
    private Date dataResgate;
    private String especie;
    private String raca;
    private String porte;
    private Boolean castrado;
    private Boolean adotado;
    private String caminhoImagem;

    // CONSTRUTOR COMPLETO
    public Animal(
            String nome,
            Date dataResgate,
            String especie,
            String raca,
            String porte,
            Boolean castrado,
            Boolean adotado,
            String caminhoImagem
    ) {

        this.nome = nome;
        this.dataResgate = dataResgate;
        this.especie = especie;
        this.raca = raca;
        this.porte = porte;
        this.castrado = castrado;
        this.adotado = adotado;
        this.caminhoImagem = caminhoImagem;
    }

    // CONSTRUTOR SEM IMAGEM
    public Animal(
            String nome,
            Date dataResgate,
            String especie,
            String raca,
            String porte,
            Boolean castrado,
            Boolean adotado
    ) {

        this(
            nome,
            dataResgate,
            especie,
            raca,
            porte,
            castrado,
            adotado,
            null
        );
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataResgate() {
        return dataResgate;
    }

    public void setDataResgate(Date dataResgate) {
        this.dataResgate = dataResgate;
    }

    public String geEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    public Boolean getCastrado() {
        return castrado;
    }

    // CORRIGIDO
    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    public Boolean getAdotado() {
        return adotado;
    }

    public void setaAotado(Boolean adotado) {
        this.adotado = adotado;
    }

    public void setAdotado(Boolean adotado) {
        this.adotado = adotado;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }
    
}