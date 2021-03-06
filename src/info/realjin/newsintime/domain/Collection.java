package info.realjin.newsintime.domain;

import java.util.ArrayList;
import java.util.List;

public class Collection {
	private List<CollectionItem> items;
	private Integer id;
	private String name;

	public Collection() {
		items = new ArrayList<CollectionItem>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CollectionItem> getItems() {
		return items;
	}

	public void setItems(List<CollectionItem> items) {
		this.items = items;
	}

}
