package com.excilys.formation.cdb.paginator;

import java.util.List;

public abstract class Page<T> {
	
	public Page() {
		this.refreshContent();
	}

	protected List<T> content;
	protected int offset = 0;
	protected int nbPerPage = 10;

	public void setContent(List<T> content) {
		this.content = content;
	}

	public void setNbPerPage(int nb) {
		this.nbPerPage = nb;
		this.refreshContent();
	}

	public void setOffset(int offset) {
		this.offset = offset;
		this.refreshContent();
	}
	
	public void next() {
		this.setOffset(offset + nbPerPage);
		if(content.isEmpty()) {
			this.setOffset(offset - nbPerPage);
		}
	}

	public void prev() {
		int newoffset;
		if(offset > 0) {
			newoffset = offset - nbPerPage;
			
			if(newoffset < 0) {
				newoffset = 0; 
			}

			this.setOffset(newoffset);
		}
	}

	public void goToPage(int npage) {
		int oldOffset = offset;
		setOffset((npage-1) * nbPerPage);
		if(content.isEmpty()) {
			this.setOffset(oldOffset);
			System.err.println("Page number is too big");
		}
	}
	
	public List<T> getContent() {
		return content;
	}
	
	protected abstract void refreshContent();

}
