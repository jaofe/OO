import java.util.*;

public class Biblioteca {
    ArrayList<Usuario> usuarios = new ArrayList<>();
    ArrayList<Livro> livros = new ArrayList<> ();

    public void criarUsuario(String username, String senha, String contato) {
        Usuario novoUsuario = new Usuario(username, senha, contato);
        this.usuarios.add(novoUsuario);
    }

    public void cadastrarLivro(Livro livro) {
        this.livros.add(livro);
    }

    public void listarLivros() {
        for (Livro livro: livros) {
            livro.printLivro();
        }
    } 

    public void listarUsuarios() {
        for (Usuario usuario : this.usuarios) {
            usuario.listarLivrosAlugados();
        }
    }

    public void listarLivrosAlugados()
    {
        Usuario usuario = null;
        int count = 0;

        for (Usuario u : usuarios) {
            usuario = u;
            if(usuario.livrosAlugados.size() != 0)
            {
                usuario.listarLivrosAlugados();
                System.out.println();
                count ++;
            }
        }
        if(count == 0)
        {
            System.out.println("Nenhum Livro Alugado!");
        }
    }

    public void listarLivrosAtrasados() {
        int count = 0;
        for(Livro l: livros) {
            if (l.disponibilidade == false) {
                if (l.checkarAtraso() == true) {
                    l.printLivro();
                    for (Usuario u: usuarios) {
                        for (Livro liv: u.livrosAlugados) {
                            if (l.titulo.equals(liv.titulo)) {

                                System.out.print("O livro está com o seguinte usuario: ");
                                System.out.println(u.username + " Forma de contato: " + u.contato);
                                break;
                            }
                        }

                    }
                    count++;
                }
            }
        }
        if(count == 0)
        {
            System.out.println("Nenhum Livro Atrasado!");
        }
    }

    public void criarAdmin(String username, String senha, String contato) {
        Usuario novoUsuario = new Admin(username, senha, contato);
        this.usuarios.add(novoUsuario);
    }

    public void listarAdmins() {
        for (Usuario usuario : this.usuarios)
            System.out.println(usuario.username + " Contato: " + usuario.contato);
    }

    Livro buscarLivro(String titulo) {
        Livro livro = null;
    
        for (Livro l : this.livros) {
            if (l.titulo.equals(titulo))
                livro = l;
        }

        return livro;
    }

    public Usuario buscarUsuario(String username) {
        for (Usuario usuario : this.usuarios) {
            if (usuario.username.equals(username)) {
                return usuario;
            }
        }
    
        for (Usuario usuario : this.usuarios) {
            if (usuario.username.equals(username)) {
                return usuario;
            }
        }

        return null;
    }
}
