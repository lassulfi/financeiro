package br.com.javaparaweb.financeiro.web;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import br.com.javaparaweb.financeiro.cheque.Cheque;
import br.com.javaparaweb.financeiro.cheque.ChequeRN;
import br.com.javaparaweb.financeiro.conta.Conta;
import br.com.javaparaweb.financeiro.util.MensagemUtil;
import br.com.javaparaweb.financeiro.util.RNException;

@ManagedBean(name = "chequeBean")
@RequestScoped
public class ChequeBean {

	private Cheque selecionado = new Cheque();
	private List<Cheque> lista = null;
	private Integer chequeInicial;
	private Integer chequeFinal;
	
	@ManagedProperty(value="#{contextoBean}")
	private ContextoBean contextoBean;
	
	public void salvar() {
		FacesContext context = FacesContext.getCurrentInstance();
		Conta conta = contextoBean.getContaAtiva();
		if(chequeInicial == null || chequeFinal == null) {
			String texto = MensagemUtil.getMensagem("cheque_erro_sequencia");
			context.addMessage(null, new FacesMessage(texto));
		} else if(chequeFinal < chequeInicial) {
			String texto = MensagemUtil.getMensagem("cheque_erro_inicial_final", chequeInicial, chequeFinal);
			context.addMessage(null, new FacesMessage(texto));
		} else {
			ChequeRN chequeRN = new ChequeRN();
			int totalCheques = chequeRN.salvarSequencia(conta, chequeInicial, chequeFinal);
			String texto = MensagemUtil.getMensagem("cheque_info_cadastro", chequeFinal, totalCheques);
			context.addMessage(null, new FacesMessage(texto));
			lista = null;
		}
	}
	
	public void excluir() {
		ChequeRN chequeRN = new ChequeRN();
		try {
			chequeRN.excluir(selecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("cheque_erro_cancelar");
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			context.addMessage(null, msg);
		}
		lista = null;
	}
	
	public void cancelar() {
		ChequeRN chequeRN = new ChequeRN();
		try {
			chequeRN.cancelarCheque(selecionado);
		} catch (RNException e) {
			FacesContext context = FacesContext.getCurrentInstance();
			String texto = MensagemUtil.getMensagem("cheque_erro_cancelar");
			FacesMessage msg = new FacesMessage(texto);
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
			context.addMessage(null, msg);
		}
		
		this.lista = null;
	}
	
	public List<Cheque> getLista(){
		if(lista == null) {
			Conta conta = contextoBean.getContaAtiva();
			ChequeRN chequeRN = new ChequeRN();
			lista = chequeRN.listar(conta);
		}
		
		return lista;
	}
	
	public Cheque getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Cheque selecionado) {
		this.selecionado = selecionado;
	}

	public Integer getChequeInicial() {
		return chequeInicial;
	}

	public void setChequeInicial(Integer chequeInicial) {
		this.chequeInicial = chequeInicial;
	}

	public Integer getChequeFinal() {
		return chequeFinal;
	}

	public void setChequeFinal(Integer chequeFinal) {
		this.chequeFinal = chequeFinal;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public void setLista(List<Cheque> lista) {
		this.lista = lista;
	}
}
