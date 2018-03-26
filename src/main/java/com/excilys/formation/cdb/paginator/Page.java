package com.excilys.formation.cdb.paginator;

import java.util.List;

public abstract class Page<T> {

    public Page() {
        this.refreshContent();
    }

    private List<T> content;
    private int nbPerPage = 10;
    private long nbTotal;
    private int currentPage = 1;
    private long maxPage;

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

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setNbPerPage(int nb) {
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

    public void setCurrentPage(int newPage) {
        if (newPage > 0 && (newPage - 1) * nbPerPage <= nbTotal) {
            this.currentPage = newPage;
            this.refreshContent();
        }
    }

    public void next() {
        this.setCurrentPage(currentPage + 1);
        if (content.isEmpty()) {
            this.setCurrentPage(currentPage - 1);
        }
    }

    public void prev() {
        if(currentPage > 1) {
            this.setCurrentPage(currentPage - 1);
        } else {
            this.setCurrentPage(1);
        }
    }

    public int getOffset() {
        return nbPerPage * (currentPage - 1);
    }

    protected abstract void refreshContent();

}
