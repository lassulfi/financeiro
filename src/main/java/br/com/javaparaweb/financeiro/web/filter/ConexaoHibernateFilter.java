package br.com.javaparaweb.financeiro.web.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import br.com.javaparaweb.financeiro.util.HibernateUtil;

@WebFilter(urlPatterns = {"*.jsf"})
public class ConexaoHibernateFilter implements Filter {

	//Atributos
	private SessionFactory sf;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, 
			ServletResponse servletResponse, FilterChain chain)
			throws ServletException {
		// TODO Auto-generated method stub
		Session currentSession = this.sf.getCurrentSession();
		
		Transaction transaction = null;
		
		try {
			transaction = currentSession.beginTransaction();
			chain.doFilter(servletRequest, servletResponse);
			transaction.commit();
			if (currentSession.isOpen()) {
				currentSession.close();
			}
		} catch (Throwable ex) {
			// TODO: handle exception
			try {
				if (transaction.isActive()) {
					transaction.rollback();
				}
			} catch (Throwable t) {
				// TODO: handle exception
				t.printStackTrace();
			}
			throw new ServletException(ex);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		this.sf = HibernateUtil.getSessionFactory();
	}

}
