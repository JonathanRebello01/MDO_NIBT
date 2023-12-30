package com.example.mdo_nibt;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {

    public static String dataAtual() {
        // Obtém a data atual
        LocalDate dataAtual = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dataAtual = LocalDate.now();
        }

        // Define o formato desejado para a string da data
        DateTimeFormatter formato = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formato = DateTimeFormatter.ofPattern("ddMMyyyy");
        }

        // Converte a data para string usando o formato definido
        String dataAtualString = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dataAtualString = dataAtual.format(formato);
        }

        // Imprime a data atual em string
        return dataAtualString;
    }
}
