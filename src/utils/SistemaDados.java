package utils;

import data.Animal;
import data.Adotante;
import data.Adoacao;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SistemaDados {

    public static List<Animal> animais = new ArrayList<>();
    public static List<Adotante> adotantes = new ArrayList<>();
    public static List<Adoacao> adocoes = new ArrayList<>();

    private static final Type ANIMAL_LIST_TYPE = new TypeToken<List<Animal>>(){}.getType();
    private static final Type ADOTANTE_LIST_TYPE = new TypeToken<List<Adotante>>(){}.getType();
    private static final Type ADOCOES_LIST_TYPE = new TypeToken<List<Adoacao>>(){}.getType();

    public static void load() {
        animais = JsonUtil.carregar("animais.json", ANIMAL_LIST_TYPE);
        adotantes = JsonUtil.carregar("adotantes.json", ADOTANTE_LIST_TYPE);
        adocoes = JsonUtil.carregar("adocoes.json", ADOCOES_LIST_TYPE);

        if (animais.isEmpty() && adotantes.isEmpty() && adocoes.isEmpty()) {
            inicializarDadosPadrao();
            save();
        } else {
            resolveReferences();
        }
    }

    public static void save() {
        JsonUtil.salvar("animais.json", animais);
        JsonUtil.salvar("adotantes.json", adotantes);
        JsonUtil.salvar("adocoes.json", adocoes);
    }

    private static void resolveReferences() {
        for (Adoacao adocao : adocoes) {

            // Resolvendo referência ao Adotante
            if (adocao.getAdotante() != null) {
                String cpf = adocao.getAdotante().getCpf();

                for (Adotante ad : adotantes) {

                    if (ad.getCpf() != null && ad.getCpf().equals(cpf)) {
                        adocao.setAdotante(ad);
                        break;
                    }
                }
            }
            // Resolvendo referência ao Animal
            if (adocao.getAnimal() != null) {
                String nome = adocao.getAnimal().getNome();
                for (Animal an : animais) {
                    if (an.getNome() != null && an.getNome().equals(nome)) {
                        adocao.setAnimal(an);
                        break;
                    }
                }
            }
        }
    }

    private static void inicializarDadosPadrao() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
        java.text.SimpleDateFormat sdfSimple = new java.text.SimpleDateFormat("dd/MM/yyyy");
        try {
            // Animais
            Animal a1 = new Animal("Mel", sdfSimple.parse("10/01/2024"), "Cachorro", "Labrador", "Médio", true, true);
            Animal a2 = new Animal("Luna", sdfSimple.parse("15/02/2024"), "Gato", "SRD", "Pequeno", true, false);
            Animal a3 = new Animal("Thor", sdfSimple.parse("20/03/2024"), "Cachorro", "SRD", "Grande", false, true);
            Animal a4 = new Animal("Nina", sdfSimple.parse("25/04/2024"), "Gato", "SRD", "Pequeno", true, true);
            Animal a5 = new Animal("Bidu", sdfSimple.parse("30/05/2024"), "Cachorro", "SRD", "Pequeno", true, false);
            animais.add(a1);
            animais.add(a2);
            animais.add(a3);
            animais.add(a4);
            animais.add(a5);

            // Adotantes
            Adotante ad1 = new Adotante("Maria Oliveira", sdfSimple.parse("05/05/1990"), "123.456.789-00", "(65) 99999-1111", "maria@email.com", "78000-000");
            Adotante ad2 = new Adotante("João Pereira", sdfSimple.parse("12/12/1985"), "987.654.321-00", "(11) 98888-2222", "joao@email.com", "01000-000");
            Adotante ad3 = new Adotante("Fernanda Souza", sdfSimple.parse("22/10/1993"), "456.789.123-00", "(11) 97777-3333", "fernanda@email.com", "02000-000");
            Adotante ad4 = new Adotante("Carlos Lima", sdfSimple.parse("18/07/1988"), "321.654.987-00", "(11) 96666-4444", "carlos@email.com", "03000-000");
            Adotante ad5 = new Adotante("Ana Paula Souza", sdfSimple.parse("09/03/1995"), "654.321.987-00", "(11) 95555-5555", "ana@email.com", "04000-000");
            adotantes.add(ad1);
            adotantes.add(ad2);
            adotantes.add(ad3);
            adotantes.add(ad4);
            adotantes.add(ad5);

            // Adoções
            Adoacao adocao1 = new Adoacao("ADO-0001", sdf.parse("24/05/2024 10:15"), ad1, a1, false, null, null, "Ana Costa", "comprovante_ADO-0001.pdf", "Adoção bem-sucedida");
            Adoacao adocao2 = new Adoacao("ADO-0002", sdf.parse("23/05/2024 15:30"), ad2, a2, true, sdfSimple.parse("01/06/2024"), "Adaptação difícil", "Carlos Lima", "comprovante_ADO-0002.pdf", "O gato estranhou o ambiente");
            Adoacao adocao3 = new Adoacao("ADO-0003", sdf.parse("22/05/2024 09:45"), ad3, a3, false, null, null, "Ana Costa", "comprovante_ADO-0003.pdf", "Adoção bem-sucedida");
            adocoes.add(adocao1);
            adocoes.add(adocao2);
            adocoes.add(adocao3);
            
            // Vincular
            resolveReferences();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ANIMAIS DISPONIVEIS -------------------

        public static int getQuantidadeAnimaisDisponiveis() {

    int quantidade = 0;

    for (Animal a : animais) {

        if (!a.getAdotado()) {
            quantidade++;
        }
    }

    return quantidade;
}
// DOAÇÕES NO MES---------------------

public static int getQuantidadeAdocoesMes() {

    java.util.Calendar hoje = java.util.Calendar.getInstance();

    int quantidade = 0;

    for (Adoacao a : adocoes) {

        if (a.getDataAdoacao() == null) continue;

        java.util.Calendar data = java.util.Calendar.getInstance();
        data.setTime(a.getDataAdoacao());

        if (data.get(java.util.Calendar.MONTH) == hoje.get(java.util.Calendar.MONTH)
                && data.get(java.util.Calendar.YEAR) == hoje.get(java.util.Calendar.YEAR)) {

            quantidade++;
        }
    }

    return quantidade;
}

// taxa de doações

public static int getTaxaAdocao() {

    if (animais.isEmpty()) {
        return 0;
    }

    int adotados = 0;

    for (Animal a : animais) {
        if (a.getAdotado()) {
            adotados++;
        }
    }

    return (adotados * 100) / animais.size();
}

}