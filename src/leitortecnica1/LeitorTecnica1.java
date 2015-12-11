/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leitortecnica1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LeitorTecnica1 {

    public List<String> filePaths = new ArrayList<>();

    public static void main(String[] args) {
        List<StringBuilder> geracoes = new ArrayList<>();
        LeitorTecnica1 lt1 = new LeitorTecnica1();
        String diretorioAtual = System.getProperty("user.dir");
        lt1.fileTreePrinter(new File(diretorioAtual), 0);

        for (String filePath : lt1.filePaths) {
            try {
                lt1.leDados(filePath, geracoes);
            } catch (IOException ex) {
                Logger.getLogger(LeitorTecnica1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        int index;
        lt1.deletaArquivoExistente("tec1");
        for (StringBuilder sb : geracoes) {
            index = sb.lastIndexOf(",");
            sb.replace(index, index + 1, "\n");
            try {
                lt1.imprimeArquivo("tec1" + ".csv", sb.toString());
            } catch (IOException ex) {
                Logger.getLogger(LeitorTecnica1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void leDados(String filePath, List<StringBuilder> geracoes) throws FileNotFoundException, IOException {
        List<StringBuilder> listaGeracoes = geracoes;
        String linhaLida;
        int numGeracao = -1;

        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linhaLida = br.readLine();
                if (linhaLida.contains("Geracao")) {
                    numGeracao++;
                    atualizaListaGeracoes(listaGeracoes, numGeracao, br);
                }
            }
            br.close();
            fr.close();
        }
    }

    public void atualizaListaGeracoes(List<StringBuilder> listaGeracoes, int numGeracao, final BufferedReader br) throws IOException {
        StringBuilder sb;
        try {
            sb = listaGeracoes.get(numGeracao);
        } catch (IndexOutOfBoundsException e) {
            sb = new StringBuilder();
            listaGeracoes.add(sb);
        }
        String linhaLida = br.readLine();
        linhaLida = formataStringDados(linhaLida);
        sb.append(linhaLida).append(",");
    }

    public String formataStringDados(String linhaLida) {
        linhaLida = linhaLida.replaceAll("[a-zA-Z:\\s]", "");
        String[] dados = linhaLida.split("-");
        linhaLida = dados[0] + "," + dados[4];
        return linhaLida;
    }

    private void deletaArquivoExistente(String newFile) {
        File arquivo = new File(newFile + ".csv");
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    private void imprimeArquivo(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(texto);
            bw.close();
            fw.close();
        }
    }

    public List<String> fileTreePrinter(File initialPath, int initialDepth) {
        int depth = initialDepth++;
        if (initialPath.exists()) {
            File[] contents = initialPath.listFiles();
            for (File content : contents) {
                if (content.isDirectory()) {
                    fileTreePrinter(content, initialDepth + 1);
                } else {
                    if (content.getName().contains("BRST")) {
                        filePaths.add(content.toString());
                    }
                }
            }
        }
        return filePaths;
    }
}
