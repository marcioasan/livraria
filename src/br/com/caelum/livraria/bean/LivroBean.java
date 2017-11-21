package br.com.caelum.livraria.bean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import br.com.caelum.livraria.dao.DAO;
import br.com.caelum.livraria.modelo.Autor;
import br.com.caelum.livraria.modelo.Livro;

@ManagedBean
//@RequestScoped // --> É o default. O bean dura apenas durante a requisição.
@ViewScoped  // o bean dura enquanto a tela existir
public class LivroBean {

	private Livro livro = new Livro();
	
	private Integer autorId;
	private Integer livroId;
	
	public Integer getLivroId() {
		return livroId;
	}

	public void setLivroId(Integer livroId) {
		this.livroId = livroId;
	}
	public Integer getAutorId() {
		return autorId;
	}

	public void setAutorId(Integer autorId) {
		this.autorId = autorId;
	}

	public Livro getLivro() {
		return livro;
	}
	
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	
	public List<Autor> getAutores(){
		return new DAO<Autor>(Autor.class).listaTodos();
	}
	
	public List<Autor> getAutoresDoLivro(){
		return this.livro.getAutores();
	}

	public List<Livro> getLivros() {
		return new DAO<Livro>(Livro.class).listaTodos();
	}	
	
	public void gravarAutor(){
		Autor autor = new DAO<Autor>(Autor.class).buscaPorId(this.autorId);
		if(autor == null){
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Selecione um Autor"));
		}else{
			this.livro.adicionaAutor(autor);
			System.out.println("Livro escrito por: " + autor.getNome());
		}
	}

	public void carregarPeloId(){
		this.livro = new DAO<Livro>(Livro.class).buscaPorId(this.livroId);
	}
	
	public void gravar() {
		System.out.println("Gravando livro " + this.livro.getTitulo());

		if (livro.getAutores().isEmpty()) {
			//throw new RuntimeException("Livro deve ter pelo menos um Autor.");
			FacesContext.getCurrentInstance().addMessage("autor", new FacesMessage("Livro deve ter pelo menos um Autor."));
			return;
		}
		
		if(this.livro.getId() == null){
			new DAO<Livro>(Livro.class).adiciona(this.livro);
		}else{
			new DAO<Livro>(Livro.class).atualiza(this.livro);
		}
		this.livro = new Livro();
	}

	/**
	  Usando no livro.xhtml no <h:commandLink a tag <f:setPropertyActionListener target="#{livroBean.livro}" value="#{livro}" />
	  não é preciso de um método carregar para carregar o livro para o formulário ao alterar
	  
	  <!-- isso não funcionava no JSF 1.x -->
	  <h:commandLink value="Alterar" action="#{livroBean.carregar(livro)}" />
	  Hoje em dia podemos utilizar esses parâmetros na EL, então não há mais tanta necessidade de usar o componente f:setPropertyActionListener.
	  
	  
		<!-- isso não funcionava no JSF 1.x -->
		<!-- <h:commandLink value="altera" action="#{livroBean.carregar(livro)}"/> -->
		<!-- Hoje em dia podemos utilizar esses parâmetros na EL, então não há mais tanta necessidade de usar o componente f:setPropertyActionListener. -->
	  
	 * */
	
	
/*	public void carregar(Livro livro){
		System.out.println("Carregando livro");
		this.livro = livro;
	}*/
	
	public void remover(Livro livro){
		System.out.println("Removendo livro");
		new DAO<Livro>(Livro.class).remove(livro);
	}

	public void removerAutorDoLivro(Autor autor){
		this.livro.removeAutor(autor);
	}
	
	public String formAutor(){
		System.out.println("Chamando o formulário do Autor");
		return "autor?faces-redirect=true";
	}

	/**
		O primeiro parâmetro é do tipo javax.faces.context.FacesContext e permite obter informações da view processada no momento.
		O segundo parâmetro é do tipo javax.faces.component.UIComponent e é um referencia ao componente que está sendo validado, normalmente um UIInput.
		O terceiro parâmetro é do tipo java.lang.Object e é um objeto que representa o valor digitado pelo usuário. O objeto já foi convertido.		
	 * */
	public void comecaComDigitoUm(FacesContext fc, UIComponent component, Object value) throws ValidatorException{
		String valor = value.toString();
		if(!valor.startsWith("1")){
			throw new ValidatorException(new FacesMessage("ISBN deve começar com 1"));
		}
	}
}
