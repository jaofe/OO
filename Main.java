import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class Main {
    static Biblioteca biblioteca;
    static Scanner input;
    static Usuario usuario;
    static boolean isAdmin = false;

    public static void main(String[] args) {
        biblioteca = new Biblioteca();
        input = new Scanner(System.in);

        biblioteca.usuarios.add(new Admin("admin", "admin", "admin@admin.com"));

        do {
            loginMenu();

            if (usuario != null) {
                if (usuario instanceof Admin) {
                    adminMenu();
                }
                else {
                    usuarioMenu();
                }
            }
        } while (usuario != null);

        System.out.println("\n                           ATÉ A PRÓXIMA!");
        input.close();
    }

    private static void loginMenu() {
        usuario = null;
        
        loginLoop:
        while (usuario == null) {
            System.out.println("--------------------------- SEJA BEM-VINDO ---------------------------\n");
            System.out.println("                                MENU\n");
            System.out.println("1 - Logar");
            System.out.println("2 - Cadastrar Usuario");
            System.out.println("3 - Sair");
            System.out.print("Digite o que deseja fazer: ");

            String opcao = input.nextLine();
            System.out.println();

            switch (opcao) {
                case "1":
                    usuario = tentarLogar();

                    if (usuario == null)
                        System.out.println("\n\nUsuario ou senha invalidos! Tente novamente");
                    else
                        System.out.println("Usuario " + usuario.username + " logado com sucesso!");
                    break;
                case "2":
                    cadastrarUsuario();
                    break;
                case "3":
                    break loginLoop;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        }
    }

    private static void adminMenu() {
        menu: while (true) {
            System.out.println("--------------------------- SEJA BEM-VINDO ---------------------------\n");
            System.out.println("                                MENU\n");
            System.out.println("1  - Criar Usuario Administrador");
            System.out.println("2  - Cadastrar Livros");
            System.out.println("3  - Listar Livros");
            System.out.println("4  - Listar Usuarios");
            System.out.println("5  - Listar Administradores");
            System.out.println("6  - Buscar");
            System.out.println("7  - Listar Livros Alugados");
            System.out.println("8  - Listar Livros Atrasados");
            System.out.println("9  - Remover Review");
            System.out.println("10 - Sair");
            System.out.print("Digite o que deseja fazer: ");

            String opcao = input.nextLine();
            System.out.println();

            switch (opcao) {
                case "1":
                    cadastrarAdmin();
                    break;
                case "2":
                    cadastrarLivro();
                    break;
                case "3":
                    biblioteca.listarLivros();
                    break;
                case "4":
                    biblioteca.listarUsuarios();
                    break;
                case "5":
                    biblioteca.listarAdmins();
                    break;
                case "6":
                    buscarLivro();
                    break;
                case "7":
                    biblioteca.listarLivrosAlugados();
                    break;
                case "8":
                    biblioteca.listarLivrosAtrasados();
                    break;                
                case "9":
                    removerReview();
                    break;
                case "10":
                    break menu;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        }
    }

    private static void usuarioMenu() {
        menu: while (true) {
            System.out.println("--------------------------- SEJA BEM-VINDO ---------------------------\n");
            System.out.println("                                MENU\n");
            System.out.println("1 - Buscar");
            System.out.println("2 - Listar Livros");
            System.out.println("3 - Alugar Livro");
            System.out.println("4 - Devolver Livro");
            System.out.println("5 - Listar Livros Alugados");
            System.out.println("6 - Reservar Livro");
            System.out.println("7 - Multas Pendentes");
            System.out.println("8 - Escrever Review");
            System.out.println("9 - Sair");
            System.out.print("Digite o que deseja fazer: ");

            String opcao = input.nextLine();
            System.out.println();

            switch (opcao) {
                case "1":
                    buscarLivro();
                    break;
                case "2":
                    biblioteca.listarLivros();
                    break;
                case "3":
                    alugarLivro();
                    break;
                case "4":
                    devolverLivro();
                    break;
                case "5":
                    usuario.listarLivrosAlugados();
                    break;
                case "6":
                    reservar();
                    break;
                case"7":
                    olharMulta();
                    break;    
                case "8":
                    escreverReview();
                    break;
                case "9":
                    break menu;
                default:
                    System.out.println("Opcao invalida!");
                    break;
            }
        }
    }

    private static void cadastrarAdmin() {
        System.out.print("Digite o Username: ");
        String username = input.nextLine();

        if (biblioteca.buscarUsuario(username) != null) {
            System.out.println("Usuario já existente!");
            return;
        }
        System.out.print("Digite forma de contato(email/telefone): ");
        String contato = input.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = input.nextLine();

        biblioteca.criarAdmin(username, senha, contato);
    }

    private static Usuario tentarLogar() {
        System.out.print("Digite o seu usuario: ");
        String username = input.nextLine();

        System.out.print("Digite a sua senha: ");
        String senha = input.nextLine();

        for (Usuario usuario : biblioteca.usuarios) {
            if (usuario.username.equals(username)){
                if (usuario.senha.equals(senha)){
                    return usuario;
                }

                return null;
            }
        }

        for (Usuario usuario : biblioteca.usuarios) {
            if (usuario.username.equals(username)){
                if (usuario.senha.equals(senha))
                    return usuario;
                    
                return null;
            }
        }

        return null;
    }

    private static void devolverLivro() {
        System.out.print("Digite o titulo do livro: ");
        String titulo = input.nextLine();

        Livro livro = biblioteca.buscarLivro(titulo);

        if (livro == null) {
            System.out.println("Livro nao encontrado!");
            return;
        }

        if (livro.dataDevolucao != null) {
            if(livro.checkarAtraso()) {
                LocalDate hoje = LocalDate.now();
                long atraso = ChronoUnit.DAYS.between(livro.dataDevolucao, hoje);
                System.out.println("Digite (ok) para comfirmar o pagamento da multa de R$:" + (5 + atraso*.75) + "\n(R$ 5.00 + R$ 0.75 por dia de atraso.)");
                String ok = input.nextLine();
            
                if(!ok.equals("ok")) return;
            }
        }

        usuario.devolverLivro(livro,biblioteca,usuario.username);
    }

    private static void olharMulta()
    {
        int atrasado = 0;
        
        if (usuario.livrosAlugados.size() > 0) {
            
            for (Livro livro: usuario.livrosAlugados) {
                if (livro.checkarAtraso() == true) {
                    LocalDate hoje = LocalDate.now();
                    long atraso = ChronoUnit.DAYS.between(livro.dataDevolucao, hoje);
                    
                    System.out.print("Titulo:");
                    System.out.print(" "+ livro.pegarTitulo());
                    System.out.print(" multa de R$: " + (5 + atraso*.75) + " / ");
                    atrasado++;
                }
            }
        }
        if(atrasado == 0)
        {
            System.out.println("Sem multas pendentes!");
            return;    
        }
        else
        {
            System.out.println();
        }
        
    }

    private static void alugarLivro() {

        for(Livro l: usuario.livrosAlugados) {
            if(l.checkarAtraso()) {
                System.out.println("Devido a existencia de livros atrasados, esta função está indisponivel!");
                return;
            }
        }



        System.out.print("Digite o titulo do livro: ");
        String titulo = input.nextLine();

        Livro livro = biblioteca.buscarLivro(titulo);

        if (livro == null) {
            System.out.println("Livro nao encontrado!");
            return;
        }


        System.out.print("Digite a data para devolução do livro (YYYY-MM-DD):");
        String devolucao = input.nextLine();

    
        DateTimeFormatter formato = DateTimeFormatter.ISO_LOCAL_DATE;

        boolean formatoCorreto = false;
        while (!formatoCorreto) {
            try {
                LocalDate dataD = LocalDate.parse(devolucao, formato);
                livro.dataDevolucao = dataD;
                formatoCorreto = true;
            } catch (Exception e) {
                System.out.println("A Input não apresenta o formato correto, digite novamente a data no formato (YYYY-MM-DD)");
                devolucao = input.nextLine();
            }
        }

        usuario.alugarLivro(livro);
    }

    private static void cadastrarUsuario() {
        System.out.print("Digite o Username: ");
        String username = input.nextLine();

        if (biblioteca.buscarUsuario(username) != null) {
            System.out.println("Usuario já existente!");
            return;
        }

        System.out.print("Digite forma de contato(email/telefone): ");
        String contato = input.nextLine();

        System.out.print("Digite sua senha: ");
        String senha = input.nextLine();

        biblioteca.criarUsuario(username, senha, contato);
    }

    private static void cadastrarLivro() {
        System.out.println("Nome do Livro:");
        String titulo = input.nextLine();

        System.out.println("Autor do Livro:");
        String autor = input.nextLine();

        System.out.println("Editora do Livro:");
        String editora = input.nextLine();

        System.out.println("Ano de lançamento do Livro:");
        String anoLancamento = input.nextLine();

        Livro livro = new Livro(titulo, autor, editora, anoLancamento);
        
        biblioteca.cadastrarLivro(livro);
    }

    private static void buscarLivro()
    {
        System.out.print("Digite o titulo do livro: ");
        String titulo = input.nextLine();

        Livro livro = biblioteca.buscarLivro(titulo);

        if(livro == null)
            System.out.println("Livro nao encontrado!");
        
        else {
            livro.printLivro();
            if (livro.reviews.isEmpty()) {
                System.out.println("Nenhuma review.");
            } else {
                System.out.println("Reviews:");
                for(Review R: livro.reviews) {
                    R.printReview();
                }
            }
        }
    }

    private static void reservar()
    {
        System.out.print("Digite o titulo do livro: ");
        String titulo = input.nextLine();
        
        Livro livro = biblioteca.buscarLivro(titulo);

        if (livro == null) {
            System.out.println("Livro nao encontrado!");
            return;
        }

        usuario.reservar(livro);
    }

    private static void escreverReview() {
        if (usuario.livrosDevolvidos.isEmpty()) {
            System.out.println("Nenhum livro valido (devolva um livro para poder escrever uma review).");
        } else {
            int count = 1;
            for (Livro l: usuario.livrosDevolvidos) {
                System.out.println(count++ + " - " + l.titulo);
            }
            System.out.println("Digite o número do livro que quer escrever a review: ");
            int numl = input.nextInt();

            Livro selecionado = null;
            boolean formatoCorreto = false;
            while (!formatoCorreto) {
                try {
                    selecionado = usuario.livrosDevolvidos.get(numl - 1);
                    formatoCorreto = true;
                } catch (Exception e) {
                    System.out.println("A Input não apresenta o formato correto");
                    numl = input.nextInt();
                }
            }

            System.out.println("Digite sua review:");
            String rev = input.nextLine();
            rev = input.nextLine();
            selecionado.reviews.add(new Review(rev, usuario.username));
        }       
    }

    private static void removerReview() {
        System.out.println("Digite o username do usuario:");
        String username = input.nextLine();
        if(biblioteca.buscarUsuario(username) == null) {
            System.out.println("Usuario não encotrado");
            return;
        }
        System.out.print("Digite o titulo do livro: ");
        String titulo = input.nextLine();

        Livro livro = biblioteca.buscarLivro(titulo);
        if(livro == null) {
            System.out.println("Livro não encotrado");
            return;
        }
        
        int index = -1;
        for(Review r: livro.reviews) {
            if (r.reviewerUsername.equals(username)) {
                index = livro.reviews.indexOf(r);
            }
        }
        if (index == -1) {
            System.out.println("Usuario não possui review desse livro.");
            return;
        } else {
            livro.reviews.remove(index);
            System.out.println("Review removida com sucesso.");
            return;
        }
    }
}


