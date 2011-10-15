package net.barragem.business.bo;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.barragem.persistence.entity.Validatable;
import net.barragem.view.backingbean.BaseBean;

import org.apache.commons.lang.NotImplementedException;

public abstract class BaseBo extends BaseBean {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public BaseBo(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    protected void addMensagemAtualizacaoComSucesso() {
        throw new NotImplementedException();
    }

    @Override
    protected Object evaluateElExpression(String elExpression) {
        throw new NotImplementedException();
    }

    @Override
    protected UIComponent findComponent(UIComponent c, String id) {
        throw new NotImplementedException();
    }

    @Override
    public String getContextPath() {
        return request.getContextPath();
    }

    @Override
    public HtmlDataTable getDataTable() {
        throw new NotImplementedException();
    }

    @Override
    public FacesContext getFacesContext() {
        throw new NotImplementedException();
    }

    @Override
    protected int getIndex() {
        String index = getRequest().getParameter("index");
        if (index == null || "".equals(index)) {
            index = (String) getRequestAttribute("index");
        }
        return Integer.parseInt(index);
    }

    @Override
    public HttpServletRequest getRequest() {
        return request;
    }

    @Override
    public Object getRequestAttribute(String atributo) {
        return request.getAttribute(atributo);
    }

    @Override
    public HttpServletResponse getResponse() {
        return response;
    }

    @Override
    protected ServletContext getServletContext() {
        return request.getSession().getServletContext();
    }

    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public Object getSessionAttribute(String atributo) {
        return request.getSession().getAttribute(atributo);
    }

    @Override
    public String getUrl() {
        return request.getRequestURL().toString();
    }

    @Override
    protected void refreshView() {
        throw new NotImplementedException();
    }

    @Override
    public void removeSessionAttribute(String key) {
        request.getSession().removeAttribute(key);
    }

    @Override
    public void setDataTable(HtmlDataTable dataTable) {
        throw new NotImplementedException();
    }

    @Override
    public void setRequestAttribute(String key, Object value) {
        request.setAttribute(key, value);
    }

    @Override
    public void setSessionAttribute(String key, Object value) {
        request.getSession().setAttribute(key, value);
    }

    @Override
    protected boolean valida(Validatable validatable) {
        throw new NotImplementedException();
    }

    @Override
    protected boolean validaSenha(String clientId, String senha) {
        throw new NotImplementedException();
    }

    @Override
    protected boolean updateModelValues(FacesEvent evt) {
        throw new NotImplementedException();
    }

}