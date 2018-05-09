package br.com.javaparaweb.financeiro.util;

import br.com.javaparaweb.financeiro.usuario.UsuarioDAO;
import br.com.javaparaweb.financeiro.usuario.UsuarioDAOHibernate;

public class DAOFactory {
	
	//Métodos
	
	public static UsuarioDAO criarUsuarioDAO() {
		UsuarioDAOHibernate usuarioDAO = new UsuarioDAOHibernate();
		usuarioDAO.SetSession(HibernateUtil.getSessionFactory().getCurrentSession());
		return usuarioDAO;
	}

}
