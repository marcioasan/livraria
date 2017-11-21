package br.com.caelum.livraria.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.util.ForwardView;
import br.com.caelum.livraria.util.RedirectView;

@ManagedBean
@ViewScoped
public class AutorBean {

	private Autor autor = new Autor();
	private Integer autorId;

	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public void carregarAutorPeloId(){
		this.autor = new DAO<Autor>(Autor.class).buscaPorId(autorId);
	}
	
	public Autor getAutor() {
		return autor;
	}

/*	public String gravar() {
		System.out.println("Gravando autor " + this.autor.getNome());

		new DAO<Autor>(Autor.class).adiciona(this.autor);
		
		this.autor = new Autor();
		
		return "livro?faces-redirect=true";
	}*/
	
	public void carregarAutor(Autor autor){
		System.out.println("Carregando autor");
		this.autor = autor;
	}
	
	public RedirectView gravar(){
	    
	    if(this.autor.getId() == null){
	    	System.out.println("Gravando novo autor " + this.autor.getNome());
	    	new DAO<Autor>(Autor.class).adiciona(this.autor);
	    }else{
	    	System.out.println("Alterando Autor");
	    	new DAO<Autor>(Autor.class).atualiza(this.autor);
	    }
	    
	    this.autor = new Autor();

	    System.out.println("-------> Chamando RedirectView('livro')");
	    return new RedirectView("livro");
	}
	
	public void remover(Autor autor){
		System.out.println("Removendo autor");
		new DAO<Autor>(Autor.class).remove(autor);
	}
	
	//para redirecionamento no lado do servidor
/*	public ForwardView gravar() {
	    System.out.println("Gravando autor " + this.autor.getNome());

	    new DAO<Autor>(Autor.class).adiciona(this.autor);

	    this.autor = new Autor();

	    return new ForwardView("livro");
	}*/
}
