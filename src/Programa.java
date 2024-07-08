import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Programa {
    public static void main(String[] args) throws Exception {
        String arquivoCSV = "C:\\Users\\autologon\\Desktop\\ProvaJava\\Prova\\alunos.csv"; //precisa mudar o caminho da pasta porque ela é diferente em cada Pc
        String arquivoResumo = "C:\\Users\\autologon\\Desktop\\ProvaJava\\Prova\\resumo.csv"; //precisa mudar o caminho da pasta porque ela é diferente em cada Pc
        List<Aluno> listaDeAlunos = new ArrayList<>();

        
        int quantidadeAlunos = 0;
        int quantidadeAprovados = 0;
        int quantidadeReprovados = 0;
        double menorNota = Double.MAX_VALUE;
        double maiorNota = Double.MIN_VALUE;
        double somaNotas = 0.0;

        
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            String linha;
            int numeroLinha = 0;
            boolean primeiraLinha = true; 

            while ((linha = br.readLine()) != null) {
                numeroLinha++;

                if (primeiraLinha) {
                    primeiraLinha = false; 
                    continue; 
                }

                String[] dados = linha.split(";");

                
                if (dados.length >= 3) { 
                    String matricula = dados[0].trim(); 
                    String nome = dados[1].trim(); 
                    String notaStr = dados[2].trim(); 
                    
                  
                    if (isNumeroDecimal(notaStr)) {
                        try {
                            
                            notaStr = notaStr.replace(',', '.');
                            double nota = Double.parseDouble(notaStr); 

                           
                            quantidadeAlunos++;
                            somaNotas += nota;
                            if (nota >= 6.0) {
                                quantidadeAprovados++;
                            } else {
                                quantidadeReprovados++;
                            }
                            if (nota < menorNota) {
                                menorNota = nota;
                            }
                            if (nota > maiorNota) {
                                maiorNota = nota;
                            }

                            
                            Aluno aluno = new Aluno(matricula, nome, nota);
                            listaDeAlunos.add(aluno);

                        } catch (NumberFormatException e) {
                            System.err.println("Erro ao converter nota para double na linha " + numeroLinha + ": " + notaStr);
                            
                        }
                    } else {
                        System.err.println("Nota inválida encontrada na linha " + numeroLinha + ": " + notaStr);
                    }
                } else {
                    System.err.println("Número insuficiente de campos na linha " + numeroLinha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

       
        double mediaGeral = 0.0;
        if (quantidadeAlunos > 0) {
            mediaGeral = somaNotas / quantidadeAlunos;
        }

        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivoResumo))) {
            
            bw.write("Quantidade de Alunos;Quantidade de Aprovados;Quantidade de Reprovados;Menor Nota;Maior Nota;Média Geral");
            bw.newLine();

            
            bw.write(quantidadeAlunos + ";" + quantidadeAprovados + ";" + quantidadeReprovados + ";" +
                    menorNota + ";" + maiorNota + ";" + mediaGeral);
            bw.newLine();

            System.out.println("Arquivo de resumo gerado com sucesso: " + arquivoResumo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private static boolean isNumeroDecimal(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        boolean pontoDecimal = false;
        boolean pontoEspecial = false; 
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            
            if (c == '.' || c == ',' || c == ';') {
                if (pontoDecimal || pontoEspecial) {
                    return false; 
                }
                pontoDecimal = true;
            } else if (!Character.isDigit(c)) {
                return false; 
            }
        }
        return true;
    }
}
