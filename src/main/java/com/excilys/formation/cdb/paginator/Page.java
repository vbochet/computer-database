package com.excilys.formation.cdb.paginator;

import java.util.List;

import com.excilys.formation.cdb.exceptions.PageException;

public abstract class Page<T> {

    public Page() throws PageException {
        this.refreshContent();
    }

    private List<T> content;
    private int nbPerPage = 10;
    private long nbTotal;
    private int currentPage = 1;
    private long maxPage;
    protected String orderBy = "id";

    public List<T> getContent() {
        return content;
    }

    public int getNbPerPage() {
        return nbPerPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getNbTotal() {
        return nbTotal;
    }

    public long getMaxPage() {
        return maxPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setNbPerPage(int nb) throws PageException {
        if (nb > 0) {
            this.nbPerPage = nb;

            long newMaxPage = nbTotal / nbPerPage;
            if (nbTotal % nbPerPage != 0) {
                newMaxPage++;
            }
            this.maxPage = newMaxPage;

            this.refreshContent();
        }
    }

    public void setNbTotal(long nb) {
        if (nb > 0) {
            this.nbTotal = nb;

            long newMaxPage = nbTotal / nbPerPage;
            if (nbTotal % nbPerPage != 0) {
                newMaxPage++;
            }
            this.maxPage = newMaxPage;
        }
    }

    public void setCurrentPage(int newPage) throws PageException {
        if (newPage > 0 && (newPage - 1) * nbPerPage <= nbTotal) {
            this.currentPage = newPage;
            this.refreshContent();
        }
    }

    public abstract void setOrderBy(String orderBy) throws PageException;

    public void next() throws PageException {
        this.setCurrentPage(currentPage + 1);
        if (content.isEmpty()) {
            this.setCurrentPage(currentPage - 1);
        }
    }

    public void prev() throws PageException {
        if(currentPage > 1) {
            this.setCurrentPage(currentPage - 1);
        } else {
            this.setCurrentPage(1);
        }
    }

    public int getOffset() {
        return nbPerPage * (currentPage - 1);
    }

    protected abstract void refreshContent() throws PageException;

}
