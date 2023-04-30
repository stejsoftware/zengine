package com.stejsoftware.zengine.data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.Objects;


@Entity
public class Story {
	private @Id
	@GeneratedValue Long id;
	private String name;
	private Byte[] data;

	public Story() {
	}

	public Story(Long id, String name, Byte[] data) {
		this.id = id;
		this.name = name;
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Story story = (Story) o;
		return Objects.equals(id, story.id) &&
			Objects.equals(name, story.name) &&
			Arrays.equals(data, story.data);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, Arrays.hashCode(data));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte[] getData() {
		return data;
	}

	public void setData(Byte[] data) {
		this.data = data;
	}
}



