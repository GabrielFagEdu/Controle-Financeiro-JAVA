import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class Main {
    private static void salvarJSON(JSONObject json, String nomeArquivo) {
        try (FileWriter file = new FileWriter(nomeArquivo)) {
            file.write(json.toString(2));
            System.out.println("JSON salvo com sucesso em: " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double investimento = 0;
        List<JSONObject> registros = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println("***** SEU CONTROLE DE GASTOS E SALDOS ********");
        System.out.println("***** DIGITE O NOME DO CLIENTE ********");
        String nome = scanner.nextLine();
        System.out.println("Digite o seu Salário:");
        double salario = scanner.nextDouble();

        if(nome.isEmpty() || Double.isNaN(salario) || salario == 0){
            System.out.println("Preencha todos os campos");
            return;
        }

        System.out.println("Cliente: " + nome);
        System.out.println("Salário: " + salario);

        // Criar arquivo inicial
        JSONObject dadosIniciais = new JSONObject();
        dadosIniciais.put("cliente", nome);
        dadosIniciais.put("saldoInicial", salario);
        dadosIniciais.put("registros", new JSONArray());
        salvarJSON(dadosIniciais, "controle_financeiro.json");

        System.out.println("agora você irá digitar um número de 1 a 4 \n para as seguintes operações:\n" +
                "1: Consultar seu saldo \n 2: Registrar uma despesa \n 3: Registrar um investimento \n 4: Registrar uma entrada \n 0: Sair");
        Integer opcao = scanner.nextInt();

        while (opcao != 0){
            LocalDateTime dataHora = LocalDateTime.now();
            JSONObject registro = new JSONObject();
            boolean operacaoRealizada = false;

            if (opcao == 1){
                System.out.println("Saldo atual: " + salario);
            }
            else if (opcao == 2){
                System.out.println("Digite o valor da despesa: ");
                double despesa = scanner.nextDouble();
                salario = salario - despesa;
                System.out.println("Saldo atual: " + salario);

                registro.put("categoria", "DESPESA");
                registro.put("valor", despesa);
                registro.put("data", dataHora.format(formatter));
                registros.add(registro);
                operacaoRealizada = true;
            }
            else if(opcao == 3){
                System.out.println("Digite o valor do Investimento: ");
                investimento = scanner.nextDouble();
                salario = salario - investimento;
                System.out.println("Saldo de Investimentos Atual: " + investimento);

                registro.put("categoria", "INVESTIMENTO");
                registro.put("valor", investimento);
                registro.put("data", dataHora.format(formatter));
                registros.add(registro);
                operacaoRealizada = true;
            }
            else if(opcao == 4){
                System.out.println("Digite o valor da entrada: ");
                double entrada = scanner.nextDouble();
                salario = salario + entrada;
                System.out.println("Saldo atual: " + salario);

                registro.put("categoria", "ENTRADA");
                registro.put("valor", entrada);
                registro.put("data", dataHora.format(formatter));
                registros.add(registro);
                operacaoRealizada = true;
            }

            if (operacaoRealizada) {
                JSONObject resultadoAtual = new JSONObject();
                resultadoAtual.put("cliente", nome);
                resultadoAtual.put("saldoFinal", salario);
                resultadoAtual.put("investimentoTotal", investimento);
                resultadoAtual.put("registros", new JSONArray(registros));

                // Salva o JSON após cada operação
                salvarJSON(resultadoAtual, "controle_financeiro.json");
            }

            System.out.println("\nDigite uma nova opção (1 a 4), \n para as seguintes operações:\n" +
                    "1: Consultar seu saldo \n 2: Registrar uma despesa \n 3: Registrar um investimento \n 4: Registrar uma entrada \n 0: Sair");
            opcao = scanner.nextInt();

            if (opcao == 0) {
                JSONObject resultadoFinal = new JSONObject();
                resultadoFinal.put("cliente", nome);
                resultadoFinal.put("saldoFinal", salario);
                resultadoFinal.put("investimentoTotal", investimento);
                resultadoFinal.put("registros", new JSONArray(registros));

                // Salva o JSON final
                salvarJSON(resultadoFinal, "controle_financeiro_final.json");

                System.out.println("\n=== RESUMO FINAL DAS OPERAÇÕES ===");
                System.out.println(resultadoFinal.toString(2));
                System.out.println("================================");
            }
        }
    }
}