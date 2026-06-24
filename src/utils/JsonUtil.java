package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("dd/MM/yyyy HH:mm")
            .setPrettyPrinting()
            .create();

    private static final String FOLDER = "db";

    public static <T> void salvar(String nomeArquivo, List<T> dados) {
        File dir = new File(FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, nomeArquivo);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
            gson.toJson(dados, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> carregar(String nomeArquivo, Type tipo) {
        File file = new File(FOLDER, nomeArquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8")) {
            List<T> resultado = gson.fromJson(reader, tipo);
            return resultado != null ? resultado : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}