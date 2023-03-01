import java.time.LocalDate;
import java.util.ArrayList;

public class Usuario {
    public String username;
    public String senha;
    public String contato;
  
    public ArrayList<Livro> livrosAlugados = new ArrayList<>();
    public ArrayList<Livro> livrosReservados = new ArrayList<>();
    public ArrayList<Livro> livrosDevolvidos = new ArrayList<>();

    public Usuario(String username, String senha, String contato) {
        super();
        this.username = username;
        this.senha = senha;
        this.contato = contato;
        System.out.println("Usuario cadastrado com sucesso!");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        
        Usuario other = (Usuario) obj;
        return this.username.equals(other.username);
    }

    @Override
    public String toString() {
        return "{usuario: " + this.username + " senha:" + this.senha + " livros: " + livrosAlugados +" reservas: "+ livrosReservados +"}";
    }

    public void alugarLivro(Livro livro) {
        if (livro.pegarDisponibilidade()) {
            livro.mudarDisponibilidade();
            this.livrosAlugados.add(livro);
            System.out.println("Livro alugado com sucesso!");
        }
        else {
            System.out.println("Livro indisponivel!");
        }
    }

    public void devolverLivro(Livro livro, Biblioteca biblioteca, String user) {
        String nome = livro.titulo;

        for (int i = 0; i < livrosAlugados.size(); i++) {
            Livro atual = livrosAlugados.get(i);

            if (atual.titulo.equals(nome)) {
                atual.mudarDisponibilidade();
                livrosAlugados.remove(i);
                this.livrosDevolvidos.add(atual);

                System.out.println("Livro devolvido com sucesso!");

                buscarReserva(livro,biblioteca, user);
                return;
            }
        }
        System.out.println("Você não alugou este livro!");
    }

    public void listarLivrosAlugados() {
        System.out.print(username + " -> ");

        if (livrosAlugados.size() > 0) {
            
            for (Livro livro: livrosAlugados) {
                System.out.print("Titulo:");
                System.out.print(" "+ livro.pegarTitulo());
                if (livro.checkarAtraso() == true) {
                    System.out.print(", Livro Atrasado. / ");
                } else System.out.print(", Sem Atraso. / ");
            }
        }
        else {
            System.out.print("Nenhum livro alugado");
        }
        
        System.out.println();
    }

    public void reservar(Livro livro)
    {
        if(livrosAlugados.contains(livro))
        {
           System.out.println("Operacao invalida!"); 
        }
        else
        {
            if (livro.pegarReserva() && !livro.pegarDisponibilidade()) {
                livro.mudarReserva();
                this.livrosReservados.add(livro);
                System.out.println("Livro reservado com sucesso!");
            }
            else if (livro.pegarReserva() && livro.pegarDisponibilidade())
            {
                System.out.println("Opcao invalida, livro atualmente disponivel!");
            }
            else if (!livro.pegarDisponibilidade() && !livro.pegarReserva())
            {
                System.out.println("Livro já reservado!");
            }
        }
    }
    
    public void buscarReserva(Livro livro, Biblioteca biblioteca, String user)
    {
        for(Usuario u : biblioteca.usuarios)
        {
            if(!u.username.equals(user))
            {
                removerReserva(livro, u);
            }
        }
    }

    public void removerReserva(Livro livro, Usuario u)
    {
        if (livro.pegarDisponibilidade() && u.livrosReservados.contains(livro)) {
            livro.mudarDisponibilidade();
            LocalDate hoje = LocalDate.now();
            if(hoje.equals(livro.dataDevolucao))
            {
                livro.dataDevolucao = livro.dataDevolucao.plusDays(14);
            }
            else
            {
                livro.dataDevolucao = hoje.plusDays(14);
            }
            u.livrosAlugados.add(livro);
        }
       
        String n = livro.titulo;
       
        for(int j = 0;j < u.livrosReservados.size(); j++)
        {
            Livro aux = u.livrosReservados.get(j);
            if(aux.titulo.equals(n))
            {
                aux.mudarReserva();
                u.livrosReservados.remove(j);
                System.out.println("Livro reservado foi alugado!");
                break;
            }
        }
    }
}
