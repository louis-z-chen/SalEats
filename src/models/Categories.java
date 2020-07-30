package models;

public class Categories {
	private String alias;
    private String title;
    
    public Categories(String alias, String title) {
    	this.alias = alias;
    	this.title = title;
    }
    
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias){
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
